package handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import event.EvenCustomer;
import event.Event;
import event.EventCustomeType;
import javafx.collections.ObservableList;
import model.User;
import view.DataManagementController;

public class SystemExitHandler extends EvenCustomer {
	
	private static SystemExitHandler handler = new SystemExitHandler();

	private SystemExitHandler() {
		super(EventCustomeType.SYSTEM_EXIT);
	}
	
	public static SystemExitHandler getHandler() {
		return handler;
	}

	/**
	 * 保存用户数据
	 */
	@Override
	public void exec(Event event) {
		File file = new File("user.db");
		if (file.exists()) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				ObservableList<User> data = DataManagementController.getData();
				for (User user : data) {
					oos.writeObject(user);
				}
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        System.exit(0);
	}

}
