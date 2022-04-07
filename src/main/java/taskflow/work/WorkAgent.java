package taskflow.work;

import taskflow.task.routing.TaskRoutingWrap;

/**
 * 执行Work中不对外暴露的方法
 * 
 * @author bailey
 * @date 2022年2月10日
 */
public class WorkAgent {

	public static void callReceive(Work work, TaskRoutingWrap task) throws Exception {
		((AbstractWork) work).receive(task);
	}

	/**
	 * 当前Task是否需要等待所需入参<br/>
	 * 
	 * 若当前Task为同步，且存在尚未执行的异步Task，则返回true，否则返回false
	 * 
	 * @param work
	 * @return
	 */
	public static boolean callIsCurrentTaskNeedWait4Params(SequentialRouteWork work) {
		return work.isCurrentTaskNeedWait4Params();
	}
}
