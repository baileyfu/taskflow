package taskflow.enums;

public enum TagAttribute {
	TASK_METHOD("method"),
	TASK_EXTRA("extra"),
	TASK_ASYNC("async"),
	
	TASK_ROUTING("routing"),
	TASK_ROUTING_KEY("key"),
	TASK_ROUTING_TO_TASK("toTask"),
	TASK_ROUTING_TO_WORK("toWork"),
	TASK_ROUTING_PATTERN("pattern"),
	TASK_ROUTING_EXTRA("extra"),
	
	WORK_START("start"),
	WORK_STARTWORK("startWork"),
	WORK_FINISH("finish"),
	WORK_FINISHWORK("finishWork"),
	WORK_TRACEABLE("traceable"),
	WORK_MAX_TASKS("maxTasks");
	public final String NAME;

	private TagAttribute(String NAME) {
		this.NAME = NAME;
	}
}
