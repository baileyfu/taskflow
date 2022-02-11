package taskflow.work.context;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class WorkContextAgent {

	public static void callReleaseLatch(PromiseMapWorkContext promiseMapWorkContext) {
		promiseMapWorkContext.releaseLatch();
	}
	
	public static HashMap<String, CountDownLatch> callGetParamsLatch(PromiseMapWorkContext promiseMapWorkContext) {
		return promiseMapWorkContext.getParamsLatch();
	}
	
}
