package taskflow.work.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import taskflow.constants.PropertyNameAndValue;
import taskflow.work.AbstractWork;
import taskflow.work.CustomRouteWork;
import taskflow.work.SequentialRouteWork;
import taskflow.work.Work;

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
	protected int maxTasks;
	protected Boolean traceable;
	protected Map<String, String> taskRefExtraMap = null;
	
	protected void addExtra(String taskName, String extra) {
		taskRefExtraMap = taskRefExtraMap == null ? new HashMap<>() : taskRefExtraMap;
		taskRefExtraMap.put(taskName, extra);
	}
	public void setMaxTasks(int maxTasks) {
		this.maxTasks = maxTasks;
	}
	public void setTraceable(Boolean traceable) {
		this.traceable = traceable;
	}
	public abstract Work build();
	protected Work postBuild(Work work) {
		if (maxTasks > 0) {
			((AbstractWork) work).setMaxTasks(maxTasks);
		}
		if (traceable != null) {
			((AbstractWork) work).setTraceable(traceable);
		}
		return work;
	}
	
	private static Function<Map<String, String>,SequentialRouteWork> DEFAULT_WORK_CREATER=(extraMap) -> new SequentialRouteWork(extraMap);
	public static SequentialWorkBuilder newSequentialInstance() {
		return new SequentialWorkBuilder(DEFAULT_WORK_CREATER);
	}

	public static SequentialWorkBuilder newSequentialInstance(Function<Map<String, String>,SequentialRouteWork> workCreater) {
		return new SequentialWorkBuilder(workCreater);
	}
	
	public static RouteAbleWorkBuilder newRouteableInstance() {
		return new RouteAbleWorkBuilder();
	}
	
	public static RouteAbleWorkBuilder newRouteableInstance(CustomRouteWork work) {
		return new RouteAbleWorkBuilder(work);
	}
}
