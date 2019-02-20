package taskflow.enums;

public enum TagAttribute {
	TASK_METHOD("method"),
	
	TASK_ROUTING("routing"),
	TASK_ROUTING_TO("to"),
	TASK_ROUTING_PATTEN("patten"),
	
	WORK_START("start"),
	WORK_FINISH("finish"),
	WORK_RECORD("record"),
	WORK_MAX_TASKS("maxTasks"),
	EXTRA("extra");
	public final String NAME;

	private TagAttribute(String NAME) {
		this.NAME = NAME;
	}
}
