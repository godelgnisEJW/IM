package event;

import java.util.HashSet;
import java.util.Set;

public abstract class EvenCustomer {

	private Set<EventCustomeType> customeType = new HashSet<>();
	public EvenCustomer(EventCustomeType type) {
		addCustomType(type);
	}
	
	public void addCustomType(EventCustomeType type) {
		customeType.add(type);
	}
	
	public Set<EventCustomeType> getEventCustomeType(){
		return customeType;
	}
	
	public abstract void exec(Event event);
}
