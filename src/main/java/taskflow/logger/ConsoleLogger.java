package taskflow.logger;

import java.util.function.Consumer;

public class ConsoleLogger extends TFLogger {
	private Consumer<String> logPrinter;

	public ConsoleLogger() {
		logPrinter = System.out::println;
	}

	@Override
	protected Consumer<String> getLogPrinter() {
		return logPrinter;
	}
}
