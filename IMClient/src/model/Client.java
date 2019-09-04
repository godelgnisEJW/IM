package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import event.Event;
import event.EventType;
import message.Message;
import util.Converter;

public class Client implements Runnable{
	//服务器的监听端口
	private int port = 1088;
	//服务器IP地址
	private String IP = "127.0.0.1";
	private Socket socket;
	private String count;
	private String password;
	//本地服务器线程的监听端口
	private int listenPort;
	
	private InputStream in;
	private OutputStream out;
	private Message message;
	private boolean flag = true;
	
	private static Client client;
	
	public Client(String count,String password,int listenPort) {
		this.count = count;
		this.password = password;
		this.listenPort = listenPort;
		client = this;
	}
	
	public static Client getClient() {
		return client;
	}
	
	public void connect(String count, String password, int listenPort){
		try {
			socket = new Socket(IP, port);
			out = socket.getOutputStream();
			byte[] type = {1};
			String msg = "count:" + count + ";passowrd:" + password + ";listenPort:" + listenPort;
			byte[] content = msg.getBytes();
			byte[] len = Converter.changeToBytes(content.length);
			out.write(type);
			out.write(len);
			out.write(content);
			out.flush();
			
			in = socket.getInputStream();
			while(flag) {
				in.read(type, 0, 1);
				in.read(len,0, 4);
				int length = Converter.changeToInt(len);
				content = new byte[length];
				in.read(content, 0, content.length);
				int _type = Converter.changeToInt(type);
				message = new Message(_type, content);
				//接受到新消息
				new Event(this, EventType.SERVER_MESSAGE);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		this.flag = false;
	}
	
	@Override
	public void run() {
		connect(count, password, listenPort);
	}

	public InputStream getIn() {
		return in;
	}

	public OutputStream getOut() {
		return out;
	}
	
	public Message getMessage() {
		return message;
	}
}
