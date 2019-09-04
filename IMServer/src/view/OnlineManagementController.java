package view;

import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;

import application.App;
import event.EventDispatch;
import handler.ConnectHandler;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import message.LoginMessageParser;
import message.Message;
import message.TextMessageParser;
import model.Connector;
import model.User;
import util.TimeGenerator;
import model.Server.ConnectionHandler;


public class OnlineManagementController implements Initializable{
	
	@FXML
    private JFXListView<User> onlineUserList;

    @FXML
    private TextArea noticeLog;

    @FXML
    private TextArea notice;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ConnectHandler handler = ConnectHandler.getHandler();
    	handler.setOnlineManagementController(this);
    	
    	LoginMessageParser parser = LoginMessageParser.getParser();
    	parser.setOnlineManagementController(this);
    	
    	TextMessageParser.getParser().setOmController(this);
    }

    @FXML
    void sentNotice(ActionEvent event) {
    	String msg = notice.getText();
    	String content = msg.trim();
    	if (!content.equals("")) {
    		int selectedIndex = onlineUserList.getSelectionModel().getSelectedIndex();
    		if(selectedIndex == -1) {
    			//没有选中时群发消息
    			broadcast("聊天大厅#" + content);
    		}else {
    			//有选中时就单发给选中的这个人
    			//暂时只支持单选
				User user = onlineUserList.getSelectionModel().getSelectedItem();
				String key = user.getIp() + ":" + user.getPort();
				ConnectionHandler handler = Connector.getConnector().getCache().get(key);
				if (handler != null) {
					Message.send(handler.getOutputStream(), 2, ("聊天大厅#@" + user.getNickName() + " " +  content).getBytes());
				}
				onlineUserList.getSelectionModel().clearSelection(selectedIndex);
			}
    		Platform.runLater(()->{
    			noticeLog.appendText(TimeGenerator.getTime() + "\t服务器发布公告:\n" + content + "\n");
    		});
		}
    	notice.clear();
    	
    }
    
    public void broadcast(String msg) {
    	for (ConnectionHandler connectionHandler : Connector.getConnector().allConnection()) {
			Message.send(connectionHandler.getOutputStream(), 2, msg.toString().getBytes());
		}
    }

	public JFXListView<User> getOnlineUserList() {
		return onlineUserList;
	}

	public TextArea getNoticeLog() {
		return noticeLog;
	}

	public TextArea getNotice() {
		return notice;
	}

}
