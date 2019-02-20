package taskflow.config.bean;

import java.util.Set;

/**
 * Task定义
 */
public class TaskDefinition {
	private String taskId;
	private String taskBeanId;
	private String method;
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
	public static class RouteDefinition{
		private String value;
		private String to;
		private String patten;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public String getPatten() {
			return patten;
		}
		public void setPatten(String patten) {
			this.patten = patten;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
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
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}
}
