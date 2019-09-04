package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
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
		msgPane.setPrefWidth(prefWidth);
		String now = TimeGenerator.getTime();
		time.setText(now);
		userName.setText(nickName);
		setMsg(content);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		parentWidthProperty.addListener(e->{
			msgPane.setPrefWidth(parentWidthProperty.get());
			this.setPrefWidth(parentWidthProperty.get() * 0.99);
			ObservableList<Node> list = msgOutput.getChildren();
			double size = 0;
			for (Node node : list) {
				if (node instanceof Text) {
					size += ((Text)node).getText().length() * 16;
				}else {
					size += 25;
				}
			}
			size += 25;
			double limit = parentWidthProperty.get() * 0.48;
			if (limit < 200) {
				limit = 200;
			}
			if (size > limit) {
				msgOutput.setPrefWidth(limit);
			}else {
				msgOutput.setPrefWidth(size);
			}
			AnchorPane.setLeftAnchor(this.time, null);
			AnchorPane.setRightAnchor(this.time, (parentWidthProperty.get() - 180)/2);
		});
		msgOutput.heightProperty().addListener(e->{
			msgPane.setPrefHeight(msgOutput.heightProperty().get() + 60);
		});
		msgOutput.setPadding(new Insets(10));
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
		Text textNode = new Text(msg);
		textNode.setFont(Font.font(16));
		textNode.setFill(Paint.valueOf("#ffffff"));
		msgOutput.getChildren().add(textNode);
	}
	
	public void setUserImg(String url) {
		if(!url.trim().equals("")) {
			Image image = new Image(url);
			this.userImg.setImage(image);
		}
	}
}
