package taskflow.config;

/**
 * xsd自定义的标签
 * 
 * @author bailey.fu
 * @date 2018年4月26日
 * @version 1.0
 * @description
 */
public enum Tag {
	WORKER_FACTORY("workerFactory"),
	WORKER("worker"),
	TASK("task"),
	ROUTING("routing");
	public String VALUE;
	private Tag(String VALUE) {
		this.VALUE = VALUE;
	}

	public String getTagName() {
		return "tf:" + VALUE;
	}
}
