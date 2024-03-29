package taskflow.work.context;

import java.util.HashMap;
import java.util.Map;

import taskflow.work.Work;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public class MapWorkContext extends AbstractWorkContext {
	protected Map<String, Object> context;

	public MapWorkContext(Class<? extends Work> workClazz) {
		super(workClazz);
		context = new HashMap<String, Object>();
	}
	public MapWorkContext(Class<? extends Work> workClazz,Map<String,String> taskRefExtraMap) {
		super(workClazz);
		context = new HashMap<String, Object>();
		this.setTaskRefExtraMap(taskRefExtraMap);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String parameterName) {
		return (T)context.get(parameterName);
	}
	public void put(String key, Object value) {
		context.put(key, value);
	}

	@Override
	public String toString() {
		return "MapWorkContext [context=" + context + ", " + super.toString() + "]";
	}
	@Override
	public Map<String, Object> getAll() {
		return context;
	}
	@Override
	public void putAll(WorkContext workContext) {
		if (workContext != null) {
			context.putAll(workContext.getAll());
		}
	}
}
