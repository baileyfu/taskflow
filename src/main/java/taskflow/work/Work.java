package taskflow.work;

import java.util.ArrayList;

import taskflow.work.context.TaskTrace;
import taskflow.work.context.WorkContext;

/**
 * worker在定义的时候是SCOPE_PROTOTYPE
 * 
 * @author bailey.fu
 * @date 2018年4月26日
 * @version 1.0
 * @description Created by lizhou on 2017/4/8/008. <br/>
 *              update by bailey.fu
 */
public interface Work {
	String getName();
	/**
	 * 获取work上下文环境
	 * 
	 * @return
	 */
	WorkContext getWorkContext();

	/**
	 * 返回Task调用轨迹
	 * 
	 * @return
	 */
	ArrayList<TaskTrace> getTaskTraces();

	/**
	 * 加入上下文环境
	 * 
	 * @param key
	 * @param input
	 */
	void putContext(String key, Object input);

	/**
	 * 从上下文获取参数
	 * 
	 * @param key
	 * @return
	 */
	public <T> T getContext(String key);

	/**
	 * 业务开始<br/>
	 * 不考虑线程安全,所有Work实现类均以原型模式创建
	 * 
	 * @return
	 */
	WorkContext run();
}
