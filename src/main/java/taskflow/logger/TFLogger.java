package taskflow.logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import taskflow.constants.TFLogType;
import taskflow.enums.ConfigSource;

public abstract class TFLogger{
	static final int INIT_SIZE = 1000;
	private Map<String, String> registerInfo = new LinkedHashMap<>(INIT_SIZE);

	public void log(String content) {
		getLogPrinter().accept(content);
	}
	
	public void logRegister(ConfigSource configSource, TFLogType type, String id, String value) {
		registerInfo.remove(id);
		StringBuilder log=new StringBuilder("[TaskFlow] - Register from ");
		log.append(configSource);
		log.append(" , [TYPE] : ").append(type);
		log.append(" , [VALUE] : ").append(value);
		registerInfo.put(id, log.toString());
	}

	public void printRegister(boolean printDetail) {
		if (registerInfo.size() > 0) {
			String lineSeparator = System.getProperty("line.separator");
			StringBuilder logInfo = new StringBuilder();
			Map<String, String> temp = registerInfo;
			registerInfo = new LinkedHashMap<>(INIT_SIZE);
			if (printDetail) {
				temp.values().stream().forEach((x) -> {
					logInfo.append(x).append(lineSeparator);
				});
			} else {
				for (String id : temp.keySet()) {
					logInfo.append(StringUtils.substringBefore(temp.get(id), "[VALUE]")).append("[VALUE] : ")
							.append(id).append(lineSeparator);
				}
			}
			getLogPrinter().accept(logInfo.substring(0,logInfo.length()-lineSeparator.length()).toString());
			temp = null;
		}
	}
	protected abstract Consumer<String> getLogPrinter();
}
