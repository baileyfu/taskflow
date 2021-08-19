package demo.task;

import java.util.Date;

import taskflow.task.Task;
import taskflow.work.Work;

public class TaskB implements Task{

	@Override
	public void execute(Work work) {
		work.putContext("birth", new Date());
		System.out.println("TaskB's method execute be invoked !!! extra : "+work.getWorkContext().getRuntimeArgs());
	}
}
