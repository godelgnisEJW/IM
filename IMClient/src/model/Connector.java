package model;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import application.App;
import message.Message;
import model.Server.ConnectionHandler;

public class Connector {
	
	private final static Connector connector = new Connector();
	/**
	 * 之后重新将其命名为cache，在client中会起到极大的作用
	 */
	private static HashMap<User, ConnectionHandler> cache = new HashMap<>();
	private Connector() {
		
	}
	public static Connector getConnector() {
		return connector;
	}
	public static ConnectionHandler getHandlerBy(User user) {
		ConnectionHandler handler = null;
		
		if ((handler = cache.get(user)) != null) {
			System.out.println("从缓存中获取" + user.getNickName());
			return handler;
		}
		try {
			Socket connection = new Socket(user.getIp(), user.getListenPort());
			Server server = Server.getInstance(0, 25, "127.0.0.1");
			handler = server.new ConnectionHandler(connection);
			server.submit(handler);
			OutputStream out = connection.getOutputStream();
			//发送用户消息,这条信道是由客户端主动发起的，所以就需要将客户端的信息发送过去
			Message.send(out, 4, App.getAppUser().info().getBytes());
			System.out.println("创建新连接");
		} catch (IOException e) {
			//忽略
			System.out.println("创建Socket连接失败");
		}
		cache.put(user, handler);
		return handler;
		
	}
	public HashMap<User, ConnectionHandler> getCache() {
		return cache;
	}
	public int size() {
		return cache.size();
	}
	public void add(User user, ConnectionHandler handler) {
		cache.put(user, handler);
	}
	public void remove(User user) {
		cache.remove(user);
	}
	public boolean contains(User user) {
		return cache.containsKey(user);
	}
	public String[] allUser() {
		Set<User> set = cache.keySet();
		return (String[]) set.toArray();
	}
	public Collection<ConnectionHandler> allConnection() {
		return cache.values();
	}
}
