package taskflow.work.builder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import taskflow.config.bean.TaskExecutorFactory;
import taskflow.exception.TaskFlowException;
import taskflow.task.DefaultTaskRoutingWrap;
import taskflow.task.Task;
import taskflow.work.SequentialRouteWork;
import taskflow.work.Work;

/**
 * SequentialWork构造器
 * @author bailey
 * @date 2022年3月21日
 */
public final class SequentialWorkBuilder extends WorkBuilder{
	private static final String ASYNC = "ASYNC@";
	private Map<String, Task> taskMap = null;
	private Function<Map<String, String>,SequentialRouteWork> workCreater;
	SequentialWorkBuilder(Function<Map<String, String>,SequentialRouteWork> workCreater) {
		this.workCreater = workCreater;
		taskMap = new LinkedHashMap<>();
	}
	
	public SequentialWorkBuilder addTask(Task task) {
		taskMap.put(task.getId(), task);
		return this;
	}
	public SequentialWorkBuilder addTask(Task task,String extra) {
		addExtra(task.getId(), extra);
		return addTask(task);
	}
	
	public SequentialWorkBuilder addAsyncTask(Task task) {
		taskMap.put(ASYNC + task.getId(), task);
		return this;
	}
	public SequentialWorkBuilder addAsyncTask(Task task,String extra) {
		addExtra(ASYNC + task.getId(), extra);
		return addAsyncTask(task);
	}

	public Work build(String workName) {
		return build(workName, null);
	}
	
	public Work build(TaskExecutorFactory taskExecutorFactory) {
		return build(null, taskExecutorFactory);
	}
	
	public Work build(String workName,TaskExecutorFactory taskExecutorFactory) {
		SequentialRouteWork work = workCreater.apply(taskRefExtraMap);
		work.setName(workName);
		work.setTaskExecutorFactory(taskExecutorFactory);
		try {
			for (Entry<String, Task> entry : taskMap.entrySet()) {
				Task task = entry.getValue();
				DefaultTaskRoutingWrap taskRoutingWrap = new DefaultTaskRoutingWrap(task);
				taskRoutingWrap.setName(entry.getKey());
				if (entry.getKey().substring(0, 6).equals(ASYNC)) {
					work.appendAsyncTask(taskRoutingWrap);
					continue;
				}
				work.appendTask(taskRoutingWrap);
			}
		} catch (Exception e) {
			throw new TaskFlowException("build work error!", e);
		}
		return postBuild(work);
	}
}
