package taskflow.work.context;

import java.util.HashMap;
import java.util.Map;

public class ExtraArgsHolder {
	//task定义的运行时参数
	private static Map<String, String> TASK_EXTRA_MAP;
	//task.routing定义的运行时参数;key格式：previousTask_currentTask
	private static Map<String, String> TASK_ROUTING_EXTRA_MAP;
	
	protected Map<String, String> takeAllExtra() {
		Map<String, String> all = new HashMap<>();
		if (TASK_EXTRA_MAP != null) {
			all.putAll(TASK_EXTRA_MAP);
		}
		if (TASK_ROUTING_EXTRA_MAP != null) {
			all.putAll(TASK_ROUTING_EXTRA_MAP);
		}
		return all;
	}
	
	protected String getTaskRoutingExtra(String taskId, String toTask) {
		if (TASK_ROUTING_EXTRA_MAP != null) {
			return TASK_ROUTING_EXTRA_MAP.get(taskId + "_" + toTask);
		}
		return null;
	}
	
	protected String getTaskExtra(String taskId) {
		if (TASK_EXTRA_MAP != null) {
			return TASK_EXTRA_MAP.get(taskId);
		}
		return null;
	}
	
	public static void putTaskExtra(String taskId,String extra) {
		if (TASK_EXTRA_MAP == null) {
			TASK_EXTRA_MAP = new HashMap<>();
		}
		TASK_EXTRA_MAP.put(taskId.trim(), extra);
	}

	public static void putTaskRoutingExtra(String taskId, String toTask, String extra) {
		if (TASK_ROUTING_EXTRA_MAP == null) {
			TASK_ROUTING_EXTRA_MAP = new HashMap<>();
		}
		TASK_ROUTING_EXTRA_MAP.put(taskId.trim() + "_" + toTask.trim(), extra);
	}

	@Override
	public String toString() {
		return "ExtraArgsHolder [TASK_ROUTING_EXTRA_MAP=" + TASK_ROUTING_EXTRA_MAP + ", TASK_EXTRA_MAP="
				+ TASK_EXTRA_MAP + "]";
	}
}
