package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.AcceptPendingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import event.Event;
import event.EventType;
import message.Message;
import util.Converter;

public class Server implements Runnable{
	private int port;
	private int poolSize;
	private String IP;
	private ExecutorService pool;
	private static Server server = null;
	private boolean flag = false;
	private static ServerSocket serverSocket = null;
	
	private Server(int port, int poolSize, String IP) {
		this.port = port;
		this.poolSize = poolSize;
		this.IP = IP;
	}
	
	public static Server getInstance(int port, int poolSize, String IP) {
		if (server == null) {
			server = new Server(port, poolSize, IP);
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				new Event("端口被占用", EventType.SERVER_ERROR);
			}
			return server;
		}else {
			if (server.getPoolSize() != poolSize) {
				server.setPoolSize(poolSize);
			}
			return server;
		}
	}
	
	public void submit(Runnable task) {
		pool.submit(task);
	}
	
	public int getPort() {
		while(serverSocket == null);
		return serverSocket.getLocalPort();
	}
	
	public int getPoolSize() {
		return this.poolSize;
	}
	
	private void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	
	public void start() {
		try {
			pool = Executors.newFixedThreadPool(poolSize);
			new Event("服务器开始启动服务......\n服务IP地址为" + IP +",监听" + port + "端口,最大在线人数调整为" + poolSize + "人。", EventType.SERVER_START);
			this.flag = true;
			while(flag) {
				try {
					Socket connection = serverSocket.accept();
					ConnectionHandler handler = new ConnectionHandler(connection);
					pool.submit(handler);
				} catch (IOException e) {
					//忽略accept错误
				}
			}
		} catch (AcceptPendingException e) {
			// TODO: handle exception
		}finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					//忽略
				}
			}
		}
	}
	
	public void close() {
		new Event("正在关闭服务器....", EventType.SERVER_CLOSE);
		this.flag = false;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new Event("已关闭服务器", EventType.SERVER_CLOSE);
	}
	
	
	@Override
	public void run() {
		this.start();
	}
	
	public class ConnectionHandler implements Runnable{

		private Socket connection;
		private boolean isStarted = true;
		private String key;
		private String ip;
		private int connectPort;
		private Message message;
		
		private OutputStream out;
		private InputStream in;
		
		public ConnectionHandler(Socket connection) {
			this.connection = connection;	
			key = connection.getRemoteSocketAddress().toString().substring(1);
			try {
				in = new BufferedInputStream(connection.getInputStream());
				out = new BufferedOutputStream(connection.getOutputStream());
			} catch (IOException e) {
				//忽略
			}
			new Event(this, EventType.NEW_CONNECTION);
		}
		//会一直阻塞到读取到完整的长度为len的字节数组
		private void read(InputStream in, byte[] array, int len) {
			
			int off = 0;
			int restLen = 0;
			while(off < len) {
				try {
					restLen = len - off;
					off += in.read(array, off, restLen);
				} catch (IOException e) {
					new Event("连接不可用", EventType.SERVER_ERROR);
					new Event(this, EventType.CLOSE_CONNECTION);
				}
			}
		}
		
		@Override
		public void run() {
			byte[] type= new byte[1];
			byte[] length = new byte[4];
			while(isStarted) {
//					in.read(type, 0, 1);
				read(in, type, 1);
//					in.read(length,0, 4);
				read(in, length, 4);
				int _lenngth = Converter.changeToInt(length);
				byte[] content = new byte[_lenngth];
//					int recvLen = in.read(content, 0, content.length);
				read(in, content, _lenngth);
				int _type = Converter.changeToInt(type);
				message = new Message(_type, content);
				//接受到新消息
				new Event(this, EventType.NEW_MESSAGE);
			}
		}
		
		public String getIp() {
			return ip;
		}

		public int getConnectPort() {
			return connectPort;
		}
		
		public void end() {
			this.isStarted = false;
		}
		
		public Socket getConnection() {
			return this.connection;
		}
		
		public Message getMessage() {
			return this.message;
		}
		
		public String getKey() {
			return this.key;
		}

		public InputStream getInputStream() {
			return in;
		}

		public OutputStream getOutputStream() {
			return out;
		}
		
	}


}
