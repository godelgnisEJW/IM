package message;

import java.io.OutputStream;
import java.net.Socket;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import model.Connector;
import model.Server.ConnectionHandler;
import model.User;
import util.TimeGenerator;
import view.DataManagementController;
import view.OnlineManagementController;
import view.RunningManagementController;

public class LoginMessageParser extends MessageParser {
	
	private String count;
	private String password;
	private int listenPort;
	
	private OnlineManagementController controller;
	
	private RunningManagementController rmController;
	
	private DataManagementController dmController;
	
	private final static LoginMessageParser parser = new LoginMessageParser();
	
	private LoginMessageParser() {
		super(1);
	}

	public static LoginMessageParser getParser() {
		return parser;
	}
	
	@Override
	public void parse(Message msg, ConnectionHandler handler) {
		byte[] content = msg.getContent();
		String text = new String(content);
		String[] token = text.split("(:|;)");
		count = token[1];
		password = token[3];
		listenPort = Integer.parseInt(token[5]);
		String time = TimeGenerator.getTime();
		String key = handler.getKey();
		
		OutputStream out = handler.getOutputStream();
		Platform.runLater(()->{
			rmController.getServerLog().appendText(time + "\n新用户登录\t账号:" + count + "密码:" + password + "监听端口:" + listenPort + "\n");
			rmController.getServerLog().appendText(TimeGenerator.getTime() + "\n进行校验........\n");
			String err = "";
			if (dmController.check(count, password)) {
				//获取在线用户列表
				ObservableList<User> list =  controller.getOnlineUserList().getItems();
				//从数据管理控制器获取用户
				User user = dmController.getUser(count, password);
				if (!list.contains(user)) {
					/*****往connector中添加socket连接*****/
					Connector.getConnector().add(key, handler);
					//往在线列表中添加用户
					list.add(user);
					//设置用户的监听端口
					user.setListenPort(listenPort);
					user.setPort(handler.getConnectPort());
					user.setIp(handler.getIp());
					//校验成功
					rmController.getServerLog().appendText(TimeGenerator.getTime() + "\n校验成功!!\n[" + key + "] 昵称:" + user.getNickName() + "\t上线\n");
					//获取在线列表的人数
					int onlineNum = list.size();
					//修改在线人数
					rmController.getOnlineNum().setText(onlineNum + "人");
					//返回信息组成
					String response = "success;" + user.info();
					Message.send(out, 2, response.getBytes());
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
				}else {
					err = "该用户已经登陆";
					rmController.getServerLog().appendText(TimeGenerator.getTime() + "\n校验失败\t原因:" + err + "\n");
					Message.send(out, 2, ("false:" + err).getBytes());
				}
			}else {
				err = "账户不存在或者密码错误";
				rmController.getServerLog().appendText(TimeGenerator.getTime() + "\n校验失败\t原因:" + err + "\n");
				Message.send(out, 2, ("false:" + err).getBytes());
			}
		});
	}

	public String getCount() {
		return count;
	}

	public String getPassword() {
		return password;
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
}
