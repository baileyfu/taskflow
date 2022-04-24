package demo.task;

import java.util.Date;

import taskflow.annotation.Taskparam;
import taskflow.work.context.WorkContext;

public class TaskC  {

	public void method1(WorkContext workContext) {
		System.out.println("TaskC's method1 be invoked!!! extra : "+workContext.getRuntimeArgs()+"--->"+Thread.currentThread());
	}

	public void method2() {
		System.out.println("TaskC's method2 be invoked+++>"+Thread.currentThread());
	}
	
	public void method3(WorkContext workContext, @Taskparam("name") String x, @Taskparam(required = false) Date birth) {
		System.out.println("TaskC's method3 be invoked !!! x : "+x+" , birth : "+birth+" , extra : "+workContext.getRuntimeArgs()+" called by :"+Thread.currentThread());
		System.out.println("TaskC's method3 get result from subWork is : 'subWork'=" + workContext.get("subWork")+" , 'subWorkResult'="+ workContext.get("subWorkResult"));
		workContext.setResult("RESULT from TaskC.method3()");
	}

	public void methodEnd(WorkContext workContext) {
		System.out.println("TaskC's methodEnd be invoked !!! birth : "+workContext.get("birth")+" called by :"+Thread.currentThread());
		workContext.setResult("OVER");
	}

}
