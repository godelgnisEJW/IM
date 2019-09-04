package handler;

import java.io.File;

import event.EvenCustomer;
import event.Event;
import event.EventCustomeType;

public class SystemExitHandler extends EvenCustomer {
	
	private static SystemExitHandler handler = new SystemExitHandler();

	private SystemExitHandler() {
		super(EventCustomeType.SYSTEM_EXIT);
	}
	
	public static SystemExitHandler getHandler() {
		return handler;
	}

	@Override
	public void exec(Event event) {
		File directory = new File("tmp_img");
		if (directory.exists()) {
			File[] files = directory.listFiles();
			System.out.println("清除缓存......");
			for (File file : files) {
				System.out.println("正在删除图片" + file.getName());
				if (file.exists()) {
					file.delete();
				}
			}
			directory.delete();
			System.out.println("删除临时文件夹tmp_img");
		}
        System.exit(0);
	}

}
