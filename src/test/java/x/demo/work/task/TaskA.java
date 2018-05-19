package x.demo.work.task;

import taskflow.task.Task;
import taskflow.work.Work;

public class TaskA implements Task{

	@Override
	public void execute(Work work) {
		work.putContext("name","tony");
		System.out.println("TaskA's method execute be invoked!");
	}
}
