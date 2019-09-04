package conf;

import application.App;
import emojione.fx.EmojiConversion;
import emojione.fx.EmojiList;
import event.EventDispatch;
import handler.SystemExitHandler;
import handler.SystemStartHandler;
import message.FileCheckMessageParser;
import message.FileMessageParser;
import message.FileRecvParser;
import message.LinkMessageParser;
import message.MessageDispatch;
import message.ServerMsgParser;
import message.TextMessageParser;
import message.VideoDataMessageParser;
import message.VideoRequestMessageParser;

public class Configration {

	private App app;
	
	public Configration(App app) {
		this.app = app;
	}
	
	public void init() {
		
		EmojiList.getEmojiList();
		
		EmojiConversion.getEmojiConversion();
		
		EventDispatch.getDispatch().resgisterCustomer(MessageDispatch.getMessageDispatch());
		
		EventDispatch.getDispatch().resgisterCustomer(SystemStartHandler.getHandler());
		
		EventDispatch.getDispatch().resgisterCustomer(SystemExitHandler.getHandler());
		
		MessageDispatch.getMessageDispatch().registerParser(ServerMsgParser.getParser());
		
		MessageDispatch.getMessageDispatch().registerParser(LinkMessageParser.getParser());
		
		MessageDispatch.getMessageDispatch().registerParser(TextMessageParser.getParser());
		
		MessageDispatch.getMessageDispatch().registerParser(FileMessageParser.getParser());
		
		MessageDispatch.getMessageDispatch().registerParser(FileCheckMessageParser.getParser());
		
		MessageDispatch.getMessageDispatch().registerParser(FileRecvParser.getParser());
		
		MessageDispatch.getMessageDispatch().registerParser(VideoRequestMessageParser.getParser());
		
		MessageDispatch.getMessageDispatch().registerParser(VideoDataMessageParser.getParser());
	}
}
