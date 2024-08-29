package rs.baselib.collection;

public class SyncException extends Exception {
	private static final long serialVersionUID = 1L;
	private Object value;
	public SyncException(String message, Throwable cause) {
		this(null, message, cause);
	}
	public SyncException(String message) {
		this(null, message, null);
	}
	public SyncException(Throwable cause) {
		this(null, null, cause);
	}
	public SyncException(Object value, String message) {
		this(value, message, null);
	}

	public SyncException(Object value, Throwable cause) {
		this(value, null, cause);
	}		
	public SyncException(Object value, String message, Throwable cause) {
		super(message, cause);
		this.value = value;
	}
	public Object getValue() {
		return value;
	}
}