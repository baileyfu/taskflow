package taskflow.work.context;

import java.util.HashMap;
import java.util.Map;

import taskflow.work.Work;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public class MapWorkContext extends AbstractWorkContext {
	private Map<String, Object> context;

	public MapWorkContext(Class<? extends Work> workClazz) {
		super(workClazz);
		context = new HashMap<String, Object>();
	}

	public Map<String, Object> getContext() {
		return context;
	}
	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	public Object get(String parameterName) {
		return context.get(parameterName);
	}
	public void put(String key, Object value) {
		context.put(key, value);
	}

	@Override
	public String toString() {
		return "MapWorkContext [context=" + context + ", " + super.toString() + "]";
	}
}
