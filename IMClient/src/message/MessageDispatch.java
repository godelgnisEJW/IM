package message;

import java.util.HashSet;
import java.util.Set;

import event.EvenCustomer;
import event.Event;
import event.EventCustomeType;
import model.Client;
import model.Server.ConnectionHandler;

public class MessageDispatch extends EvenCustomer{

	private final static MessageDispatch dispatch = new MessageDispatch();
	private Set<MessageParser> parsers = new HashSet<>();
	
	private MessageDispatch() {
		super(EventCustomeType.NEW_MESSAGE);
		super.addCustomType(EventCustomeType.SERVER_MESSAGE);
	}

	public static MessageDispatch getMessageDispatch() {
		return dispatch;
	}
	
	@Override
	public void exec(Event event) {
		Object source = event.getSource();
//		System.out.println("处理" + event.getType().getValue() + "类型事件");
		if (event.getType().getValue() == EventCustomeType.SERVER_MESSAGE.getValue()) {
			for (MessageParser parser : parsers) {
				if (parser.getType() == 2) {
					Client client = (Client)source;
					parser.parse(client.getMessage(),client);
				}
			}
		}else {
			ConnectionHandler handler = (ConnectionHandler)source;
			Message message = handler.getMessage();
			for (MessageParser parser : parsers) {
				if (message.getType() == parser.getType()) {
					parser.parse(message, handler);
				}
			}
		}
	}

	public void registerParser(MessageParser parser) {
		this.parsers.add(parser);
	}
}
