package x.demo.station;

import java.util.List;

import taskflow.task.Taskparam;
import taskflow.task.CustomMethodTask;
import taskflow.work.Work;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class FindMax extends CustomMethodTask{
    public static final String FindMaxKey = "maxValue";
    public void doBusiness(@Taskparam("intList") List<Integer> l, Work bus) {
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
}
