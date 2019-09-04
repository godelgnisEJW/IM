package event;

import java.util.Observable;

public class Event extends Observable{
	//事件源
	private Object source;
	//事件类型
	private EventType type;
	
	public Event(Object source,EventType type) {
		this.source = source;
		this.type = type;
		notifyEventDispatch();
	}

	public Object getSource() {
		return source;
	}

	public EventType getType() {
		return type;
	}
	
	private void notifyEventDispatch() {
		super.addObserver(EventDispatch.getDispatch());
		super.setChanged();
		super.notifyObservers(source);
	}
}
