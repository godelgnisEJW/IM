package view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;

import application.App;
import event.Event;
import event.EventType;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import message.ServerMsgParser;
import model.SessionCache;
import model.User;

public class SessionManagerController implements Initializable{

    @FXML
    private SplitPane splitPane;
    
    @FXML
    private JFXListView<User> onlineUserList;

    @FXML
    private Label nickNameLabel;

    @FXML
    private Label msgNumLabel;

    @FXML
    private Label onlineNumLabel;

//    @FXML
//    private AnchorPane sessionPane;
    
    private AnchorPane managerPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ServerMsgParser.getParser().setSessionManagerController(this);
	}
    
    public void showManagerPane(String nickName) {
    	new Event(this, EventType.SYSTEM_START);
    	this.nickNameLabel.setText(nickName);
    	Stage smStage = new Stage();
    	smStage.setScene(new Scene(managerPane));
    	smStage.setTitle("IMClient");
    	smStage.show();
    	smStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            	new Event(this, EventType.SYSTEM_EXIT);
            }
        });
    	App.setStage(smStage);
    }
    
    @FXML
    void click(MouseEvent event) {
    	User user = onlineUserList.getSelectionModel().selectedItemProperty().get();
    	if (user!=null) {
    		Platform.runLater(()->{
    			if (splitPane.getItems().size() > 1) {
    				splitPane.getItems().remove(1);
				}
    			splitPane.getItems().add(SessionCache.getSessionControllerBy(user).getSessionPane());
    		});
		}
    }
    public void setManagerPane(AnchorPane pane) {
    	this.managerPane = pane;
    }

	public JFXListView<User> getOnlineUserList() {
		return onlineUserList;
	}
	
	public void setOnlineNum(int num) {
		onlineNumLabel.setText("" + num);
	}
}
