package message;

import javafx.application.Platform;
import model.Server.ConnectionHandler;
import model.User;
import util.TimeGenerator;
import view.DataManagementController;
import view.OnlineManagementController;

public class TextMessageParser extends MessageParser{

	private final static TextMessageParser parser = new TextMessageParser();
	
	private DataManagementController dmController;
	
	private OnlineManagementController omController;
	
	private TextMessageParser() {
		super(3);
	}
	
	public static TextMessageParser getParser() {
		return parser;
	}
	
	public void setDmController(DataManagementController dmController) {
		this.dmController = dmController;
	}
	
	public void setOmController(OnlineManagementController omController) {
		this.omController = omController;
	}
	
	@Override
	public void parse(Message msg, ConnectionHandler handler) {
		String mString = new String(msg.getContent());
		//处理文本消息
		String[] token = mString.split("#");
		String nickName = token[0];
		String content = token[1];
		
		StringBuilder builder = new StringBuilder();
		Platform.runLater(()->{
			builder.append(TimeGenerator.getTime());
			builder.append("\t");
			builder.append(nickName);
			builder.append(":\n");
			builder.append(content);
			builder.append("\n");
			omController.getNoticeLog().appendText(builder.toString());
			omController.broadcast(mString);
		});
	}
}
