package message;

import model.Server.ConnectionHandler;
import model.SessionCache;
import model.User;
import model.UserCache;
import view.SessionController;

public class TextMessageParser extends MessageParser{

	private final static TextMessageParser parser = new TextMessageParser();
	
	private TextMessageParser() {
		super(3);
	}
	
	public static TextMessageParser getParser() {
		return parser;
	}
	

	@Override
	public void parse(Message msg, Object arg) {
		String mString = new String(msg.getContent());
		//处理文本消息
		ConnectionHandler handler = (ConnectionHandler)arg;
		String[] token = mString.split("#");
		String nickName = token[0];
		String content = token[1];
		User user = UserCache.getUserBy(nickName);
		SessionController controller = SessionCache.getSessionControllerBy(user);
		controller.updateRecordArea(nickName, content, false);
	}

}
