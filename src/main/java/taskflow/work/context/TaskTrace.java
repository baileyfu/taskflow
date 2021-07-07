package taskflow.work.context;

/**
 * 记录task的运行路径，方便打印日志，了解业务调用流程和快照
 */
public class TaskTrace {
	private String taskName;
	private String contextInfo;

	public TaskTrace(String taskName, String contextInfo) {
		this.taskName = taskName;
		this.contextInfo = contextInfo;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getContextInfo() {
		return contextInfo;
	}

	public void setContextInfo(String contextInfo) {
		this.contextInfo = contextInfo;
	}

	@Override
	public String toString() {
		return "TaskTrace [taskName=" + taskName + ", contextInfo=" + contextInfo + "]";
	}
}
