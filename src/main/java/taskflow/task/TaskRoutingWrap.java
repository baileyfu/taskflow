package taskflow.task;

import taskflow.work.Work;

/**
 * 执行task的逻辑并routing到下一个task
 */
public interface TaskRoutingWrap {
	String getName();

	/**
	 * 执行当前task并路由到下一个
	 * 
	 * @param work
	 */
	void doTask(Work work)throws Exception;
}
