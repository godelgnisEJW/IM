package view;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import message.ServerMsgParser;
import model.Client;
import model.Server;

public class LoginController implements Initializable{

    @FXML
    private JFXTextField count;

    @FXML
    private JFXPasswordField password;
    
    @FXML
    private JFXButton registerbt;

    @FXML
    private JFXButton loginbt;
	
	@FXML
    private Label setting;
	private Client client;
	private Server server;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	@FXML
	public void login() {
		String IMcount = count.getText().trim();
		String IMpassword = password.getText().trim();
		if (!IMcount.equals("") && !IMpassword.equals("")) {
			server = Server.getInstance(0, 25, "127.0.0.1");
			new Thread(server).start();
			int listenPort = server.getPort();
			client = new Client(IMcount, IMpassword, listenPort);
			new Thread(client).start();
		}
	}
	@FXML
	public void over() {
		setting.setStyle("-fx-background-color:#5264AE");
	}
	@FXML
	public void exit() {
		setting.setStyle("");
	}
	
}
