package x.demo.station;

import taskflow.task.Task;
import taskflow.work.DefaultWork;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class TestEndStation implements Task {
    public void doBusiness(DefaultWork bus) {
        System.out.println("over");
    }

    @Override
    public String getName() {
        return null;
    }
}
