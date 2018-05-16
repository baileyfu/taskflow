package taskflow.routing.impl;

import taskflow.routing.RoutingCondition;
import taskflow.task.TaskRoutingWrap;

/**
 * RoutingCondition抽象实现类，反向代理StationRoutingWrap的调用。
 * Created by lizhou on 2017/4/7/007.
 */
public abstract class AbstractRoutingCondition implements RoutingCondition {
    private TaskRoutingWrap taskRoutingWrap;

	public TaskRoutingWrap getTaskRoutingWrap() {
		return taskRoutingWrap;
	}

	public void setTaskRoutingWrap(TaskRoutingWrap taskRoutingWrap) {
		this.taskRoutingWrap = taskRoutingWrap;
	}
}
