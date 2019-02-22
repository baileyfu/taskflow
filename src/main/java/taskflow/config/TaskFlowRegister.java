package taskflow.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;

import taskflow.config.register.TaskBeanRegister;
import taskflow.config.register.TaskRegister;
import taskflow.config.register.WorkRegister;

public class TaskFlowRegister implements BeanFactoryAware, TaskBeanRegister, TaskRegister, WorkRegister{
	protected DefaultListableBeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		Assert.isTrue(beanFactory instanceof DefaultListableBeanFactory,"the TaskFlow requires DefaultListableBeanFactory");
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

}
