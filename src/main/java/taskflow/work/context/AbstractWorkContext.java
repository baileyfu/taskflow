package taskflow.work.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import taskflow.work.CustomRouteWork;
import taskflow.work.SequentialRouteWork;
import taskflow.work.Work;

public abstract class AbstractWorkContext extends ExtraArgsHolder implements WorkContext {
	//仅CustomRouteWork需要
	private String previousTask;
	//异步情况下各线程当前task不同
	private ThreadLocal<String> currentTask;
	
	private String routingKey;
	// finish task执行完后设置此值,保存需要返回的数据
	private Object result;
	//task-ref定义的运行时参数
	private Map<String,String> taskRefExtraMap;
	// 保存Task运行期间的异常
	private Map<String, Exception> exceptions;
	//持有当前WorkContext的Work类型
	private Class<? extends Work> workClazz;
	
	public AbstractWorkContext(Class<? extends Work> workClazz) {
		this.workClazz = workClazz;
		this.currentTask = new ThreadLocal<>();
	}
	
	public void setCurrentTask(String currentTask) {
		this.previousTask = this.currentTask.get();
		this.currentTask.set(currentTask);
	}
	public String getCurrentTask() {
		return currentTask.get();
	}
	public void setTaskRefExtraMap(Map<String, String> taskRefExtraMap) {
		this.taskRefExtraMap = taskRefExtraMap;
	}
	@Override
	public Map<String, String> getAllRuntimeArgs() {
		Map<String, String> argsMap = takeAllExtra();
		if (SequentialRouteWork.class.isAssignableFrom(workClazz)) {
			if (taskRefExtraMap != null) {
				argsMap.putAll(taskRefExtraMap);
			}
		}
		return argsMap;
	}
	@Override
	public String getRuntimeArgs() {
		String currentTaskValue = currentTask.get();
		if (currentTaskValue == null)
			return null;
		String runtimeArgs = null;
		if (SequentialRouteWork.class.isAssignableFrom(workClazz)) {
			if (taskRefExtraMap != null) {
				runtimeArgs = taskRefExtraMap.get(currentTaskValue);
			}
		} else if (CustomRouteWork.class.isAssignableFrom(workClazz)) {
			if (previousTask != null) {
				runtimeArgs = getTaskRoutingExtra(previousTask, currentTaskValue);
			}
		}
		return StringUtils.isEmpty(runtimeArgs) ? getTaskExtra(currentTaskValue) : runtimeArgs;
	}
	@Override
	public JSONObject getRuntimeArgsJSON() {
		String extraArgs = getRuntimeArgs();
		return StringUtils.isBlank(extraArgs) ? new JSONObject() : JSON.parseObject(extraArgs);
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@SuppressWarnings("unchecked")
	public <T> T getResult() {
		return (T) result;
	}
	
	public void holderException(String taskName, Exception exception) {
		if (exceptions == null) {
			exceptions = new HashMap<>();
		}
		exceptions.put(taskName, exception);
	}

	@Override
	public Map<String, Exception> getExceptions() {
		return exceptions;
	}

	@Override
	public String toString() {
		return "AbstractWorkContext [workClazz="+workClazz+", currentTask=" + currentTask + ", routingKey=" + routingKey + ", result=" + result 
				+ ", exceptions=" + exceptions 
				+ ", taskRefExtraMap=" + taskRefExtraMap
				+ ", " + super.toString() + "]";
	}
}
