package event;

/**
 * 原本close_conection是2的，修正消息的类型将2给了server_message
 * @author godlegnis
 *
 */
public enum EventType {
	NEW_CONNECTION(1),
	SERVER_MESSAGE(2),
	CLOSE_CONNECTION(7),
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
