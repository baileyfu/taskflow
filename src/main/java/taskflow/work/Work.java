package taskflow.work;

import taskflow.context.WorkContext;
import taskflow.task.TaskRoutingWrap;

/**
 * worker在定义的时候是SCOPE_PROTOTYPE
 * 
 * @author bailey.fu
 * @date 2018年4月26日
 * @version 1.0
 * @description Created by lizhou on 2017/4/8/008. <br/>
 * update by bailey.fu
 */
public interface Work {
    /**
     * 获取work上下文环境
     * @return
     */
    WorkContext getWorkContext();

    /**
     * 异常处理
     * @param e
     */
    void dealExcpetion(Exception e);

    /**
     * 在调用真正的Task业务逻辑之前进行操作
     * @param StationRoutingWrap 包含Station具体业务逻辑和Routing信息
     * @throws Exception
     */
    void receive(TaskRoutingWrap taskRoutingWrap) throws Exception;

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
    WorkContext run();

    /**
     * 设置BusContext
     * @param busContext
     */
    void setWorkContext(WorkContext workContext);
}
