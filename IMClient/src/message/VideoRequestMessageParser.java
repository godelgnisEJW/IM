package message;

import javafx.application.Platform;
import javafx.stage.Stage;
import model.User;
import model.UserCache;
import view.TipView;
import view.VideoPane;

public class VideoRequestMessageParser extends MessageParser {

	private static VideoRequestMessageParser parser = new VideoRequestMessageParser();
	
	private VideoRequestMessageParser() {
		super(8);
	}

	public static VideoRequestMessageParser getParser() {
		return parser;
	}
	
	@Override
	public void parse(Message msg, Object arg) {
		System.out.println("接收了");
		String markString = new String(msg.getContent());
		System.out.println(markString);
		if (markString.equals("refuse")) {
			VideoPane videoPane = VideoPane.getVideoPane();
			if (videoPane != null) {
				Platform.runLater(()->{
//					videoPane.close();
					Stage stage = videoPane.getStage();
					TipView<?> tipView = new TipView<>("用户拒绝视频通话", stage);
					tipView.setOnConfirmCall(()->{
						videoPane.close();
						return null;
					});
					tipView.setOnCancleCall(()->{
						videoPane.close();
						return null;
					});
					tipView.show();
				});
			}
		}else if (markString.equals("accept")){
			VideoPane videoPane = VideoPane.getVideoPane();
			if (videoPane != null) {
				videoPane.sendVideoData();
			}
		}else if(markString.equals("cancle")){
			VideoPane videoPane = VideoPane.getVideoPane();
			if (videoPane != null) {
				Platform.runLater(()->{
//					videoPane.close();
					Stage stage = videoPane.getStage();
					TipView<?> tipView = new TipView<>("用户断开视频连接", stage);
					tipView.setOnConfirmCall(()->{
						videoPane.close();
						return null;
					});
					tipView.setOnCancleCall(()->{
						videoPane.close();
						return null;
					});
					tipView.show();
				});
			}
		}else {
			Platform.runLater(()->{
				User user = UserCache.getUserBy(markString);
				System.out.println(user.info());
				VideoPane videoPane = VideoPane.createVideoPaneBy(user, false);
				System.out.println(videoPane);
				if (videoPane != null) {
					videoPane.show();
				}
			});
		}
	}

}
