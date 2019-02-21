package x.demo.work.task;

import java.util.Date;

import taskflow.annotation.Taskparam;
import taskflow.work.Work;

public class TaskC  {

	public void method1() {
		System.out.println("TaskC's method1 be invoked");
	}

	public void method2() {
		System.out.println("TaskC's method2 be invoked");
	}
	
	public void method3(Work work, @Taskparam("name") String x, @Taskparam(require = false) Date birth) {
		System.out.println("TaskC's method3 be invoked====>"+x+"-->"+birth+" , extra : "+work.getWorkContext().getRuntimeArgs());
		work.getWorkContext().setResult("RESULT");
	}

	public void methodEnd(Work work) {
		System.out.println("TaskC's methodEnd be invoked--->"+work.getContext("birth"));
		work.getWorkContext().setResult("OVER");
	}

}
