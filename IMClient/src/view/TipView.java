package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TipView<V> extends AnchorPane implements Initializable{
	
	@FXML
	private AnchorPane tipPane;
	
	@FXML
	private Label tipLabel;
	
	private Stage stage = new Stage();
	
	private Stage primaryStage;
	
	private Callable<V> confirmCall;
	
	private Callable<V> cancleCall;
	
	public TipView() {
		this("", null);
	}
	
	public TipView(String tip, Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TipView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tipLabel.setText(tip);
		this.primaryStage = primaryStage;
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void show() {
		stage.setScene(new Scene(tipPane));
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);
		double X = primaryStage.getX() + primaryStage.getWidth()/ 4;
		double Y = primaryStage.getY() + primaryStage.getHeight() / 4;
		stage.setX(X);
		stage.setY(Y);
		stage.show();
	}
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	public void setTip(String tip) {
		tipLabel.setText(tip);
	}
	@FXML
	void confirm(ActionEvent event) {
		stage.close();
		if (confirmCall != null) {
			try {
				confirmCall.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@FXML
    void cancle(ActionEvent event) {
		stage.close();
		if (cancleCall != null) {
			try {
				cancleCall.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	public void setOnConfirmCall(Callable<V> call) {
		this.confirmCall = call;
	}
	public void setOnCancleCall(Callable<V> call) {
		this.cancleCall = call;
	}
}
