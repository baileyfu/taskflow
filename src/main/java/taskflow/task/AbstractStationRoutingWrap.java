package taskflow.task;

import taskflow.routing.Routing;
import taskflow.work.Work;

/**
 * Created by lizhou on 2017/5/10/010.
 */
public abstract class AbstractStationRoutingWrap implements TaskRoutingWrap {
    private Routing routing;

    public void doBusiness(Work bus) {
        try {
            bus.arrive(this);
            invokeStationMethod(bus);
        } catch (Exception e) {
            bus.dealExcpetion(e);
        }
        if (routing != null) {
            TaskRoutingWrap next = routing.doRouting(bus.getBusContext());
            if (next != null) {
                next.doBusiness(bus);
            }
        }
    }


    public Routing getRouting() {
        return routing;
    }

    public void setRouting(Routing routing) {
        this.routing = routing;
    }
}
