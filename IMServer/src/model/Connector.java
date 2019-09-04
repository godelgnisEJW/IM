package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import model.Server.ConnectionHandler;

public class Connector {
	
	private final static Connector connector = new Connector();
	/**
	 * 之后重新将其命名为cache，在client中会起到极大的作用
	 */
	private HashMap<String, ConnectionHandler> cache = new HashMap<>();
	private Connector() {
		
	}
	public static Connector getConnector() {
		return connector;
	}
	public HashMap<String, ConnectionHandler> getCache() {
		return this.cache;
	}
	public int size() {
		return this.cache.size();
	}
	public void add(String IP, ConnectionHandler handler) {
		cache.put(IP, handler);
	}
	public void remove(String IP) {
		cache.remove(IP);
	}
	public boolean contains(String IP) {
		return cache.containsKey(IP);
	}
	public String[] allIP() {
		Set<String> set = cache.keySet();
		return (String[]) set.toArray();
	}
	public Collection<ConnectionHandler> allConnection() {
		return cache.values();
	}
}
