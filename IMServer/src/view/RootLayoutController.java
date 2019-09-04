package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class RootLayoutController implements Initializable{
	
    @FXML
    private Tab rmTab;
    
    @FXML
    private Tab omTab;

    @FXML
    private Tab dmTab;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void initRmTab(AnchorPane rmPane) {
		rmTab.setContent(rmPane);
	}
	
	public void initOmTab(AnchorPane omPane) {
		omTab.setContent(omPane);
	}

	public void initDmTab(AnchorPane dmPane) {
		dmTab.setContent(dmPane);
	}
}
