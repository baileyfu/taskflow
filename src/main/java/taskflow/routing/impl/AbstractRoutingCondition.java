package taskflow.routing.impl;

import taskflow.routing.RoutingCondition;
import taskflow.task.StationRoutingWrap;

/**
 * RoutingCondition抽象实现类，反向代理StationRoutingWrap的调用。
 * Created by lizhou on 2017/4/7/007.
 */
public abstract class AbstractRoutingCondition implements RoutingCondition {
    private StationRoutingWrap stationRoutingWrap;

    public StationRoutingWrap getStationRoutingWrap() {
        return stationRoutingWrap;
    }

    public void setStationRoutingWrap(StationRoutingWrap stationRoutingWrap) {
        this.stationRoutingWrap = stationRoutingWrap;
    }
}
