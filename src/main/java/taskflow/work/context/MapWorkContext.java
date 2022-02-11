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
