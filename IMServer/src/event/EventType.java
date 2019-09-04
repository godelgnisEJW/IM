package event;

public enum EventType {
	NEW_CONNECTION(1),
	CLOSE_CONNECTION(2),
	SERVER_ERROR(3),
	SERVER_START(4),
	SERVER_CLOSE(5),
	NEW_MESSAGE(6),
	SYSTEM_START(8),
	SYSTEM_EXIT(9);
	
	private int value;
	private EventType(int value) {
		this.value = value;
		
	}
	public int getValue() {
		return this.value;
	}
}
