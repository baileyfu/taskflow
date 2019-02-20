package taskflow.work.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public abstract class AbstractWorkContext implements WorkContext {
	private String currentTask;
	private String routingKey;
	// finish task执行完后设置此值,保存需要返回的数据
	private Object result;
	private Map<String,String> extraArgsMap;
	// 保存Task运行期间的异常
	private Map<String, Exception> exceptions;
	
	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}
	public String getCurrentTask() {
		return currentTask;
	}
	public void setExtraArgsMap(Map<String, String> extraArgsMap) {
		this.extraArgsMap = extraArgsMap;
	}

	@Override
	public String getExtraArgs() {
		return extraArgsMap == null || currentTask == null ? null : extraArgsMap.get(currentTask);
	}
	@Override
	public JSONObject getExtraArgsJSON() {
		String extraArgs = getExtraArgs();
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
				+ ", extraArgsMap=" + extraArgsMap + ", exceptions=" + exceptions + "]";
	}
}
