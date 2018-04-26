package x.demo.station;

import java.util.List;

import taskflow.task.BusParameter;
import taskflow.task.Task;
import taskflow.work.Work;

/**
 * Created by lizhou on 2017/4/8/008.
 */
public class FindMin implements Task {
    public static final String FindMinKey = "minValue";

    public void doBusiness(List<Integer> intList, @BusParameter(value = "test", require = false) char test, Work bus) {
        if (intList.size() == 0) {
            return;
        }
        int min = intList.get(0);
        for (Integer integer : intList) {
            if (integer < min) {
                min = integer;
            }
        }
        bus.putContext(FindMinKey, min);
    }

    @Override
    public String getName() {
        return "FindMin";
    }
}
