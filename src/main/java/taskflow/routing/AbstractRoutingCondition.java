package taskflow.routing;

import taskflow.task.TaskRoutingWrap;

/**
 * RoutingCondition抽象实现类，反向代理TaskRoutingWrap的调用。
 * Created by lizhou on 2017/4/7/007.
 */
public abstract class AbstractRoutingCondition implements RoutingCondition {
	protected String condition;
    private TaskRoutingWrap taskRoutingWrap;

	public TaskRoutingWrap getTaskRoutingWrap() {
		return taskRoutingWrap;
	}

	public void setTaskRoutingWrap(TaskRoutingWrap taskRoutingWrap) {
		this.taskRoutingWrap = taskRoutingWrap;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
}
