package taskflow.logger;

import java.util.function.Consumer;

public abstract class TFLogger{
	private static String LOG_TAG = "[TaskFlow] - ";

	public void log(String content) {
		getLogPrinter().accept(LOG_TAG + content);
	}
	protected abstract Consumer<String> getLogPrinter();
}
