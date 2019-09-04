package view;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;

import event.EventDispatch;
import handler.ConnectHandler;
import handler.ServerStatusHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import message.LoginMessageParser;
import message.MessageDispatch;
import model.Server;

public class RunningManagementController implements Initializable{

	@FXML
    private TextArea serverLog;

    @FXML
    private Label status;

    @FXML
    private Label sercerIP;

    @FXML
    private JFXRadioButton tcpProtocol;

    @FXML
    private JFXRadioButton udpProtocol;

    @FXML
    private JFXSlider maxOnlineNum;

    @FXML
    private Label onlineNum;

    @FXML
    private Label serverPort;

    @FXML
    private JFXButton startButton;

    @FXML
    private JFXButton closeButton;
    
    private ToggleGroup group = new ToggleGroup();
    private Server server;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		startButton.disabledProperty().addListener(listener->{
			if (startButton.disabledProperty().get()) {
				closeButton.setDisable(false);
			}
		});
		closeButton.disabledProperty().addListener(listener->{
			if (closeButton.disabledProperty().get()) {
				startButton.setDisable(false);
			}
		});
		
		tcpProtocol.setToggleGroup(group);
		udpProtocol.setToggleGroup(group);
		ServerStatusHandler handler = ServerStatusHandler.getHandler();
		handler.setRunningManagementController(this);
		
		ConnectHandler cHandler = ConnectHandler.getHandler();
		cHandler.setRunningManagementController(this);
		
		LoginMessageParser parser = LoginMessageParser.getParser();
    	parser.setRunningManagementController(this);
	}

    @FXML
    void start(ActionEvent event) {
    	int port = Integer.parseInt(serverPort.getText());
    	int poolSize = maxOnlineNum.valueProperty().intValue();
    	String IP = sercerIP.getText();
    	server = Server.getInstance(port, poolSize, IP);
    	startButton.setDisable(true);
    	new Thread(server).start();
    }

    @FXML
    void close(ActionEvent event) {
    	server.close();
    	closeButton.setDisable(true);
    }

	public TextArea getServerLog() {
		return serverLog;
	}

	public Label getStatus() {
		return status;
	}

	public Label getOnlineNum() {
		return onlineNum;
	}
}
