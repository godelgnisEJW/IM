package application;

import conf.Configration;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.User;
import view.SessionManagerController;

public class App extends Application {
	private static Stage primaryStage;
	private static User appUser;
    private AnchorPane rootLayout;
    private FXMLLoader rootLoader;
    private Configration conf = new Configration(this);
    
    public static Stage getStage() {
    	return primaryStage;
    }
    
    public static void setStage(Stage stage) {
    	App.primaryStage = stage;
    }
    
    public static void setAppUser(User user) {
    	appUser = user;
    }
    
    public static User getAppUser() {
    	return appUser;
    }
    
	@Override
	public void start(Stage primaryStage) {
		try {
			rootLoader = new FXMLLoader();
			rootLoader.setLocation(App.class.getResource("../view/LoginLayout.fxml"));
			rootLayout = (AnchorPane)rootLoader.load();
		}catch (Exception e) {
			//忽略
		}
		App.primaryStage = primaryStage;
		App.primaryStage.setScene(new Scene(rootLayout));
		conf.init();
		initRootLayout();
		primaryStage.setTitle("IMClient客户端");
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
	}

	public void initRootLayout() {
		try {
			FXMLLoader smLoader = new FXMLLoader();
			smLoader.setLocation(App.class.getResource("../view/SessionManager.fxml"));
			AnchorPane mPane = (AnchorPane)smLoader.load();
			SessionManagerController smController = (SessionManagerController)smLoader.getController();
			smController.setManagerPane(mPane);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}