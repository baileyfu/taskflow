package x.demo.station;

import taskflow.context.WorkContext;
import taskflow.task.BusParameter;
import taskflow.task.Task;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class GetDiff implements Task {

    public void abstractCalculate(@BusParameter("maxValue") int a, @BusParameter("minValue") int b, WorkContext busContext) {

        if (Math.abs(a - b) < 10) {
            busContext.setRoutingKey("ok");
        } else {
            busContext.setRoutingKey("no");
        }
    }

    @Override
    public String getName() {
        return null;
    }

}
