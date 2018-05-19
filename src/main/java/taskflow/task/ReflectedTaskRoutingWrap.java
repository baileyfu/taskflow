package taskflow.task;

import taskflow.exception.TaskFlowException;
import taskflow.work.Work;

/**
 * 反射调用task的方法<br/>
 * 适用于需自定义Task方法的情况
 * 
 * @author bailey.fu
 * @date 2018年5月16日
 * @version 1.0
 * @description
 */
public class ReflectedTaskRoutingWrap extends AbstractTaskRoutingWrap {
	private TaskMethodInvoker taskMethodInvoker;

	@Override
	public void invokeTaskMethod(Work work) throws Exception {
		if (taskMethodInvoker != null) {
			taskMethodInvoker.invokeTask(work);
		} else {
			throw new TaskFlowException("TaskMethodInvoker is null,No task be executed !");
		}
	}

	public TaskMethodInvoker getTaskMethodInvoker() {
		return taskMethodInvoker;
	}

	public void setTaskMethodInvoker(TaskMethodInvoker taskMethodInvoker) {
		this.taskMethodInvoker = taskMethodInvoker;
	}
}
