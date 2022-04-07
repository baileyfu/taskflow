package taskflow.config.register;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import taskflow.constants.TaskRoutingPropName;
import taskflow.task.TaskWrapper;
import taskflow.task.routing.DefaultTaskRoutingWrap;

/**
 * 目前TaskWrapper仅包装Work
 * 
 * @author bailey
 * @date 2022年4月7日
 */
public class TaskWrapperRegister {
	private static String SUFFIX = "$TaskWrapper";
	private static String ROUTING_SUFFIX = "$TaskWrapper$$RoutingWrapper";

	/**
	 * 注册TaskWrapper
	 * 
	 * @param registry
	 * @param workId 
	 * @return taskRoutingWrapperId
	 */
	public static String register(BeanDefinitionRegistry registry, String workId) {
		String taskRoutingWrapperId = workId + ROUTING_SUFFIX;
		if (!registry.containsBeanDefinition(taskRoutingWrapperId)) {
			String taskWrapperId = workId + SUFFIX;
			RootBeanDefinition taskWrapperDefinition = new RootBeanDefinition();
			taskWrapperDefinition.setBeanClass(TaskWrapper.class);
			ConstructorArgumentValues taskWrapperConstructorArgumentValues = new ConstructorArgumentValues();
			taskWrapperConstructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(workId));
			taskWrapperDefinition.setConstructorArgumentValues(taskWrapperConstructorArgumentValues);
			BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskWrapperDefinition, taskWrapperId), registry);
		
			RootBeanDefinition taskRoutingWrapDefinition = new RootBeanDefinition();
			taskRoutingWrapDefinition.getPropertyValues().add(TaskRoutingPropName.NAME, taskRoutingWrapperId);
			taskRoutingWrapDefinition.setBeanClass(DefaultTaskRoutingWrap.class);
			ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
			constructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(taskWrapperId));
			taskRoutingWrapDefinition.setConstructorArgumentValues(constructorArgumentValues);
			BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskRoutingWrapDefinition, taskRoutingWrapperId), registry);
		}
		return taskRoutingWrapperId;
	}
}
