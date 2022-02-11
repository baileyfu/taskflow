package taskflow.work;

import taskflow.task.TaskRoutingWrap;

/**
 * 执行Work中不对外暴露的方法
 * @author bailey
 * @date 2022年2月10日
 */
public class WorkAgent {

	public static void callReceive(Work work,TaskRoutingWrap task) throws Exception{
		((AbstractWork)work).receive(task);
	}
}
