package taskflow.work.builder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import taskflow.constants.PropertyNameAndValue;
import taskflow.task.Task;
import taskflow.work.SequentialRouteWork;

/**
 * Work构造器,编码方式创建work
 * @author bailey
 * @date 2022年3月21日
 */
public abstract class WorkBuilder {
	static {
		//初始化默认参数
		PropertyNameAndValue.init();
	}
	protected Map<String, Task> taskMap = null;
	protected Map<String, String> taskRefExtraMap = null;
	
	WorkBuilder() {
		taskMap = new LinkedHashMap<>();
	}
	
	public WorkBuilder addTask(Task task) {
		taskMap.put(task.toString(), task);
		return this;
	}
	public WorkBuilder addTask(Task task,String extra) {
		taskRefExtraMap = taskRefExtraMap == null ? new HashMap<>() : taskRefExtraMap;
		taskRefExtraMap.put(task.toString(), extra);
		return addTask(task);
	}
	
	private static Function<Map<String, String>,SequentialRouteWork> DEFAULT_WORK_CREATER=(extraMap) -> new SequentialRouteWork(extraMap);
	public static SequentialWorkBuilder newInstance() {
		return new SequentialWorkBuilder(DEFAULT_WORK_CREATER);
	}

	public static SequentialWorkBuilder newInstance(Function<Map<String, String>,SequentialRouteWork> workCreater) {
		return new SequentialWorkBuilder(workCreater);
	}
}
