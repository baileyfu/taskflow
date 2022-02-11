package taskflow.config.bean;

import java.util.concurrent.Executor;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public interface TaskExecutorFactory extends ApplicationListener<ContextClosedEvent>{
	/**
	 * 获取自定义线程池
	 * @return
	 */
	public Executor getExecutor();
	/**
	 * 销毁线程池以释放资源
	 */
	public void shutdown();
	default public void onApplicationEvent(ContextClosedEvent event) {
		shutdown();
	}
}
