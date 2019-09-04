package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TT {

	public static void main(String[] args) {
		int num = 0;
		File file = new File("F:\\种子，你懂的");
		File[] list  =  file.listFiles();
		int i = list.length;
		for (File f : list) {
			String name = f.getAbsolutePath();
			System.out.println(name);
			name = name.replaceAll("_urlgot.com_", "");
			System.out.println(name);
			f.renameTo(new File(name));
		}
	}

}
