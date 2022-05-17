package taskflow.work;

import taskflow.task.routing.AbstractTaskRoutingWrap;
import taskflow.task.routing.TaskRoutingWrap;
import taskflow.work.context.WorkContext;

/**
 * 自定义路由的Work;由各TaskRoutingWrap指定路由顺序
 */
public class CustomRouteWork extends AbstractWork {
	// 初始任务;一般在此初始化一些参数
	private TaskRoutingWrap start;
	// 结束任务;无论是否正常完成,该任务都会执行;可再次封装返回的结果
	private TaskRoutingWrap finish;
	public CustomRouteWork() {
	}

	public WorkContext execute() {
		try {
			try {
				start.doTask(this);
			} finally {
				if (finish != null) {
					// finish的routing置空，不再执行路由逻辑
					((AbstractTaskRoutingWrap)finish).setRouting(null);
					finish.doTask(this);
				}
			}
		} catch (Exception e) {
			this.dealExcpetion(e);
		}
		return workContext;
	}

	public void setStart(TaskRoutingWrap start) {
		this.start = start;
	}

	public void setFinish(TaskRoutingWrap finish) {
		this.finish = finish;
	}
}
