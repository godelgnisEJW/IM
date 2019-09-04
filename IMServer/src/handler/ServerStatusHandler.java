package handler;

import event.EvenCustomer;
import event.Event;
import event.EventCustomeType;
import event.EventType;
import javafx.application.Platform;
import util.TimeGenerator;
import view.RunningManagementController;

public class ServerStatusHandler extends EvenCustomer{
	
	private RunningManagementController controller;
	
	private final static ServerStatusHandler handler = new ServerStatusHandler();

	private ServerStatusHandler() {
		super(EventCustomeType.SERVER_START);
		super.addCustomType(EventCustomeType.SERVER_CLOSE);
		super.addCustomType(EventCustomeType.SERVER_ERROR);
	}

	public static ServerStatusHandler getHandler() {
		return handler;
	}
	
	@Override
	public void exec(Event event) {
		String msg = (String)event.getSource();
		String time = TimeGenerator.getTime();
		Platform.runLater(()->{
			controller.getServerLog().appendText(time + "\n" + msg + "\n");
			if (event.getType().getValue() == EventType.SERVER_START.getValue()) {
				controller.getStatus().setText("启动");
			}else {
				controller.getStatus().setText("关闭");
			}
		});
	}
	
	public void setRunningManagementController(RunningManagementController controller) {
		this.controller = controller;
	}
	
}
