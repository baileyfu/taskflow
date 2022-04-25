package taskflow.routing;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import taskflow.task.routing.TaskRoutingWrap;
import taskflow.work.context.WorkContext;
import static taskflow.constants.RoutingConditionPropValue.CONDITION_DEFAULT_VALUE;

/**
 * Created by lizhou on 2017/4/7/007.
 */
public class DefaultRouting implements Routing {
    private List<RoutingCondition> routingConditions;
    public DefaultRouting() {}
    public DefaultRouting(List<RoutingCondition> routingConditions) {
        this.routingConditions = routingConditions;
    }

	public TaskRoutingWrap doRouting(WorkContext workContext) {
		if (routingConditions.size() == 1) {
			RoutingCondition unique = routingConditions.get(0);
			// 当只有一个routing时，不设置key并且Task中不设置RoutingKey，将直接跳转到toTask
			if ((ifKeyEmpty(unique.getCondition()) && StringUtils.isEmpty(workContext.getRoutingKey())) 
					|| unique.matched(workContext)) {
				return unique.getTaskRoutingWrap();
			}
		} else {
			for (RoutingCondition routingCondition : routingConditions) {
				if (routingCondition.matched(workContext)) {
					return routingCondition.getTaskRoutingWrap();
				}
			}
		}
		return null;
	}
	
	private boolean ifKeyEmpty(String condition) {
		return StringUtils.isEmpty(condition) || CONDITION_DEFAULT_VALUE.equals(condition);
	}
	
    public List<RoutingCondition> getRoutingConditions() {
        return routingConditions;
    }

    public void setRoutingConditions(List<RoutingCondition> routingConditions) {
        this.routingConditions = routingConditions;
    }
}
