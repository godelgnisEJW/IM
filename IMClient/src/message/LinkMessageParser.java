package message;

import model.Connector;
import model.Server.ConnectionHandler;
import model.User;
import model.UserCache;

public class LinkMessageParser extends MessageParser{

	private static LinkMessageParser parser = new LinkMessageParser();
	private LinkMessageParser() {
		super(4);
	}

	public static LinkMessageParser getParser() {
		return parser;
	}
	
	@Override
	public void parse(Message msg, Object arg) {
		ConnectionHandler handler = (ConnectionHandler)arg;
		String uString = new String(msg.getContent());
		User user = UserCache.getUserBy(uString);
		Connector.getConnector().add(user, handler);
	}

}
