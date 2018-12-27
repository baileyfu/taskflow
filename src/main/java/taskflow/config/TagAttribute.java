package taskflow.config;

public enum TagAttribute {
	TASKS("tasks"),
	MAX_TASKS("maxTasks"),
	START("start"),
	FINISH("finish"),
	METHOD("method"),
	ROUTING_CONDITIONS("routingConditions"),
	ROUTING("routing"),
	EXTRA("extra");
	public final String NAME;

	private TagAttribute(String NAME) {
		this.NAME = NAME;
	}
}
