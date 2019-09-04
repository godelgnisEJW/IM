package message;

import model.Server.ConnectionHandler;

public abstract class MessageParser {
	private int type;
	
	public MessageParser(int type) {
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
	public abstract void parse(Message msg,ConnectionHandler handler);
}
