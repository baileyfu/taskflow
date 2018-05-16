package x.demo.station;

import taskflow.context.WorkContext;
import taskflow.task.BusParameter;
import taskflow.task.CustomMethodTask;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class GetDiff extends CustomMethodTask {

    public void abstractCalculate(@BusParameter("maxValue") int a, @BusParameter("minValue") int b, WorkContext busContext) {

        if (Math.abs(a - b) < 10) {
            busContext.setRoutingKey("ok");
        } else {
            busContext.setRoutingKey("no");
        }
    }

}
