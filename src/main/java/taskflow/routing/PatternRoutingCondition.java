package taskflow.routing;

import org.apache.commons.lang3.builder.ToStringBuilder;

import taskflow.routing.match.PatternFactory;
import taskflow.routing.match.PatternMatch;
import taskflow.routing.match.PatternType;
import taskflow.work.context.WorkContext;

/**
 * 字符串或正则匹配<br/>
 * Created by lizhou on 2017/4/7/007.
 */
public class PatternRoutingCondition extends AbstractRoutingCondition {
	private PatternType pattern;

	public boolean matched(WorkContext workContext) {
		String routingKey = workContext.getRoutingKey();
		PatternMatch patternMatch = PatternFactory.getPatternMatch(pattern);
		return patternMatch.isMatched(condition, routingKey);
	}
	public PatternType getPattern() {
		return pattern;
	}
	public void setPattern(PatternType pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
