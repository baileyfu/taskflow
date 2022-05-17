package taskflow.work;

import java.util.ArrayList;
import java.util.function.Function;

import taskflow.constants.PropertyNameAndValue;
import taskflow.exception.TaskFlowException;
import taskflow.task.routing.TaskRoutingWrap;
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
	private boolean executed;
	public AbstractWork() {
		workContext = new MapWorkContext(this.getClass());
		this.name = this.toString();
	}

	public AbstractWork(Function<Work, WorkContext> WorkContextCreator) {
		workContext = WorkContextCreator.apply(this);
		this.name = this.toString();
	}

	/**
	 * 记录任务调用轨迹,并检查任务调用次数上限
	 */
	void receive(TaskRoutingWrap stationRoutingWrap) throws Exception {
		((AbstractWorkContext)workContext).setCurrentTask(stationRoutingWrap.getName());
		// 单个work定义的优先级高于系统定义
		int maxTasks = this.maxTasks > 0 ? this.maxTasks : Integer.getInteger(PropertyNameAndValue.WORK_MAX_TASKS, 0);
		if (maxTasks > 0 && maxTasks <= (executedTasks + 1)) {
			throw new TaskFlowException("the work '"+name+"''s maxTasks is:" + maxTasks+",the executed of times has been exceeded!");
		}
		if (traceable) {
			if (Boolean.getBoolean(PropertyNameAndValue.WORK_TRACEABLE)) {
				if (taskTraces == null) {
					taskTraces = new ArrayList<TaskTrace>();
				}
				taskTraces.add(new TaskTrace(stationRoutingWrap.getName(), workContext.toString()));
			}
		}
		executedTasks++;
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
	//让SequentialRouteWork和CustomRouteWork都可以设置路由信息，以支持将某个SequentialRouteWork作为一个Task在其他的CustomRouteWork中执行时可以路由到其下一个Task
	//通过配置Task或者Routing时指定extra来决定路由信息
	public void setRoutingKey(String key) {
		workContext.setRoutingKey(key);
	}
	/**
	 * 处理Work执行过程中的异常;也包括各Task执行过程中hold的异常
	 * 最好创建子类覆盖方法以自定义异常处理
	 * @param workException
	 *            Work.run时抛出的异常
	 */
	protected void dealExcpetion(Exception workException) {
		workContext.holderException(((AbstractWorkContext) workContext).getCurrentTask(), workException);
		throw new TaskFlowException("TaskFlow's work '"+name+"' occur error!", workException);
	}
	
	@Override
	public WorkContext run() {
		if (executed) {
			throw new TaskFlowException("Non repeatable ! the Work '" + name + "' has already executed!");
		}
		executed = true;
		return execute();
	}

	abstract protected WorkContext execute(); 
}
