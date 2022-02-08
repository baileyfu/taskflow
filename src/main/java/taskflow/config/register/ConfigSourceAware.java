package taskflow.config.register;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import taskflow.constants.TFLogType;
import taskflow.enums.ConfigSource;

public interface ConfigSourceAware {
	Map<String, String> REGISTER_LOG = new LinkedHashMap<>(1000);
	Map<TFLogType, Integer> REGISTER_COUNT = new HashMap<>(4);

	ConfigSource getConfigSource();

	default void logRegister(TFLogType type, String id, String value) {
		REGISTER_LOG.remove(id);
		StringBuilder log = new StringBuilder("Register from ");
		log.append(getConfigSource());
		log.append(" , [TYPE] : ").append(type);
		log.append(" , [VALUE] : ");
		log.append(value);
		REGISTER_LOG.put(id, log.toString());
		Integer count = REGISTER_COUNT.get(type);
		REGISTER_COUNT.put(type, count == null ? 1 : count + 1);
	}
}
