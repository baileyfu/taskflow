package taskflow.task;

import taskflow.worker.Worker;

/**
 * Created by lizhou on 2017/5/10/010.
 */
public interface StationRoutingWrap {

    void invokeStationMethod(Worker bus) throws Exception;

    String getName();

    void doBusiness(Worker bus);
}
