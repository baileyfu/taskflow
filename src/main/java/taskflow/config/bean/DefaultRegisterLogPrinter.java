package taskflow.config.bean;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

public class DefaultRegisterLogPrinter extends RegisterLogRecorder implements ApplicationListener<ContextRefreshedEvent>, Ordered{
	private boolean hasPrinted = false;
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!hasPrinted) {
			hasPrinted = true;
			printRegisterLog(true);
		}
	}

}
