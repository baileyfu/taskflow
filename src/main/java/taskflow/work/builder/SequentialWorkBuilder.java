package taskflow.work.builder;

import java.util.HashMap;
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
public class SequentialWorkBuilder extends WorkBuilder{
	private static final String ASYNC = "ASYNC@";
	private Function<Map<String, String>,SequentialRouteWork> workCreater;
	SequentialWorkBuilder(Function<Map<String, String>,SequentialRouteWork> workCreater) {
		this.workCreater = workCreater;
	}
	public SequentialWorkBuilder addSyncTask(Task task) {
		taskMap.put(ASYNC + task.toString(), task);
		return this;
	}
	public SequentialWorkBuilder addSyncTask(Task task,String extra) {
		taskRefExtraMap = taskRefExtraMap == null ? new HashMap<>() : taskRefExtraMap;
		taskRefExtraMap.put(ASYNC + task.toString(), extra);
		return addSyncTask(task);
	}
	public Work build() {
		return build(null);
	}
	
	public Work build(TaskExecutorFactory taskExecutorFactory) {
		SequentialRouteWork work = workCreater.apply(taskRefExtraMap);
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
		return work;
	}
}
