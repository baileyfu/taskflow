package x.demo.station;

import taskflow.task.CustomMethodTask;
import taskflow.work.DefaultWork;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class TestEndStation extends CustomMethodTask {
    public void doBusiness(DefaultWork bus) {
        System.out.println("over");
    }
}
