package taskflow.routing;

import taskflow.context.WorkContext;
import taskflow.task.TaskRoutingWrap;

/**
 * 路由条件，现在只支持根据{@link WorkContext}中的routingKey进行路由
 * Created by lizhou on 2017/4/7/007.
 */
public interface RoutingCondition {
    boolean matched(WorkContext busContext);

    TaskRoutingWrap getStationRoutingWrap();

    /**
     * 如果返回true，则默认匹配成功
     * @return
     */
    boolean isDefaultMatch();
}
