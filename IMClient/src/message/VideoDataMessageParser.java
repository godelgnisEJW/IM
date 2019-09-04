package message;

import view.VideoPane;

public class VideoDataMessageParser extends MessageParser {

	private static VideoDataMessageParser parser = new VideoDataMessageParser();
	
	private VideoDataMessageParser() {
		super(9);
	}
	
	public static VideoDataMessageParser getParser() {
		return parser;
	}

	@Override
	public void parse(Message msg, Object arg) {
		VideoPane videoPane = VideoPane.getVideoPane();
		if (videoPane != null) {
			videoPane.updateShowingView(msg.getContent());
		}
	}
}
