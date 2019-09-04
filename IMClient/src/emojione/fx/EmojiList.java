package emojione.fx;
/**
 * Created by UltimateZero on 9/12/2016.
 */

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class EmojiList {

	private final static EmojiList EMOJI_LIST = new EmojiList();
	private VBox emotionPane;
	private EmojiListController controller;
	
	private EmojiList() {
		try {
			FXMLLoader loader = new FXMLLoader(EmojiList.class.getResource("EmojiList.fxml"));
			emotionPane = loader.load();
			controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static EmojiList getEmojiList() {
		return EMOJI_LIST;
	}
	
	public EmojiListController getController() {
		return controller;
	}
	
	public VBox getEmojiPane() {
		return emotionPane;
	}
}
