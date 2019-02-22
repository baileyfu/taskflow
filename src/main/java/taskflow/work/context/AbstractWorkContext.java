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
	private String previousTask;
	private String currentTask;
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
	}
	
	public void setCurrentTask(String currentTask) {
		this.previousTask = this.currentTask;
		this.currentTask = currentTask;
	}
	public String getCurrentTask() {
		return currentTask;
	}
	public void setTaskRefExtraMap(Map<String, String> taskRefExtraMap) {
		this.taskRefExtraMap = taskRefExtraMap;
	}

	@Override
	public String getRuntimeArgs() {
		if(currentTask==null) 
			return null;
		String runtimeArgs = null;
		if (SequentialRouteWork.class.isAssignableFrom(workClazz)) {
			if (taskRefExtraMap != null) {
				runtimeArgs = taskRefExtraMap.get(currentTask);
			}
		} else if (CustomRouteWork.class.isAssignableFrom(workClazz)) {
			if (previousTask != null) {
				runtimeArgs = getTaskRoutingExtra(previousTask, currentTask);
			}
		}
		if (runtimeArgs == null) {
			runtimeArgs = getTaskExtra(currentTask);
		}
		return runtimeArgs;
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
		return "AbstractWorkContext [currentTask=" + currentTask + ", routingKey=" + routingKey + ", result=" + result 
				+ ", exceptions=" + exceptions 
				+ ", taskRefExtraMap=" + taskRefExtraMap
				+ ", " + super.toString() + "]";
	}
}
