package taskflow.config;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;

import taskflow.config.register.TaskBeanRegister;
import taskflow.config.register.TaskRegister;
import taskflow.config.register.WorkRegister;
import taskflow.constants.PropertyNameAndValue;
import taskflow.constants.TFLogType;
import taskflow.enums.ConfigSource;
import taskflow.logger.TFLogger;

public class CustomTaskFlowRegister implements BeanFactoryAware, TaskBeanRegister, TaskRegister, WorkRegister{
	protected DefaultListableBeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		Assert.isTrue(beanFactory instanceof DefaultListableBeanFactory,"the TaskFlow requires DefaultListableBeanFactory");
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

	@Override
	public ConfigSource getConfigSource() {
		return ConfigSource.CUS;
	}

	private TFLogger registerLogger = null;
	public void init() {
		if (registerLogger == null) {
			Map<String, TFLogger> rlMap = beanFactory.getBeansOfType(TFLogger.class);
			if (rlMap != null && rlMap.size() > 0) {
				registerLogger = rlMap.values().iterator().next();
			} else {
				registerLogger = DEFAULT_LOGGER;
			}
		}
	}

	protected void printRegisterLog(boolean printDetail) {
		if (registerLogger != null && Boolean.getBoolean(PropertyNameAndValue.LOG_PRINTABLE)) {
			registerLogger.printRegister(printDetail && Boolean.getBoolean(PropertyNameAndValue.LOG_PRINT_DETAIL));
		}
	}
	protected void directlyLog(String content) {
		if (registerLogger != null && Boolean.getBoolean(PropertyNameAndValue.LOG_PRINTABLE)) {
			registerLogger.log(content);
		}
	}

	@Override
	public void logRegister(ConfigSource configSource, TFLogType type, String id, String value) {
		if (registerLogger != null && Boolean.getBoolean(PropertyNameAndValue.LOG_PRINTABLE)) {
			registerLogger.logRegister(configSource, type, id, value);
		}
	}
}
