package taskflow.config.register;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import taskflow.config.bean.TaskBeanDefinition;

//后注册的Bean优先级高
public interface TaskBeanRegister {
	default void registerTaskBean(BeanDefinitionRegistry registry, TaskBeanDefinition taskBeanDefinition) {
		RootBeanDefinition taskBean = new RootBeanDefinition();
		taskBean.setBeanClass(taskBeanDefinition.getBeanClazz());

		if (registry.containsBeanDefinition(taskBeanDefinition.getBeanId())) 
			registry.removeBeanDefinition(taskBeanDefinition.getBeanId());
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskBean, taskBeanDefinition.getBeanId()), registry);
	}
}
