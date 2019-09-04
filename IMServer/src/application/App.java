package application;

import conf.Configration;
import event.Event;
import event.EventType;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.RootLayoutController;

public class App extends Application {
	private Stage primaryStage;
    private TabPane rootLayout;
    private FXMLLoader rootLoader;
    private Configration conf = new Configration(this);
    
	@Override
	public void start(Stage primaryStage) {
		try {
			rootLoader = new FXMLLoader();
			rootLoader.setLocation(App.class.getResource("../view/RootLayout.fxml"));
			rootLayout = (TabPane)rootLoader.load();
		}catch (Exception e) {
			//忽略
		}
		this.primaryStage = primaryStage;
		this.primaryStage.setScene(new Scene(rootLayout));
		conf.init();
		initRootLayout();
		primaryStage.setTitle("IMServer");
		primaryStage.show();
		new Event(this, EventType.SYSTEM_START);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                new Event(this, EventType.SYSTEM_EXIT);
            }
        });
	}

	public void initRootLayout() {
		try {
			RootLayoutController controller = rootLoader.getController();
			
			FXMLLoader rmLoader = new FXMLLoader();
			rmLoader.setLocation(App.class.getResource("../view/RunningManagement.fxml"));
			AnchorPane rmPane = (AnchorPane)rmLoader.load();
			controller.initRmTab(rmPane);
			
			FXMLLoader omLoader = new FXMLLoader();
			omLoader.setLocation(App.class.getResource("../view/onlineManagement.fxml"));
			AnchorPane omPane = (AnchorPane)omLoader.load();
			controller.initOmTab(omPane);
			
			FXMLLoader dmLoader = new FXMLLoader();
			dmLoader.setLocation(App.class.getResource("../view/DataManagement.fxml"));
			AnchorPane dmPane = (AnchorPane)dmLoader.load();
			controller.initDmTab(dmPane);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
