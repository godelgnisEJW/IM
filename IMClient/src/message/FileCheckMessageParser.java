package message;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import model.Server.ConnectionHandler;
import util.Converter;

public class FileCheckMessageParser extends MessageParser{

	private static FileCheckMessageParser parser = new FileCheckMessageParser();
	private FileCheckMessageParser() {
		super(6);
	}

	public static FileCheckMessageParser getParser() {
		
		return parser;
	}
	
	@Override
	public void parse(Message msg, Object arg) {
		String mString = new String(msg.getContent());
		//处理文件接收消息
		ConnectionHandler handler = (ConnectionHandler)arg;
		if(mString.startsWith("yes")) {
			String[] token = mString.split("#");
			String fileName = token[2];
			String saveFileName = token[3];
			toSendFileBytes(fileName, saveFileName, handler.getOutputStream());
		}
	}
	
	public void toSendFileBytes(String fileName, String saveFileName, OutputStream out) {
		try {
			//文件字节数据
			byte[] fileBytes = Files.readAllBytes(Paths.get(fileName));
			//接收方定义好的文件名的字节数组
			byte[] sfnBytes = saveFileName.getBytes();
			//文件名长度
			int len = sfnBytes.length;
			byte[] sfnLen = Converter.changeToBytes(len);
			//总数据字节数组
			byte[] allBytes = new byte[4 + sfnBytes.length + fileBytes.length ];
			//分别开始复制文件名长度、文件名、文件数据到allBytes中
			System.arraycopy(sfnLen, 0, allBytes, 0, sfnLen.length);
			System.arraycopy(sfnBytes, 0, allBytes, 4, sfnBytes.length);
			System.arraycopy(fileBytes, 0, allBytes, 4 + sfnBytes.length, fileBytes.length);
			//发送数据
			Message.send(out, 7, allBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
