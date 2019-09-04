package message;

import java.io.File;

import application.App;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import model.Connector;
import model.Server.ConnectionHandler;
import model.User;
import model.UserCache;
import view.TipView;

public class FileMessageParser extends MessageParser{

	private static FileMessageParser  parser= new FileMessageParser();
	private FileMessageParser() {
		super(5);
	}

	public static FileMessageParser getParser() {
		return parser;
	}
	@Override
	public void parse(Message msg, Object arg) {
		String mString = new String(msg.getContent());
		//处理文件接收消息
		String[] token = mString.split("#");
		String nickName = token[0];
		String content = token[1];
		User user = UserCache.getUserBy(nickName);
		
		File file = new File(content);
		String fileName = file.getName();
		Platform.runLater(()->{
			TipView<?> tipView = new TipView<>("是否接受来自 " + user.getNickName() + " 的文件: " + fileName + "?", App.getStage());
			
			tipView.setOnConfirmCall(()->{
				FileChooser fileChooser = new FileChooser();
		    	fileChooser.setTitle("Save Resource File");
		    	fileChooser.setInitialFileName(fileName);
		    	File saveFile = fileChooser.showSaveDialog(App.getStage());
		    	ConnectionHandler handler = Connector.getHandlerBy(user);
		    	if (saveFile != null) {
		    		Message.send(handler.getOutputStream(), 6, ("yes#" + App.getAppUser().getNickName() + "#" + file.getAbsolutePath() + "#" + saveFile.getAbsolutePath()).getBytes());
		    	}else {
		    		Message.send(handler.getOutputStream(), 6, "no".getBytes());
				}
				return null;
			});
			tipView.show();
		});
		
	}
	
}
