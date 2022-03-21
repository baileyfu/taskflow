package demo.task;

import taskflow.task.Task;
import taskflow.work.context.WorkContext;

public class TaskA implements Task{
	@Override
	public void execute(WorkContext workContext) {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		workContext.put("name","Jack");
		System.out.println("TaskA's method execute be invoked !!! extra : "+workContext.getRuntimeArgs()+" , called by : "+Thread.currentThread());
	}
}
