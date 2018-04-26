package taskflow.task;

import taskflow.work.Work;

/**
 * Created by lizhou on 2017/5/10/010.
 */
public interface TaskRoutingWrap {

    void invokeStationMethod(Work bus) throws Exception;

    String getName();

    void doBusiness(Work bus);
}
