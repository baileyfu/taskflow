package taskflow.task;

import taskflow.work.Work;

/**
 * TaskRoutingWrap的默认实现<br/>
 * 只会调用Task接口的方法来执行任务,适用于仅需执行Task.execute方法的情况
 * 
 * @author bailey.fu
 * @date 2018年5月17日
 * @version 1.0
 * @description
 */
public class DefaultTaskRoutingWrap extends AbstractTaskRoutingWrap {
	private Task task;
	public void setTask(Task task) {
		this.task = task;
	}
	@Override
	public Task getTask() {
		return task;
	}
	@Override
	public void invokeTaskMethod(Work work) throws Exception {
		task.execute(work);
	}
}
