package event;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class EventDispatch implements Observer {
	
	private final static EventDispatch dispatch = new EventDispatch();
	private Set<EvenCustomer> customers = new HashSet<>();
	
	private EventDispatch() {}

	public static EventDispatch getDispatch() {
		return dispatch;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event)o;
		for (EvenCustomer e : customers) {
			for (EventCustomeType t : e.getEventCustomeType()) {
				if (t.getValue() == event.getType().getValue()) {
					e.exec(event);
				}
			}
		}

	}
	public void resgisterCustomer(EvenCustomer customer) {
		customers.add(customer);
	}
}
