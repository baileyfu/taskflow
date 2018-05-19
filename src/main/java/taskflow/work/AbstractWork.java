package taskflow.work;

import java.util.ArrayList;

import taskflow.exception.TaskFlowException;
import taskflow.task.TaskRoutingWrap;
import taskflow.work.context.MapWorkContext;
import taskflow.work.context.TaskTrace;
import taskflow.work.context.WorkContext;

public abstract class AbstractWork implements Work{
	protected int maxTasks;
	protected int executedTasks;
	protected WorkContext workContext;
	protected ArrayList<TaskTrace> taskRecords;
	public AbstractWork() {
		workContext = new MapWorkContext();
		taskRecords = new ArrayList<TaskTrace>();
	}
	/**
	 * 最好创建子类覆盖次方法以自定义异常处理
	 */
	public void dealExcpetion(Exception workException) {
		System.err.println(workException.getMessage());
	}

	/**
	 * 记录任务调用轨迹,并检查任务调用次数上限
	 */
	public void receive(TaskRoutingWrap stationRoutingWrap) throws Exception {
		if (maxTasks <= executedTasks++) {
			throw new TaskFlowException("max tasks is:" + maxTasks);
		}
		taskRecords.add(new TaskTrace(stationRoutingWrap.getName(), workContext.toString()));
	}
	public ArrayList<TaskTrace> getTaskTrace() {
		return taskRecords;
	}
	public WorkContext getWorkContext() {
		return workContext;
	}

	public int getMaxTasks() {
		return maxTasks;
	}

	public void setMaxTasks(int maxTasks) {
		this.maxTasks = maxTasks;
	}

	public int getExecutedTasks() {
		return executedTasks;
	}

	public void putContext(String key, Object value) {
		workContext.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getContext(String key) {
		return (T)workContext.get(key);
	}

	public void setWorkContext(WorkContext workContext) {
		this.workContext = workContext;
	}
	public void setRoutingKey(String key) {
		workContext.setRoutingKey(key);
	}
}
