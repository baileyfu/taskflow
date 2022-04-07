package taskflow.config.bean;

import java.util.Set;

/**
 * Task定义
 */
public class TaskDefinition {
	private String taskId;
	private String taskBeanId;
	private String method;
	private String extra;
	//同一路由只允许定义一次
	private Set<RouteDefinition> routeDefinitions;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskBeanId() {
		return taskBeanId;
	}
	public void setTaskBeanId(String taskBeanId) {
		this.taskBeanId = taskBeanId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public Set<RouteDefinition> getRouteDefinitions() {
		return routeDefinitions;
	}
	public void setRouteDefinitions(Set<RouteDefinition> routeDefinitions) {
		this.routeDefinitions = routeDefinitions;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskDefinition other = (TaskDefinition) obj;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TaskDefinition [taskId=" + taskId + ", taskBeanId=" + taskBeanId + ", method=" + method + ", extra="
				+ extra + ", routeDefinitions=" + routeDefinitions + "]";
	}
	public static class RouteDefinition{
		private String key;
		private String toTask;
		//true则taskId为work
		private boolean isItWork;
		private String pattern;
		private String extra;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getToTask() {
			return toTask;
		}
		public void setToTask(String toTask) {
			this.toTask = toTask;
		}
		public boolean isItWork() {
			return isItWork;
		}
		public void setItWork(boolean isItWork) {
			this.isItWork = isItWork;
		}
		public String getPattern() {
			return pattern;
		}
		public void setPattern(String pattern) {
			this.pattern = pattern;
		}
		public String getExtra() {
			return extra;
		}
		public void setExtra(String extra) {
			this.extra = extra;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RouteDefinition other = (RouteDefinition) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "RouteDefinition [key=" + key + ", toTask=" + toTask + ", isItWork=" + isItWork + ", pattern="
					+ pattern + ", extra=" + extra + "]";
		}
		
	}
}
