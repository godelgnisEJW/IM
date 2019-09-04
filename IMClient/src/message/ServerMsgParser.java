package message;


import application.App;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import model.Client;
import model.SessionCache;
import model.User;
import model.UserCache;
import model.Server.ConnectionHandler;
import view.SessionController;
import view.SessionManagerController;
import view.TipView;

public class ServerMsgParser extends MessageParser {
	private final static String SERVER_INFO = "nickName:聊天大厅;listenPort:1088;ip:127.0.0.1;count:0000";

	private static ServerMsgParser parser = new ServerMsgParser();
	
	private SessionManagerController controller;
	
	private ServerMsgParser() {
		super(2);
	}
	
	public static ServerMsgParser getParser() {
		return parser;
	}
	
	public void setSessionManagerController(SessionManagerController controller) {
		this.controller = controller;
	}

	@Override
	public void parse(Message msg, Object arg) {
		
		Client client = (Client)arg;
		String content = new String(msg.getContent());
		if (content.startsWith("success")) {
			Platform.runLater(()->{
				App.getStage().hide();
				String uContent = content.substring(content.indexOf(";") + 1);
				User user = UserCache.getUserBy(uContent);
				App.setAppUser(user);
				controller.showManagerPane(user.getNickName());
				
				ObservableList<User> list = controller.getOnlineUserList().getItems();
				User u = UserCache.getUserBy(SERVER_INFO);
				list.add(u);
				
			});
		}else if (content.startsWith("false")) {
			Platform.runLater(()->{
				String reason = content.substring(content.indexOf(":") + 1);
				TipView<?> tipView = new TipView<>(reason, App.getStage());
				tipView.show();
			}); 
		}else if(content.startsWith("ul")){
			ObservableList<User> list = controller.getOnlineUserList().getItems();
			String uContent = content.substring(content.indexOf("#") + 1);
			String[] users = uContent.split("#");
			Platform.runLater(()->{
				//清空用户列表,保留第一个作为聊天大厅，可以接收服务器消息，也可以接受其他用户的消息
				list.remove(1, list.size());
//				list.removeAll(list);
				
				//重新添加在线用户信息
				for (String uString : users) {
					User user = UserCache.getUserBy(uString);
					if (!user.equals(App.getAppUser())) {
						list.add(user);
					}
				}
				//修改在线人数
				controller.setOnlineNum(list.size() - 1);
			});
		}else {
			String[] token = content.split("#");
			
			String nickName = token[0];
			String msgContent = token[1];
			User user = UserCache.getUserBy("聊天大厅");
			SessionController controller = SessionCache.getSessionControllerBy(user);
			if (nickName.equals("聊天大厅")) {
				nickName = "聊天大厅管理员";
			}
			controller.updateRecordArea(nickName, msgContent, false);
		}
	}
}
