package handler;

import event.EvenCustomer;
import event.Event;
import event.EventCustomeType;
import event.EventType;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import message.Message;
import model.Connector;
import model.User;
import model.Server.ConnectionHandler;
import util.TimeGenerator;
import view.DataManagementController;
import view.OnlineManagementController;
import view.RunningManagementController;

public class ConnectHandler extends EvenCustomer{
	
	private OnlineManagementController controller;
	
	private RunningManagementController rmController;
	
	private DataManagementController dmController;
	
	private Connector connector = Connector.getConnector();
	
	private final static ConnectHandler handler = new ConnectHandler();
	
	private ConnectHandler() {
		super(EventCustomeType.NEW_CONNECTION);
		super.addCustomType(EventCustomeType.CLOSE_CONNECTION);
	}
	
	public static ConnectHandler getHandler() {
		return handler;
	}

	public void setOnlineManagementController(OnlineManagementController controller) {
		this.controller = controller;
	}
	
	public void setRunningManagementController(RunningManagementController controller) {
		this.rmController = controller;
	}
	
	public void setDataManagementController(DataManagementController controller) {
		this.dmController = controller;
	}
	
	@Override
	public void exec(Event event) {
		ConnectionHandler source = (ConnectionHandler)event.getSource();
		String key = source.getKey();
		String time = TimeGenerator.getTime();
		if (event.getType().getValue() == EventType.CLOSE_CONNECTION.getValue()) {
			User user = dmController.getUser(key);
			ObservableList<User> list = controller.getOnlineUserList().getItems();
			Platform.runLater(()->{
				list.remove(user);
				rmController.getServerLog().appendText(time + "\n[" + key + "] 昵称:" + user.getNickName() + "\t下线\n");
				rmController.getOnlineNum().setText("" + list.size());
				connector.remove(source.getKey());
				//更新在线用户列表
				StringBuilder builder = new StringBuilder("ul");
				for (User u: list) {
					//使用"#"分割每一个用户信息
					builder.append("#");
					builder.append(u.info());
				}
				//将列表发送给用户,信息类型为3
				for (ConnectionHandler connectionHandler : Connector.getConnector().allConnection()) {
					Message.send(connectionHandler.getOutputStream(), 3, builder.toString().getBytes());
				}
			});
		}
	}
}
