package taskflow.config.bean;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultTaskExecutorFactory implements TaskExecutorFactory {
	// 默认最大线程数:4
	private static final int MAX_POOL_SIZE = 4;
	private ExecutorService executor;

	@Override
	public Executor getExecutor() {
		if (executor == null) {
			synchronized (TaskExecutorFactory.class) {
				if (executor == null) {
					executor = new ThreadPoolExecutor(0, MAX_POOL_SIZE, 60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
				}
			}
		}
		return executor;
	}

	@Override
	public void shutdown() {
		if (executor != null) {
			executor.shutdown();
			if (executor.isTerminated()) {
				executor = null;
			}
		}
	}
}
