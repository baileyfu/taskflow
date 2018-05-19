package taskflow.work.context;

import java.util.Map;

/**
 * work的task工作的上下文;持有各task工作的输入/输出参数
 * 
 * @author bailey.fu
 * @date 2018年5月16日
 * @version 1.0
 * @description
 */
public interface WorkContext {
	/**
	 * 获取上下文的参数
	 * @param key
	 * @return
	 */
    Object get(String key);
    /**
     * 设置上下文参数
     * @param key
     * @param value
     */
    void put(String key, Object value);
    /**
     * 获取要返回的结果
     * @return
     */
    <T>T getResult();
    /**
     * 设置要返回的结果;一般由finish task来操作
     * @param result
     */
    void setResult(Object result);
    void setRoutingKey(String key);
    /**
     * 获取路由信息
     * 注意，如果一个Task没有设置，则为上一个Task的设置的值(如果上一个Task也没设置，以此类推，否则为null)
     *
     * @return
     */
    String getRoutingKey();

    /**
     * 保存Task运行过程中的exception
     * @param taskName
     * @param e
     */
    void holderException(String taskName,Exception e);
    Map<String,Exception> getExceptions();
}
