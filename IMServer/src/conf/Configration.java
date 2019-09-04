package conf;

import application.App;
import event.EventDispatch;
import handler.ConnectHandler;
import handler.ServerStatusHandler;
import handler.SystemExitHandler;
import handler.SystemStartHandler;
import message.LoginMessageParser;
import message.MessageDispatch;
import message.TextMessageParser;

public class Configration {

	private App app;
	
	public Configration(App app) {
		this.app = app;
	}
	
	public void init() {
		EventDispatch.getDispatch().resgisterCustomer(MessageDispatch.getMessageDispatch());
		
		EventDispatch.getDispatch().resgisterCustomer(SystemStartHandler.getHandler());
		
		EventDispatch.getDispatch().resgisterCustomer(SystemExitHandler.getHandler());
		
		MessageDispatch.getMessageDispatch().registerParser(LoginMessageParser.getParser());
		
		MessageDispatch.getMessageDispatch().registerParser(TextMessageParser.getParser());
		
		EventDispatch.getDispatch().resgisterCustomer(ServerStatusHandler.getHandler());
		
    	EventDispatch.getDispatch().resgisterCustomer(ConnectHandler.getHandler());
    	
	}
}
