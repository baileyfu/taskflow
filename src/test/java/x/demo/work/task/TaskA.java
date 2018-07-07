package x.demo.work.task;

import com.alibaba.fastjson.JSON;

import taskflow.task.Task;
import taskflow.work.SequentialRouteWork;
import taskflow.work.Work;

public class TaskA implements Task{

	@Override
	public void execute(Work work) {
		work.putContext("name","Jack");
		System.out.println("TaskA's method execute be invoked !!!");
		String extra=((SequentialRouteWork)work).getExtra();
		System.out.println(JSON.parseObject(extra).getString("x"));
	}
}
