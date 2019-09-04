package view;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;

import application.App;
import emojione.fx.EmojiList;
import emojione.fx.EmojiListController;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import message.Message;
import model.Connector;
import model.Server.ConnectionHandler;
import model.User;

public class SessionController implements Initializable{
	
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox recordArea;

    @FXML
    private Button sendFileButton;    
    
    @FXML
    private Button emotion;
    
    @FXML
    private Button videoButton;
    
	private boolean isEmojiPaneInit = false;

    @FXML
    private JFXTextArea msgArea;
    
    @FXML
    private Label nickName;
    
    private User user;
    
    private AnchorPane sessionPane;
    
    private ReadOnlyDoubleProperty parentWidthProperty;
    
    private static EmojiList emojiList;
    private VBox emojiPane;
    private Tooltip tooltip;
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	parentWidthProperty = scrollPane.widthProperty();
    	parentWidthProperty.addListener(e->{
    		scrollPane.setPrefWidth(parentWidthProperty.get());
    	});
    	Image btnImg = new Image("file:\\emoji.png");
        ImageView imageView = new ImageView(btnImg);
        //给按钮设置图标
        Platform.runLater(()->{
        	emotion.setGraphic(imageView);
        });
        emojiList = EmojiList.getEmojiList();
        recordArea.heightProperty().addListener(e->{
        	scrollPane.setVvalue(recordArea.heightProperty().get());
        });
        
        tooltip = new Tooltip("视频通话");
        Tooltip.install(videoButton, tooltip);
	}

	@FXML
    public void sendMessage(ActionEvent event) {
    	String msg = msgArea.getText();
    	String content = msg.trim();
    	if (!content.equals("")) {
    		ConnectionHandler handler =  Connector.getHandlerBy(user);
    		OutputStream out = handler.getOutputStream();
			Message.send(out, 3, (App.getAppUser().getNickName() + "#" + content).getBytes());
			updateRecordArea("我", content, true);
		}
    	msgArea.clear();
    }

    @FXML
    public void showEmojiPane(ActionEvent event) {
    	if (!isEmojiPaneInit) {
    		emojiPane = emojiList.getEmojiPane();
    		double buttom = 228.0;
    		double left = 22.0;
    		sessionPane.getChildren().add(emojiPane);
    		AnchorPane.setBottomAnchor(emojiPane, buttom);
    		AnchorPane.setLeftAnchor(emojiPane, left);
    		this.isEmojiPaneInit = true;
		}
    	emojiPane.setVisible(true);
    }
    
    public void closeEmojiPane() {
    	if (isEmojiPaneInit) {
    		emojiPane.setVisible(false);
    	}
    }
    
    @FXML
    public void videoCall(ActionEvent event) {
		VideoPane  videoPane = VideoPane.createVideoPaneBy(user,true);
		if (videoPane != null) {
			videoPane.show();
		}else {
			User user = VideoPane.getVideoPane().getUser();
			TipView<?> tipView = new TipView<>("摄像头已被占用，正在与 " + user.getNickName() + " 进行通话", App.getStage());
			tipView.show();
		}
    }
    
    @FXML
	public void initEmojiList() {
		EmojiListController controller = emojiList.getController();
		controller.setUser(user);
	}
    
    public void updateRecordArea(String nickName, String message, boolean isConversioned) {
    	if (!nickName.equals(App.getAppUser().getNickName())) {
    		Platform.runLater(()->{
    			MsgView msgView = new MsgView(scrollPane.getPrefWidth(), parentWidthProperty, isConversioned, nickName, message);
    			recordArea.getChildren().add(msgView);
    		});
		}
    }
    //貌似JavaFX支持不太好，去掉
    @FXML
    public void enterToSend(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
        	ConnectionHandler handler =  Connector.getHandlerBy(user);
        	OutputStream out = handler.getOutputStream();
        	String msg = msgArea.getText();
        	String content = msg.trim();
        	if (!content.equals("")) {
    			Message.send(out, 3, content.getBytes());
    			updateRecordArea("我", content, true);
    		}
        	msgArea.clear();
		}
    }
    
    @FXML
    public void showVbar(MouseEvent event) {
		Platform.runLater(()->{
			scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    	});
    }
    
    @FXML
    public void hideVbar(MouseEvent event) {
    	Platform.runLater(()->{
    		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
    	});
    }

    @FXML
    void sendFile(ActionEvent event) {
    	closeEmojiPane();
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Resource File");
    	File file = fileChooser.showOpenDialog(App.getStage());
    	if (file != null) {
    		TipView<?> tipView = new TipView<>("确定发送文件 " + file.getName() + "?", App.getStage());
    		tipView.setOnConfirmCall(()->{
    			String  fileName = App.getAppUser().getNickName() + "#" +file.getAbsolutePath();
    			ConnectionHandler handler = Connector.getHandlerBy(user);
    			Message.send(handler.getOutputStream(), 5, fileName.getBytes());
    			return null;
    		});
    		tipView.show();
		}
    }
    
    public void setUser(User user) {
    	this.user = user;
    	if (user.getNickName().equals("聊天大厅")) {
    		sendFileButton.setVisible(false);
    		videoButton.setVisible(false);
		}
    	tooltip.setText("与" + user.getNickName() + "视频通话");
    }
    
    public void setNickName(String name) {
    	nickName.setText(name);
    }

	public AnchorPane getSessionPane() {
		return sessionPane;
	}

	public void setSessionPane(AnchorPane sessionPane) {
		this.sessionPane = sessionPane;
	}

	public void addTextToMsgArea(String text) {
		int pos =msgArea.getCaretPosition();
		if (pos != msgArea.getText().length()) {
			String content= msgArea.getText();
			String start = content.substring(0, pos);
			String end = content.substring(pos);
			msgArea.setText(start + text + end); 
		}
		else {
			msgArea.appendText(text);
		}
	}
	
	public User getUser() {
		return user;
	}
}
