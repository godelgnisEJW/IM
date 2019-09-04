package view;

import java.io.IOException;
import java.net.URL;
import java.util.Queue;
import java.util.ResourceBundle;

import emojione.Emoji;
import emojione.EmojiOne;
import emojione.fx.EmojiConversion;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import util.TimeGenerator;

public class MsgView extends AnchorPane implements Initializable{
	
	private ReadOnlyDoubleProperty parentWidthProperty;
	//时间
	@FXML
    private Label time;
	//用户名
    @FXML
    private Label userName;
	//用户头像
	@FXML
    private ImageView userImg;
	//消息内容
    @FXML
    private TextFlow msgOutput;
    @FXML
    private AnchorPane msgPane;
	
    
    private double msgLegnth;
    final FXMLLoader loader;
    /**
     * 
     * @param parentWidthProperty
     * @param isConversioned	true表示是自己发出的信息,false表示是接收到的信息
     * @param nickName	用户名
     * @param content	消息内容
     */
    public MsgView(double prefWidth, ReadOnlyDoubleProperty parentWidthProperty, boolean isConversioned, String nickName, String content) {
    	this.parentWidthProperty = parentWidthProperty;
    	loader = new FXMLLoader(getClass().getResource("msgView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(isConversioned) {
			conversion();
		}
		String now = TimeGenerator.getTime();
		time.setText(now);
		userName.setText(nickName);
		setMsg(content);
		adjust(prefWidth);
		
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		parentWidthProperty.addListener(e->{
			this.setPrefWidth(parentWidthProperty.get() * 0.99);
			adjust(parentWidthProperty.get());
		});
		msgOutput.heightProperty().addListener(e->{
			Platform.runLater(()->{
				msgPane.setPrefHeight(msgOutput.heightProperty().get() + 60);
			});
		});
		msgOutput.setPadding(new Insets(10));
	}
	
	private void adjust(double width) {
		msgPane.setPrefWidth(width);
		
		double limit = width * 0.48;
		if (limit < 200) {
			limit = 200;
		}
		if (msgLegnth > limit) {
			msgOutput.setPrefWidth(limit);
		}else {
			msgOutput.setPrefWidth(msgLegnth);
		}
		AnchorPane.setLeftAnchor(this.time, null);
		AnchorPane.setRightAnchor(this.time, (width - 180)/2);
	}
	
	public void setParentWidthProperty(ReadOnlyDoubleProperty parentWidthProperty) {
		this.parentWidthProperty = parentWidthProperty;
	}
    
	public void conversion() {
		AnchorPane.setLeftAnchor(this.msgOutput, null);
		AnchorPane.setRightAnchor(this.msgOutput, 115.0);
		AnchorPane.setLeftAnchor(this.userImg, null);
		AnchorPane.setRightAnchor(this.userImg, 45.0);
		AnchorPane.setLeftAnchor(this.userName, null);
		AnchorPane.setRightAnchor(this.userName, 115.0);
		userName.setAlignment(Pos.CENTER_RIGHT);
	}
	
	public void setTime(String time) {
		this.time.setText(time);
	}
	
	public void setUserName(String userName) {
		this.userName.setText(userName);
	}
	
	public void setMsg(String msg) {
		
//		ObservableList<Node> list = msgOutput.getChildren();
//		for (Node node : list) {
//			if (node instanceof Text) {
//				Text textNode = (Text)node;
//				size += textNode.getLayoutBounds().getWidth();
//			}else {
//				size += 25;
//			}
//		}
		
		
		Queue<Object> obs = EmojiOne.getInstance().toEmojiAndText(msg);
		while(!obs.isEmpty()) {
			Object ob = obs.poll();
			if(ob instanceof String) {
				Text textNode = new Text((String)ob);
				textNode.setFont(Font.font(16));
				textNode.setFill(Paint.valueOf("#ffffff"));
				msgOutput.getChildren().add(textNode);
				msgLegnth += textNode.getLayoutBounds().getWidth();
			}
			else if(ob instanceof Emoji) {
				Emoji emoji = (Emoji) ob;
				Node emojiNode = EmojiConversion.getEmojiConversion().getController().createEmojiNode(emoji);
				msgOutput.getChildren().add(emojiNode);
				msgLegnth += 36;
			}
		}
		msgLegnth += 25;
	}
	
	public void setUserImg(String url) {
		if(!url.trim().equals("")) {
			Image image = new Image(url);
			this.userImg.setImage(image);
		}
	}
}
