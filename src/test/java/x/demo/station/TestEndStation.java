package x.demo.station;

import taskflow.bus.DefaultBus;
import taskflow.task.Task;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class TestEndStation implements Task {
    public void doBusiness(DefaultBus bus) {
        System.out.println("over");
    }

    @Override
    public String getName() {
        return null;
    }
}
