package demo.task;

import taskflow.work.context.WorkContext;

public class CalNumber {

	public void cal(WorkContext workContext) {
		System.out.println("sub work execute...");
	}
	
	public void someMethod() {
		System.out.println("someMethod eecute...");
	}
}
