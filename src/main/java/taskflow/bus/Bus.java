package taskflow.bus;

import taskflow.context.BusContext;
import taskflow.task.StationRoutingWrap;

/**
 * Created by lizhou on 2017/5/10/010.
 */
public interface Bus {
    /**
     * 获取bus上下文环境
     * @return
     */
    BusContext getBusContext();

    /**
     * 异常处理
     * @param e
     */
    void dealExcpetion(Exception e);

    /**
     * 对于每个Station在调用真正的Station业务逻辑之前进行操作
     * @param StationRoutingWrap 包含Station具体业务逻辑和Routing信息
     * @throws Exception
     */
    void arrive(StationRoutingWrap StationRoutingWrap) throws Exception;

    /**
     * 加入上下文环境
     * @param key
     * @param input
     */
    void putContext(String key, Object input);

    /**
     * 业务开始
     * @return
     */
    BusContext run();

    /**
     * 设置BusContext
     * @param busContext
     */
    void setBusContext(BusContext busContext);
}
