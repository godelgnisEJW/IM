package emojione.fx;/**
 * Created by UltimateZero on 9/12/2016.
 */

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class EmojiConversion {

	private final static EmojiConversion EMOJI_CONVERSION = new EmojiConversion();
	
	private VBox conversionPane;
	
	private EmojiConversionController controller;
	
	private EmojiConversion() {
		try {
			FXMLLoader loader = new FXMLLoader(EmojiConversion.class.getResource("EmojiConversion.fxml"));
			conversionPane = loader.load();
			controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static EmojiConversion getEmojiConversion() {
		return EMOJI_CONVERSION;
	}
	
	public VBox getConversionPane() {
		return conversionPane;
	}
	
	public EmojiConversionController getController() {
		return controller;
	}
}
