package taskflow.logger;

import java.util.function.Consumer;

public class ConsoleLogger extends TFLogger{
	@Override
	protected Consumer<String> getLogPrinter() {
		return System.out::println;
	}
}
