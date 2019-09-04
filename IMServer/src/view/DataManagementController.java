package view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.IntegerTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import event.Event;
import event.EventType;
import handler.ConnectHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import message.LoginMessageParser;
import message.TextMessageParser;
import model.User;

public class DataManagementController implements Initializable{
	@FXML
    private JFXTextField searchField;

    @FXML
    private JFXTreeTableView<User> treeTableView;
    
    private static ObservableList<User> data = FXCollections.observableArrayList();

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		JFXTreeTableColumn<User, String> countCol = new JFXTreeTableColumn<>("账号");
		countCol.setStyle("-fx-alignment:center");
		setupCellValueFactory(countCol, User::countProperty);
		countCol.setResizable(true);
		countCol.setPrefWidth(200);
		countCol.setCellFactory((TreeTableColumn<User, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder());
        });
		countCol.setOnEditCommit((CellEditEvent<User, String> t) -> {
            t.getTreeTableView()
            .getTreeItem(t.getTreeTablePosition().getRow())
            .getValue().countProperty().set(t.getNewValue());
        });
		
		JFXTreeTableColumn<User, String> passwordCol = new JFXTreeTableColumn<>("密码");
		passwordCol.setStyle("-fx-alignment:center");
		setupCellValueFactory(passwordCol, User::passwordProperty);
		passwordCol.setResizable(true);
		passwordCol.setPrefWidth(200);
		passwordCol.setCellFactory((TreeTableColumn<User, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder());
        });
		passwordCol.setOnEditCommit((CellEditEvent<User, String> t) -> {
            t.getTreeTableView()
            .getTreeItem(t.getTreeTablePosition().getRow())
            .getValue().passwordProperty().set(t.getNewValue());
        });
		
		JFXTreeTableColumn<User, String> nickNameCol = new JFXTreeTableColumn<>("昵称");
		nickNameCol.setStyle("-fx-alignment:center");
		setupCellValueFactory(nickNameCol, User::nickNameProperty);
		nickNameCol.setResizable(true);
		nickNameCol.setPrefWidth(200);
		nickNameCol.setCellFactory((TreeTableColumn<User, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder());
        });
		nickNameCol.setOnEditCommit((CellEditEvent<User, String> t) -> {
            t.getTreeTableView()
            .getTreeItem(t.getTreeTablePosition().getRow())
            .getValue().nickNameProperty().set(t.getNewValue());
        });
		
		JFXTreeTableColumn<User, Number> ageCol = new JFXTreeTableColumn<>("年龄");
		ageCol.setStyle("-fx-alignment:center");
		setupCellValueFactory(ageCol, User::ageProperty);
		ageCol.setResizable(true);
		ageCol.setPrefWidth(100);
		ageCol.setCellFactory((TreeTableColumn<User, Number> param) -> {
            return new GenericEditableTreeTableCell<>(
                new IntegerTextFieldEditorBuilder());
        });
        ageCol.setOnEditCommit((CellEditEvent<User, Number> t) -> {
            t.getTreeTableView()
             .getTreeItem(t.getTreeTablePosition().getRow())
             .getValue().ageProperty().set((Integer)(t.getNewValue()));
        });
		
		JFXTreeTableColumn<User, String> sexCol = new JFXTreeTableColumn<>("性别");
		sexCol.setStyle("-fx-alignment:center");
		setupCellValueFactory(sexCol, User::sexProperty);
		sexCol.setResizable(true);
		sexCol.setPrefWidth(100);
		sexCol.setCellFactory((TreeTableColumn<User, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder());
        });
		sexCol.setOnEditCommit((CellEditEvent<User, String> t) -> {
            t.getTreeTableView()
            .getTreeItem(t.getTreeTablePosition().getRow())
            .getValue().sexProperty().set(t.getNewValue());
        });
		
		final TreeItem<User> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
    	treeTableView.setRoot(root);
    	treeTableView.setShowRoot(false);
    	treeTableView.setEditable(true);
    	treeTableView.getColumns().setAll(countCol,passwordCol,nickNameCol,ageCol,sexCol);
		
    	searchField.textProperty().addListener(setupSearchField(treeTableView));
    	
    	LoginMessageParser parser = LoginMessageParser.getParser();
    	parser.setDataManagementController(this);
    	
    	ConnectHandler cHandler = ConnectHandler.getHandler();
		cHandler.setDataManagementController(this);
		
		TextMessageParser.getParser().setDmController(this);
	}
	
	private <T> void setupCellValueFactory(JFXTreeTableColumn<User, T> column, Function<User, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

	
	private ChangeListener<String> setupSearchField(final JFXTreeTableView<User> tableView) {
        return (o, oldVal, newVal) ->
            tableView.setPredicate(userProp -> {
                final User user = userProp.getValue();
                return user.getCount().contains(newVal)
                    || user.getPassword().contains(newVal)
                    || Integer.toString(user.getAge()).contains(newVal)
                    || user.getNickName().contains(newVal)
                    || user.getSex().contains(newVal);
            });
    }

	public static ObservableList<User> getData() {
		return data;
	}

	public static void setData(ObservableList<User> listData) {
		data.clear();
		for (User user : listData) {
			data.add(user);
		}
	}
	
	
	
	public boolean check(String count, String password) {
		for (User user : data) {
			if(user.getCount().equals(count) && user.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}
	
	public String getNickName(String count, String password) {
		for (User user : data) {
			if(user.getCount().equals(count) && user.getPassword().equals(password)) {
				return user.getNickName();
			}
		}
		return null;
	}
	
	public User getUser(String count, String password) {
		for (User user : data) {
			if(user.getCount().equals(count) && user.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}
	
	public User getUser(String ip, int port) {
		for (User user : data) {
			if(user.getIp().equals(ip) && user.getPort() == port) {
				return user;
			}
		}
		return null;
	}
	public User getUserBy(String nickName) {
		for (User user : data) {
			if(user.getNickName().equals(nickName)) {
				return user;
			}
		}
		return null;
	}
	/**
	 * key的格式为	ip:port
	 * @param key
	 * @return
	 */
	public User getUser(String key) {
		String[] token  = key.split("(:|;)");
		return getUser(token[0], Integer.parseInt(token[1]));
	}
}
