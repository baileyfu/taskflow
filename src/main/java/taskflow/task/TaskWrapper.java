package taskflow.task;

import org.springframework.util.Assert;

import taskflow.work.Work;
import taskflow.work.context.WorkContext;

/**
 * 将Work封装为Task
 * 
 * @author bailey
 * @date 2022年4月6日
 */
public class TaskWrapper implements Task{
	private Work target;
	private String resultName;
	public TaskWrapper(Work work) {
		this.target = work;
		this.resultName = work.getName();
	}
	/**
	 * @param target 目标Work
	 * @param resultName 目标Work执行完成后将getResult()放入WorkContext时的key ; 默认为Work的name
	 */
	public TaskWrapper(Work work,String resultName) {
		this.target = work;
		this.resultName = resultName;
	}
	
	@Override
	public void execute(WorkContext workContext) {
		Assert.notNull(target, "the work of TaskWrapper can not be null!");
		/**
		 * work类型的task不考虑extra
		 */
		//workContext.getRuntimeArgs()
		target.getWorkContext().putAll(workContext);
		Object result = target.run().getResult();
		if (result != null) {
			workContext.put(resultName, result);
		}
	}

}
