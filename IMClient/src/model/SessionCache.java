package model;

import java.io.IOException;
import java.util.HashMap;

import application.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import view.SessionController;

public class SessionCache {
	private static HashMap<User, SessionController> cache = new HashMap<>();
	
	public static SessionController getSessionControllerBy(User user) {
		
		SessionController controller = null;
		if ((controller = cache.get(user)) != null) {
			return controller;
		}
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("../view/Session.fxml"));
			AnchorPane sessionPane = (AnchorPane)loader.load();
			controller = loader.getController();
			controller.setSessionPane(sessionPane);
			controller.setNickName(user.getNickName());
			controller.setUser(user);
			cache.put(user, controller);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return controller;
	}
}
