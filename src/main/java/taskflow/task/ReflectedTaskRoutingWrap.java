package taskflow.task;

import taskflow.work.Work;

/**
 * 反射调用task的方法<br/>
 * 主要用于配置式work
 * 
 * @author bailey.fu
 * @date 2018年5月16日
 * @version 1.0
 * @description
 */
public class ReflectedTaskRoutingWrap extends AbstractStationRoutingWrap {
	private TaskMethodInvoker taskMethodInvoker;

	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void invokeTaskMethod(Work work) throws Exception {
		if (taskMethodInvoker != null && taskMethodInvoker.getTask() != null) {
			taskMethodInvoker.invokeTask(work);
		} else {
			throw new Exception("No task be done!");
		}
	}

	public TaskMethodInvoker getTaskMethodInvoker() {
		return taskMethodInvoker;
	}

	public void setTaskMethodInvoker(TaskMethodInvoker taskMethodInvoker) {
		this.taskMethodInvoker = taskMethodInvoker;
	}

	@Override
	public Task getTask() {
		return taskMethodInvoker != null ? taskMethodInvoker.getTask() : null;
	}
}
