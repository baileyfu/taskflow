package taskflow.routing.impl;

import taskflow.routing.RoutingCondition;
import taskflow.task.TaskRoutingWrap;

/**
 * RoutingCondition抽象实现类，反向代理StationRoutingWrap的调用。
 * Created by lizhou on 2017/4/7/007.
 */
public abstract class AbstractRoutingCondition implements RoutingCondition {
    private TaskRoutingWrap stationRoutingWrap;

    public TaskRoutingWrap getStationRoutingWrap() {
        return stationRoutingWrap;
    }

    public void setStationRoutingWrap(TaskRoutingWrap stationRoutingWrap) {
        this.stationRoutingWrap = stationRoutingWrap;
    }
}
