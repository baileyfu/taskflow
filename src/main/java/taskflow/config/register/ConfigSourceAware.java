package taskflow.config.register;

import taskflow.enums.ConfigSource;

public interface ConfigSourceAware {
	ConfigSource getConfigSource();
}
