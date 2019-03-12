package taskflow.config.register;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import taskflow.constants.ConfigParams;
import taskflow.constants.TFLogType;
import taskflow.enums.ConfigSource;

public abstract class RegisterLogger{
	static final int INIT_SIZE = 1000;
	private static Map<String, String> REGISTER_LOG = new LinkedHashMap<>(INIT_SIZE);

	static void log(ConfigSource configSource, TFLogType type, String id, String value) {
		REGISTER_LOG.remove(id);
		StringBuilder log=new StringBuilder("Register from ");
		log.append(configSource);
		log.append(" , [TYPE] : ").append(type);
		log.append(" , [VALUE] : ").append(value);
		REGISTER_LOG.put(id, log.toString());
	}

	public static void clear() {
		REGISTER_LOG.clear();
	}
	@Autowired
	Environment environment;

	public synchronized void printLog(Boolean printDetail) {
		if (environment.getProperty(ConfigParams.LOG_PRINTABLE, Boolean.class, Boolean.FALSE)) {
			if (REGISTER_LOG.size() > 0) {
				String lineSeparator = System.getProperty("line.separator");
				StringBuilder logInfo = new StringBuilder();
				logInfo.append(lineSeparator);
				Map<String, String> temp = REGISTER_LOG;
				REGISTER_LOG = new LinkedHashMap<>(INIT_SIZE);
				if(printDetail==null?environment.getProperty(ConfigParams.LOG_PRINT_DETAIL, Boolean.class, Boolean.FALSE):printDetail) {
					temp.values().stream().forEach((x)->{
						logInfo.append(x).append(lineSeparator);
					});
				} else {
					for (String id : temp.keySet()) {
						logInfo.append(StringUtils.substringBefore(temp.get(id), "[VALUE]")).append("[VALUE] : ").append(id).append(lineSeparator);
					}
				}
				getLogPrinter().accept(logInfo.substring(0,logInfo.length()-lineSeparator.length()).toString());
				temp = null;
			}
		}
	}
	public abstract Consumer<String> getLogPrinter();
}