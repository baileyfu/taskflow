package demo.task;

import java.util.Date;

import taskflow.annotation.Taskparam;
import taskflow.work.Work;

public class TaskC  {

	public void method1(Work work) {
		System.out.println("TaskC's method1 be invoked!!! extra : "+work.getWorkContext().getRuntimeArgs()+"--->"+Thread.currentThread());
	}

	public void method2() {
		System.out.println("TaskC's method2 be invoked+++>"+Thread.currentThread());
	}
	
	public void method3(Work work, @Taskparam("name") String x, @Taskparam(required = false) Date birth) {
		System.out.println("TaskC's method3 be invoked !!! x : "+x+" , birth : "+birth+" , extra : "+work.getWorkContext().getRuntimeArgs()+" called by :"+Thread.currentThread());
		work.getWorkContext().setResult("RESULT");
	}

	public void methodEnd(Work work) {
		System.out.println("TaskC's methodEnd be invoked !!! birth : "+work.getContext("birth")+" called by :"+Thread.currentThread());
		work.getWorkContext().setResult("OVER");
	}

}
