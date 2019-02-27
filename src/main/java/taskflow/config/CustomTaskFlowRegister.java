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
	
	protected void printRegisterLog(Boolean printDetail) {
		Map<String, RegisterLogger> rlMap = beanFactory.getBeansOfType(RegisterLogger.class);
		if (rlMap != null && rlMap.size() > 0) {
			rlMap.values().iterator().next().printLog(printDetail);
		}else {
			RegisterLogger.clear();
		}
	}
}
