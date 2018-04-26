package taskflow.routing.impl;

import java.util.List;

import taskflow.context.BusContext;
import taskflow.routing.Routing;
import taskflow.routing.RoutingCondition;
import taskflow.task.StationRoutingWrap;

/**
 * Created by lizhou on 2017/4/7/007.
 */
public class DefaultRouting implements Routing {
    private List<RoutingCondition> routingConditions;

    public DefaultRouting(List<RoutingCondition> routingConditions) {
        this.routingConditions = routingConditions;
    }

    public DefaultRouting() {
    }

    public StationRoutingWrap doRouting(BusContext busContext) {
        for (RoutingCondition routingCondition : routingConditions) {
            if (routingCondition.isDefaultMatch() ||routingCondition.matched(busContext)) {
                return routingCondition.getStationRoutingWrap();
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
