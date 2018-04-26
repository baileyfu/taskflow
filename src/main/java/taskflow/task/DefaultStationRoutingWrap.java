package taskflow.task;


import taskflow.worker.Worker;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public class DefaultStationRoutingWrap extends AbstractStationRoutingWrap {

    private BusHandlerMethod handlerMethod;

    public void doBusiness(Worker bus) {
        if (handlerMethod != null && handlerMethod.getBean() != null) {
            super.doBusiness(bus);
        }
    }

    public String getName() {
        return handlerMethod.getBean().getClass().getSimpleName();
    }

    public BusHandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(BusHandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public void invokeStationMethod(Worker bus) throws Exception {
        handlerMethod.invokeForBus(bus);
    }
}
