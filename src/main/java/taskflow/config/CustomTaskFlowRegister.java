package taskflow.config;

import taskflow.config.bean.RegisterLogRecorder;
import taskflow.config.register.TaskBeanRegister;
import taskflow.config.register.TaskRegister;
import taskflow.config.register.WorkRegister;
import taskflow.enums.ConfigSource;

public class CustomTaskFlowRegister extends RegisterLogRecorder implements TaskBeanRegister, TaskRegister, WorkRegister{
	@Override
	public ConfigSource getConfigSource() {
		return ConfigSource.CUS;
	}
}
