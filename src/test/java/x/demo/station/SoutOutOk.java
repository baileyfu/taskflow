package x.demo.station;

import taskflow.task.Task;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class SoutOutOk implements Task {

    public void printOk() {
        System.out.println("ok");
    }

    @Override
    public String getName() {
        return null;
    }

}
