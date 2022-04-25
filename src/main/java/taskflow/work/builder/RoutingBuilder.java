package taskflow.work.builder;

import org.apache.commons.lang3.StringUtils;

import taskflow.routing.match.PatternType;

public class RoutingBuilder {
	private Routing routing;

	private RoutingBuilder() {
		routing = new Routing();
	}
	
	public RoutingBuilder key(String key) {
		routing.key = StringUtils.defaultString(key);
		return this;
	}
	public RoutingBuilder toTask(String toTask) {
		routing.toTask = StringUtils.defaultString(toTask);
		return this;
	}
	public RoutingBuilder pattern(PatternType pattern) {
		routing.pattern = pattern == null ? PatternType.string : pattern;
		return this;
	}
	public RoutingBuilder extra(String extra) {
		routing.extra = StringUtils.defaultString(extra);
		return this;
	}
	
	public Routing build() {
		return routing;
	}
	
	public static RoutingBuilder newInstance() {
		return new RoutingBuilder();
	}
	static class Routing{
		private String key;
		private String toTask;
		private PatternType pattern = PatternType.string;
		private String extra;
		public String getKey() {
			return key;
		}
		public String getToTask() {
			return toTask;
		}
		public PatternType getPattern() {
			return pattern;
		}
		public String getExtra() {
			return extra;
		}
		public boolean isEffective() {
			return !StringUtils.isEmpty(key) || !StringUtils.isEmpty(toTask);
		}
		@Override
		public String toString() {
			return "Routing [key=" + key + ", toTask=" + toTask + "]";
		}
		@Override
		public boolean equals(Object obj) {
			return obj != null && this.toString().equals(obj.toString());
		}
	}
}
