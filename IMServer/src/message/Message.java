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
	
	public static void send(OutputStream out, int type, byte[] content) {
		try {
			byte[] _type = {(byte)type};
			out.write(_type);
			int length = content.length;
			byte[] _length = Converter.changeToBytes(length);
			out.write(_length);
			out.write(content);
			out.flush();
		} catch (IOException e) {
			//
		}
		
	}
}
