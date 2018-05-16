package taskflow.routing.impl;

import java.util.List;

import taskflow.context.WorkContext;
import taskflow.routing.Routing;
import taskflow.routing.RoutingCondition;
import taskflow.task.TaskRoutingWrap;

/**
 * Created by lizhou on 2017/4/7/007.
 */
public class DefaultRouting implements Routing {
    private List<RoutingCondition> routingConditions;
    public DefaultRouting() {}
    public DefaultRouting(List<RoutingCondition> routingConditions) {
        this.routingConditions = routingConditions;
    }

    public TaskRoutingWrap doRouting(WorkContext busContext) {
        for (RoutingCondition routingCondition : routingConditions) {
            if (routingCondition.matched(busContext)) {
                return routingCondition.getTaskRoutingWrap();
            }
        }
        return null;
    }

    public List<RoutingCondition> getRoutingConditions() {
        return routingConditions;
    }

    public void setRoutingConditions(List<RoutingCondition> routingConditions) {
        this.routingConditions = routingConditions;
    }
}
