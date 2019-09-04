package handler;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import event.EvenCustomer;
import event.Event;
import event.EventCustomeType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import view.DataManagementController;

public class SystemStartHandler extends EvenCustomer {

	private static SystemStartHandler handler = new SystemStartHandler();
	
	private ObservableList<User> data = FXCollections.observableArrayList();
	
	public SystemStartHandler() {
		super(EventCustomeType.SYSTEM_START);
	}

	public static SystemStartHandler getHandler() {
		return handler;
	}
	/**
	 * 保存用户数据
	 */
	@Override
	public void exec(Event event) {
		
		File file = new File("user.db");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("无法创建数据存储文件：user.db");
			}
		}
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			User user = null;
			while((user = (User)ois.readObject()) != null) {
				data.add(user);
			}
		} catch (EOFException e) {
			// 忽略
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataManagementController.setData(data);
	}

}
