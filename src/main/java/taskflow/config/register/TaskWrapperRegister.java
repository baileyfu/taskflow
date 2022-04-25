package taskflow.config.register;

import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import taskflow.config.bean.WorkDefinition.WorkRef;
import taskflow.constants.TaskRoutingPropName;
import taskflow.task.TaskWrapper;
import taskflow.task.routing.DefaultTaskRoutingWrap;

public class TaskWrapperRegister {
	private static String SUFFIX = "$TaskWrapper";
	private static String ROUTING_SUFFIX = SUFFIX + "$$RoutingWrapper";

	public static RootBeanDefinition createBeanDefinition(BeanDefinitionRegistry registry, String taskId, String refWorkId,String resultKey,BiConsumer<String,String> logger) {
		RootBeanDefinition taskRoutingWrapDefinition = null;
		if(registry.containsBeanDefinition(taskId)) {
			taskRoutingWrapDefinition = (RootBeanDefinition)registry.getBeanDefinition(taskId);
		}
		if (taskRoutingWrapDefinition == null) {
			//先注册TaskWrapper
			String taskWrapperId = refWorkId + SUFFIX;
			RootBeanDefinition taskWrapperDefinition = new RootBeanDefinition();
			taskWrapperDefinition.setBeanClass(TaskWrapper.class);
			ConstructorArgumentValues taskWrapperConstructorArgumentValues = new ConstructorArgumentValues();
			taskWrapperConstructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(refWorkId));
			if (StringUtils.isNotEmpty(resultKey)) {
				taskWrapperConstructorArgumentValues.addIndexedArgumentValue(1, resultKey);
			}
			taskWrapperDefinition.setConstructorArgumentValues(taskWrapperConstructorArgumentValues);
			BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskWrapperDefinition, taskWrapperId), registry);
			logger.accept(taskWrapperId, taskWrapperId);
			//再定义RoutingWrap
			taskRoutingWrapDefinition = new RootBeanDefinition();
			taskRoutingWrapDefinition.setBeanClass(DefaultTaskRoutingWrap.class);
			ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
			constructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(taskWrapperId));
			taskRoutingWrapDefinition.setConstructorArgumentValues(constructorArgumentValues);
		}
		return taskRoutingWrapDefinition;
	}
	
	/**
	 * 注册TaskWrapper
	 * @param registry
	 * @param workRef
	 * @return taskRoutingWrapperId
	 */
	public static String register(BeanDefinitionRegistry registry, WorkRef workRef,BiConsumer<String,String> logger) {
		String taskRoutingWrapperId = workRef.getRefWork() + ROUTING_SUFFIX;
		RootBeanDefinition taskRoutingWrapDefinition = createBeanDefinition(registry, taskRoutingWrapperId,workRef.getRefWork(),workRef.getResultKey(),logger);
		taskRoutingWrapDefinition.getPropertyValues().add(TaskRoutingPropName.NAME, taskRoutingWrapperId);
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskRoutingWrapDefinition, taskRoutingWrapperId), registry);
		logger.accept(taskRoutingWrapperId,workRef.toString());
		workRef.setTaskId(taskRoutingWrapperId);
		return taskRoutingWrapperId;
	}
}
