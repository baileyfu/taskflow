package taskflow.work.builder;

import taskflow.routing.match.PatternType;

public class RoutingBuilder {
	private Routing routing;

	private RoutingBuilder() {
		routing = new Routing();
	}
	
	public RoutingBuilder key(String key) {
		routing.key = key;
		return this;
	}
	public RoutingBuilder toTask(String toTask) {
		routing.toTask = toTask;
		return this;
	}
	public RoutingBuilder pattern(PatternType pattern) {
		routing.pattern = pattern;
		return this;
	}
	public RoutingBuilder extra(String extra) {
		routing.extra = extra;
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
		@Override
		public String toString() {
			return "Routing [key=" + key + ", toTask=" + toTask + ", pattern=" + pattern + ", extra=" + extra + "]";
		}
		@Override
		public boolean equals(Object obj) {
			return obj != null && this.toString().equals(obj.toString());
		}
	}
}
