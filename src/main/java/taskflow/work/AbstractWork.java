package taskflow.work;

import java.util.ArrayList;
import java.util.Map;

import taskflow.exception.TaskFlowException;
import taskflow.task.TaskRoutingWrap;
import taskflow.work.context.AbstractWorkContext;
import taskflow.work.context.MapWorkContext;
import taskflow.work.context.TaskTrace;
import taskflow.work.context.WorkContext;

public abstract class AbstractWork implements Work{
	protected String name;
	protected int maxTasks;
	protected int executedTasks;
	protected WorkContext workContext;
	protected ArrayList<TaskTrace> taskTraces;
	protected boolean traceable;
	public AbstractWork() {
		workContext = new MapWorkContext(this.getClass());
	}
	public AbstractWork(Map<String,String> taskRefExtraMap) {
		workContext = new MapWorkContext(this.getClass());
		((MapWorkContext) workContext).setTaskRefExtraMap(taskRefExtraMap);
	}
	/**
	 * 最好创建子类覆盖次方法以自定义异常处理
	 */
	public void dealExcpetion(Exception workException) {
		workContext.holderException(((AbstractWorkContext)workContext).getCurrentTask(), workException);
		System.err.println(workException.getMessage());
	}

	/**
	 * 记录任务调用轨迹,并检查任务调用次数上限
	 */
	public void receive(TaskRoutingWrap stationRoutingWrap) throws Exception {
		((AbstractWorkContext)workContext).setCurrentTask(stationRoutingWrap.getName());
		if (maxTasks > 0 && maxTasks <= executedTasks++) {
			throw new TaskFlowException("max tasks is:" + maxTasks);
		}
		if (traceable) {
			if (taskTraces == null) {
				taskTraces = new ArrayList<TaskTrace>();
			}
			taskTraces.add(new TaskTrace(stationRoutingWrap.getName(), workContext.toString()));
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isTraceable() {
		return traceable;
	}
	public void setTraceable(boolean traceable) {
		this.traceable = traceable;
	}
	public ArrayList<TaskTrace> getTaskTraces() {
		return taskTraces;
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
}
