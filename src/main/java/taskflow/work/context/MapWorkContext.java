package taskflow.work.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public class MapWorkContext implements WorkContext {
	private String routingKey;
	private Map<String, Object> context;
	// finish task执行完后设置此值,保存需要返回的数据
	private Object result;
	// 保存Task运行期间的异常
	private Map<String, Exception> exceptions;

	public MapWorkContext() {
		context = new HashMap<String, Object>();
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

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public Object get(String parameterName) {
		return context.get(parameterName);
	}

	public void put(String key, Object value) {
		context.put(key, value);
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

	@Override
	public String toString() {
		return "MapWorkContext [routingKey=" + routingKey + ", context=" + context + ", result=" + result
				+ ", exceptions=" + exceptions + "]";
	}
	
}
