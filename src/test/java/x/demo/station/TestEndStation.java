package x.demo.station;

import taskflow.task.Task;
import taskflow.worker.DefaultWorker;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class TestEndStation implements Task {
    public void doBusiness(DefaultWorker bus) {
        System.out.println("over");
    }

    @Override
    public String getName() {
        return null;
    }
}
