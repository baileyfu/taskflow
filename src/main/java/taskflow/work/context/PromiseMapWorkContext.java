package taskflow.work.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import taskflow.exception.TaskFlowException;
import taskflow.work.SequentialRouteWork;

public class PromiseMapWorkContext extends MapWorkContext {
	private SequentialRouteWork currentWork;
	private HashMap<String, CountDownLatch> paramsLatch;
	public PromiseMapWorkContext(SequentialRouteWork work) {
		super(work.getClass());
		this.currentWork = work;
	}

	public PromiseMapWorkContext(SequentialRouteWork work, Map<String, String> taskRefExtraMap) {
		super(work.getClass(), taskRefExtraMap);
		this.currentWork = work;
	}

	@Override
	public void put(String key, Object value) {
		super.put(key, value);
		if (paramsLatch != null && paramsLatch.containsKey(key)) {
			paramsLatch.get(key).countDown();
		}
	}

	@Override
	public Object get(String parameterName) {
		//只有同步Task才等待,异步Task直接获取,参数不存在则直接报错
		if (!context.containsKey(parameterName) && !currentWork.isCurrentTaskAsync()) {
			if (paramsLatch == null) {
				paramsLatch = new HashMap<>();
			}
			CountDownLatch countDownLatch = paramsLatch.get(parameterName);
			if (countDownLatch == null) {
				countDownLatch = new CountDownLatch(1);
				paramsLatch.put(parameterName, countDownLatch);
			}
			try {
				countDownLatch.await();
				//理论上不存在同名参数
				paramsLatch.remove(parameterName);
			} catch (InterruptedException e) {
				throw new TaskFlowException(String.format("Error waiting to set parameters which is '%s' for Work '%s'!", parameterName,currentWork.getName()), e);
			}
		}
		return super.get(parameterName);
	}
	//释放所有latch以使阻塞线程继续
	//Work出错时调用
	void releaseLatch() {
		if (paramsLatch != null) {
			for (CountDownLatch countDownLatch : paramsLatch.values()) {
				countDownLatch.countDown();
			}
			paramsLatch.clear();
		}
	}

	HashMap<String, CountDownLatch> getParamsLatch() {
		return paramsLatch;
	}
}
