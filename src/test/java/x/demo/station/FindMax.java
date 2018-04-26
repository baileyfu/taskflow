package x.demo.station;

import java.util.List;

import taskflow.task.BusParameter;
import taskflow.task.Task;
import taskflow.worker.Worker;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class FindMax implements Task {
    public static final String FindMaxKey = "maxValue";
    public void doBusiness(@BusParameter("intList") List<Integer> l, Worker bus) {
        if (l.size() == 0) {
            return;
        }
        int max = l.get(0);
        for (Integer integer : l) {
            if (integer > max) {
                max = integer;
            }
        }
        bus.putContext(FindMaxKey, max);
    }

    @Override
    public String getName() {
        return "FindMax";
    }
}
