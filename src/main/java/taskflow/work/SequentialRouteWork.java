package taskflow.work;

import java.util.LinkedHashMap;
import java.util.Map;

import taskflow.task.AbstractTaskRoutingWrap;
import taskflow.task.TaskRoutingWrap;
import taskflow.work.context.WorkContext;

/**
 * 串行执行任务,由SerialWork指定任务路由顺序<br/>
 * 扩展时,构造器参数中extraArgsMap必须放到最后
 * 
 * @author bailey.fu
 * @date 2018年5月17日
 * @version 1.0
 * @description
 */
public class SequentialRouteWork extends AbstractWork {
	private boolean executed=false;
	private LinkedHashMap<String, TaskRoutingWrap> tasks;

	public SequentialRouteWork() {
	}
	public SequentialRouteWork(Map<String,String> extraArgsMap) {
		super(extraArgsMap);
	}
	public void appendTask(TaskRoutingWrap task) {
		if (task != null)
			tasks.put(task.getName(), task);
	}
	@Override
	public WorkContext run() {
		if(!executed) {
			executed=true;
			try {
				if (tasks != null && tasks.size() > 0) {
					tasks.values().stream().forEach((task) -> {
						AbstractTaskRoutingWrap absTask = (AbstractTaskRoutingWrap) task;
						absTask.setRouting(null);
						task.doTask(this);
					});
				}
			} catch (Exception e) {
				dealExcpetion(e);
			}
		}
		return workContext;
	}
	public void setTasks(LinkedHashMap<String, TaskRoutingWrap> tasks) {
		this.tasks = tasks;
	}
}
