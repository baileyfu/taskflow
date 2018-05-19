package x.demo.station;

import taskflow.task.Taskparam;
import taskflow.task.CustomMethodTask;
import taskflow.work.context.WorkContext;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class GetDiff extends CustomMethodTask {

    public void abstractCalculate(@Taskparam("maxValue") int a, @Taskparam("minValue") int b, WorkContext busContext) {

        if (Math.abs(a - b) < 10) {
            busContext.setRoutingKey("ok");
        } else {
            busContext.setRoutingKey("no");
        }
    }

}
