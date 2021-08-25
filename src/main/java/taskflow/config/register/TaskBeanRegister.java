package taskflow.config.register;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import taskflow.config.bean.TaskBeanDefinition;
import taskflow.constants.TFLogType;

//后注册的Bean优先级高
public interface TaskBeanRegister extends ConfigSourceAware{
	default void registerTaskBean(BeanDefinitionRegistry registry, TaskBeanDefinition taskBeanDefinition) {
		Class<?> beanClazz = null;
		try {
			beanClazz = Class.forName(taskBeanDefinition.getBeanClazz());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		RootBeanDefinition taskBean = new RootBeanDefinition();
		taskBean.setBeanClass(beanClazz);

		if (registry.containsBeanDefinition(taskBeanDefinition.getBeanId())) 
			registry.removeBeanDefinition(taskBeanDefinition.getBeanId());
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskBean, taskBeanDefinition.getBeanId()), registry);
	
		logRegister(getConfigSource(), TFLogType.TASK_BEAN,taskBeanDefinition.getBeanId(), taskBeanDefinition.toString());
	}
}
