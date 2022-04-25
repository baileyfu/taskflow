package taskflow.exception;

public class TaskFlowException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TaskFlowException() {
    }

    public TaskFlowException(String message) {
        super(message);
    }

    public TaskFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskFlowException(Throwable cause) {
        super(cause);
    }

}
