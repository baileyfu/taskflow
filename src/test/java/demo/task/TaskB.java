package demo.task;

import java.util.Date;

import taskflow.task.Task;
import taskflow.work.context.WorkContext;

public class TaskB implements Task{

	@Override
	public void execute(WorkContext workContext) {
		workContext.put("birth", new Date());
		System.out.println("TaskB's method execute be invoked !!! extra : "+workContext.getRuntimeArgs());
	}
}
