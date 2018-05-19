package taskflow.routing.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;

import taskflow.pattern.PatternFactory;
import taskflow.pattern.PatternType;
import taskflow.pattern.match.PatternMatch;
import taskflow.work.context.WorkContext;

/**
 * 字符串或正则匹配<br/>
 * Created by lizhou on 2017/4/7/007.
 */
public class PatternRoutingCondition extends AbstractRoutingCondition {
	private PatternType pattern;
	private String condition;

	public boolean matched(WorkContext workContext) {
		String routingKey = workContext.getRoutingKey();
		PatternMatch patternMatch = PatternFactory.getPatternMatch(pattern);
		return patternMatch.isMatched(routingKey, condition);
	}
	public PatternType getPattern() {
		return pattern;
	}
	public void setPattern(PatternType pattern) {
		this.pattern = pattern;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
