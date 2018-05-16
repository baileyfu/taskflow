package taskflow.task;

import taskflow.work.Work;

/**
 * 可指定任务执行的方法
 * 
 * @author bailey.fu
 * @date 2018年5月16日
 * @version 1.0
 * @description
 */
public class CustomMethodTask implements Task{
	@Override
	public String getName() {
		return this.getClass().getName();
	}
	@Override
	public void execute(Work work) {
	}
}
