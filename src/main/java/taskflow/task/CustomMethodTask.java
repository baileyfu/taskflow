package taskflow.task;

import taskflow.work.Work;

/**
 * 不执行默认的Task.execute方法,而是自定义业务执行
 * 
 * @author bailey.fu
 * @date 2018年5月16日
 * @version 1.0
 * @description
 */
public abstract class CustomMethodTask implements Task {
	private String name = this.getClass().getName();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void execute(Work work) {
	}
}
