package x.demo.station;

import taskflow.task.Task;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class SoutOutNo implements Task {

    public void printNo() {
        System.out.println("No");
    }

    @Override
    public String getName() {
        return null;
    }

}
