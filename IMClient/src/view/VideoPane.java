package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

import application.App;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import message.Message;
import model.Connector;
import model.User;

public class VideoPane extends AnchorPane implements Initializable{
    
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private ImageView videoShowingView;
    
    @FXML
    private ImageView cancelView;

    @FXML
    private ImageView refuseView;
    
    @FXML
    private ImageView acceptView;
    
	private Webcam webcam;
	
	private Stage stage = new Stage();
	
	private User user;
	
	private static VideoPane videoPane;
	
	private static AtomicBoolean isCalling = new AtomicBoolean(false);
	
	private AtomicBoolean start = new AtomicBoolean(true);
	
	private boolean active = true;
	
	private static int num = 0;
	
	private Thread sendDataThread;
	
	private Runnable task;
	
	private VideoPane(boolean active) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/VideoPane.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
			this.active = active;
			
			cancelView.setVisible(active);
			refuseView.setVisible(!active);
			acceptView.setVisible(!active);
			if (active) {
				webcam = Webcam.getDefault();
				webcam.setViewSize(WebcamResolution.VGA.getSize());
				WebcamPanel panel = new WebcamPanel(webcam);
				panel.setFPSDisplayed(true);
				panel.setDisplayDebugInfo(true);
				panel.setImageSizeDisplayed(true);
				panel.setMirrored(true);
				
				SwingNode webcamNode = new SwingNode();
				webcamNode.setContent(panel);
				rootPane.getChildren().add(1, webcamNode);
				AnchorPane.setBottomAnchor(webcamNode, 0.0);
				AnchorPane.setLeftAnchor(webcamNode, 0.0);
				AnchorPane.setRightAnchor(webcamNode, 0.0);
				AnchorPane.setTopAnchor(webcamNode, 0.0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendVideoData() {
		System.out.println("判断是否发送视频数据");
		if (webcam != null) {
			System.out.println("发送视频数据");
			task = new captureTask();
			sendDataThread = new Thread(task);
			sendDataThread.start();
			
		}
	}
	
	public static VideoPane createVideoPaneBy(User user, boolean active) {
		if (active) {
			if (!isCalling.get()) {
				isCalling.compareAndSet(false, true);
				videoPane = new VideoPane(active);
				videoPane.setUser(user);
				return videoPane;
			}else {
				return null;
			}
		}else {
			isCalling.compareAndSet(false, true);
			videoPane = new VideoPane(active);
			videoPane.setUser(user);
			return videoPane;
		}
	}
	
	public static VideoPane getVideoPane() {
		return videoPane;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}
	
	public void show() {
		
		stage.setScene(new Scene(videoPane));
		stage.setTitle("与" + user.getNickName() + "视频通话中.....");
		
		double X = App.getStage().getX() + App.getStage().getWidth()/ 4;
		double Y = App.getStage().getY() + App.getStage().getHeight() / 4;
		stage.setX(X);
		stage.setY(Y);
		
		stage.show();
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            	if (webcam != null) {
            		webcam.close();
				}
                isCalling.compareAndSet(true, false);
                start.compareAndSet(true, false);
                setUser(null);
            }
        });
		try {
			if (active) {
				message.Message.send(Connector.getHandlerBy(user).getOutputStream(), 8, App.getAppUser().getNickName().getBytes());
			}
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cancelView.setOnMouseClicked(e->{
			message.Message.send(Connector.getHandlerBy(user).getOutputStream(), 8, "cancle".getBytes());
			if (sendDataThread != null) {
				sendDataThread.interrupt();
				sendDataThread = null;
			}
			close();
		});
		refuseView.setOnMouseClicked(e->{
			message.Message.send(Connector.getHandlerBy(user).getOutputStream(), 8, "refuse".getBytes());
			close();
		});
		acceptView.setOnMouseClicked(e->{
			message.Message.send(Connector.getHandlerBy(user).getOutputStream(), 8, "accept".getBytes());
			Platform.runLater(()->{
				acceptView.setVisible(false);
				refuseView.setVisible(false);
				cancelView.setVisible(true);
			});
		});
		stage.widthProperty().addListener(e->{
			AnchorPane.setLeftAnchor(cancelView, (stage.widthProperty().get() - cancelView.getFitWidth()) / 2);
			AnchorPane.setRightAnchor(acceptView, (stage.widthProperty().get() - acceptView.getFitWidth()) / 4);
			AnchorPane.setLeftAnchor(refuseView, (stage.widthProperty().get() - refuseView.getFitWidth()) / 4);
		});
	}
	
	
	public void close() {
		if (webcam != null) {
			webcam.close();
		}
		stage.close();
		isCalling.compareAndSet(true, false);
		start.compareAndSet(true, false);
		setUser(null);
	}
	public Stage getStage() {
		return stage;
	}
	public void updateShowingView(byte[] data) {
		Platform.runLater(()->{
			try {
				System.out.println("更新数据，数组长度：" + data.length);
				File tmpFile = new File("tmp_img\\" + (num++) + ".png");
				System.out.println(tmpFile.getAbsolutePath());
				if (tmpFile.exists()) {
					tmpFile.delete();
				}
				tmpFile.createNewFile();
				FileOutputStream out = new FileOutputStream(tmpFile);
				out.write(data, 0, data.length);
				FileInputStream in =new FileInputStream(tmpFile);
				Image image = new Image(in);
				videoShowingView.setImage(image);
				File deleteTmpFile = new File("tmp_img\\" + (num-1) + ".png");
				if (deleteTmpFile.exists()) {
					deleteTmpFile.delete();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	private class captureTask implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(start.get()) {
				if (webcam != null) {
					if (webcam.isOpen()) {
						String fileName = "tmp_img\\s_" + (num++);
						WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_PNG);
						try {
							Thread.sleep(100);
							byte[] buf = Files.readAllBytes(Paths.get(fileName + ".png"));
							System.out.println(buf.length);
							Message.send(Connector.getHandlerBy(user).getOutputStream(), 9, buf);
							String deleteFileName = "tmp_img\\s_" + (num - 1) + ".png";
							File file = new File(deleteFileName);
							if (file.exists()) {
								file.delete();
							}
						} catch (InterruptedException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
