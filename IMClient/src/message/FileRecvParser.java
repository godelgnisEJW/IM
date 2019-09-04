package message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import util.Converter;

public class FileRecvParser extends MessageParser{

	private static FileRecvParser parser = new FileRecvParser();
	private FileRecvParser() {
		super(7);
	}

	public static FileRecvParser getParser() {
		return parser;
	}
	
	@Override
	public void parse(Message msg, Object arg) {
		byte[] allBytes = msg.getContent();
		toSaveFile(allBytes);
	}
	
	public void toSaveFile(byte[] allBytes) {
		//读取文件名长度
		byte[] sfnLen = new byte[4];
		System.arraycopy(allBytes, 0, sfnLen, 0, sfnLen.length);
		int len = Converter.changeToInt(sfnLen);
		//按字节长度读取文件名
		byte[] sfnBytes = new byte[len];
		System.arraycopy(allBytes, 4, sfnBytes, 0, sfnBytes.length);
		//剩余的数据为文件字节数据
		byte[] fileBytes = new byte[allBytes.length - sfnBytes.length - 4];
		System.arraycopy(allBytes, 4 + sfnBytes.length, fileBytes, 0, fileBytes.length);
//		System.out.println("文件长度前缀长度:" + (4+sfnBytes.length) + "文件长度:" + fileBytes.length);

		//文件名
		String fileName = new String(sfnBytes);
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(fileBytes, 0, fileBytes.length);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
