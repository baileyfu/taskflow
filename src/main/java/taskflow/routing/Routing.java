package taskflow.routing;

import taskflow.context.WorkContext;
import taskflow.task.TaskRoutingWrap;
import taskflow.task.Task;

/**
 * 当一个{@link Task}处理完毕后，可以动态确定下一个{@link Task}
 * 根据定义好的{@link RoutingCondition}，根据workContext的值确定下一个{@link Task}
 * 设计模式中的State Pattern
 * Created by lizhou on 2017/3/14/014.
 */
public interface Routing {
    TaskRoutingWrap doRouting(WorkContext workContext);
}
