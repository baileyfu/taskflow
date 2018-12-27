package taskflow.exception;

/**
 * 初始化的时候排除异常
 * Created by lizhou on 2017/3/14/014.
 */
public class TaskFlowException extends RuntimeException {
	private static final long serialVersionUID = -5481911907487746989L;

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
