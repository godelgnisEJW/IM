package handler;

import java.io.File;

import event.EvenCustomer;
import event.Event;
import event.EventCustomeType;

public class SystemStartHandler extends EvenCustomer {

	private static SystemStartHandler handler = new SystemStartHandler();
	public SystemStartHandler() {
		super(EventCustomeType.SYSTEM_START);
	}

	public static SystemStartHandler getHandler() {
		return handler;
	}
	@Override
	public void exec(Event event) {
		System.out.println("创建临时文件夹tmp_img");
		File directory = new File("tmp_img");
    	if (!directory.exists()) {
			directory.mkdir();
		}
	}

}
