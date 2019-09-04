package message;

import java.io.IOException;
import java.io.OutputStream;

import util.Converter;

public class Message {

	private int type;
	private byte[] content;
	
	public Message(int type, byte[] content) {
		this.type = type;
		this.content = content;
	}
	
	public int getType() {
		return type;
	}

	public byte[] getContent() {
		return content;
	}
	
	public static boolean send(OutputStream out, int type, byte[] content) {
		try {
			byte[] _type = {(byte)type};
			//写入1字节消息类型
			out.write(_type);
			int length = content.length;
			byte[] _length = Converter.changeToBytes(length);
			//写入4字节消息长度
			out.write(_length);
			//写入消息内容
			out.write(content);
			out.flush();
		} catch (IOException e) {
			//
		}
		return true;
	}
	
}
