package taskflow.config.register;

import taskflow.constants.PropertyNameAndValue;
import taskflow.constants.TFLogType;
import taskflow.enums.ConfigSource;
import taskflow.logger.ConsoleLogger;

public interface LogRegisterInfo {
	ConsoleLogger DEFAULT_LOGGER = new ConsoleLogger();
	default void logRegister(ConfigSource configSource, TFLogType type, String id, String value) {
		if (Boolean.getBoolean(PropertyNameAndValue.LOG_PRINTABLE)) {
			StringBuilder log = new StringBuilder("[TaskFlow] - Register from ");
			log.append(configSource);
			log.append(" , [TYPE] : ").append(type);
			log.append(" , [VALUE] : ");
			if (Boolean.getBoolean(PropertyNameAndValue.LOG_PRINT_DETAIL)) {
				log.append(value);
			} else {
				log.append(id);
			}
			DEFAULT_LOGGER.log(log.toString());
		}
	}

}
