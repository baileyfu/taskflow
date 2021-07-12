package taskflow.config;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;

import taskflow.config.register.RegisterLogger;
import taskflow.config.register.TaskBeanRegister;
import taskflow.config.register.TaskRegister;
import taskflow.config.register.WorkRegister;
import taskflow.enums.ConfigSource;

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

	private RegisterLogger registerLogger = null;
	public void init() {
		if (registerLogger == null) {
			Map<String, RegisterLogger> rlMap = beanFactory.getBeansOfType(RegisterLogger.class);
			if (rlMap != null && rlMap.size() > 0) {
				registerLogger = rlMap.values().iterator().next();
			}
		}
	}
	protected void printRegisterLog(Boolean printDetail) {
		if (registerLogger != null) {
			registerLogger.printLog(printDetail);
		}
		RegisterLogger.clear();
	}
	protected void directlyLog(String content) {
		if (registerLogger != null) {
			registerLogger.getLogPrinter().accept(content);
		}
	}
}
