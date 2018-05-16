package taskflow.context;

/**
 * work的task工作的上下文;持有各task工作的输入/输出参数
 * 
 * @author bailey.fu
 * @date 2018年5月16日
 * @version 1.0
 * @description
 */
public interface WorkContext {
    Object getValue(String key);
    void put(String key, Object value);
    void setRoutingKey(String key);
    /**
     * 获取路由信息
     * 注意，如果一个Station没有设置，则为上一个Station的设置的值(如果上一个Station也没设置，以此类推，否则为null)
     *
     * @return
     */
    String getRoutingKey();

    /**
     * 保存运行过程中exception
     *
     * @param e
     */
    void holderException(Exception e);
}
