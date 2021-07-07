package taskflow.work;

import taskflow.task.TaskRoutingWrap;
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
	public WorkContext run() {
		try {
			start.doTask(this);
		} catch (Exception e) {
			dealExcpetion(e);
		} finally {
			if (finish != null) {
				finish.doTask(this);
			}
		}
		return workContext;
	}

	public void setStart(TaskRoutingWrap start) {
		this.start = start;
	}

	public void setFinish(TaskRoutingWrap finish) {
		this.finish = finish;
	}
	
	public void setRoutingKey(String key) {
		workContext.setRoutingKey(key);
	}
}
