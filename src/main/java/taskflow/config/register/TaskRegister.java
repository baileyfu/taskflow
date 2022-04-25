package taskflow.config.register;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;

import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskDefinition.RouteDefinition;
import taskflow.config.bean.TaskDefinition.TaskWrapperDefinition;
import taskflow.constants.RoutingConditionPropName;
import taskflow.constants.TFLogType;
import taskflow.constants.TaskRoutingPropName;
import taskflow.routing.DefaultRouting;
import taskflow.routing.PatternRoutingCondition;
import taskflow.routing.match.PatternType;
import taskflow.task.TaskMethodInvoker;
import taskflow.task.routing.DefaultTaskRoutingWrap;
import taskflow.task.routing.ReflectedTaskRoutingWrap;
import taskflow.work.context.ExtraArgsHolder;
//后注册的Bean优先级高
public interface TaskRegister extends ConfigSourceAware{
	default BeanDefinition registerTask(BeanDefinitionRegistry registry, TaskDefinition taskDefinition) {
		RuntimeBeanReference taskRef = new RuntimeBeanReference(taskDefinition.getTaskBeanId());
		RootBeanDefinition taskRoutingWrapDefinition = new RootBeanDefinition();
		// 设置了自定义方法
		if (StringUtils.isNotBlank(taskDefinition.getMethod())) {
			taskRoutingWrapDefinition.setBeanClass(ReflectedTaskRoutingWrap.class);

			RootBeanDefinition taskMethodInvokerBeanDefinition = new RootBeanDefinition();
			taskMethodInvokerBeanDefinition.setBeanClass(TaskMethodInvoker.class);
			ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
			constructorArgumentValues.addIndexedArgumentValue(0, taskRef);
			constructorArgumentValues.addIndexedArgumentValue(1, taskDefinition.getMethod());
			taskMethodInvokerBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);
			taskRoutingWrapDefinition.getPropertyValues().add(TaskRoutingPropName.TASK_METHOD_INVOKER, taskMethodInvokerBeanDefinition);
		} else {
			/**
			 * 此时taskRef必须是Task接口
			 */
			taskRoutingWrapDefinition.setBeanClass(DefaultTaskRoutingWrap.class);
			ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
			constructorArgumentValues.addIndexedArgumentValue(0, taskRef);
			taskRoutingWrapDefinition.setConstructorArgumentValues(constructorArgumentValues);
		}
		doRegister(registry, taskRoutingWrapDefinition,taskDefinition.getRouteDefinitions(),taskDefinition.getTaskId());
		//保留task.extra参数
		ExtraArgsHolder.putTaskExtra(taskDefinition.getTaskId(), taskDefinition.getExtra());
		logRegister(TFLogType.TASK,taskDefinition.getTaskId(), taskDefinition.toString());
		return taskRoutingWrapDefinition;
	}

	default BeanDefinition registerTask(BeanDefinitionRegistry registry, TaskWrapperDefinition taskWrapperDefinition) {
		String taskId = taskWrapperDefinition.getTaskId();
		RootBeanDefinition taskRoutingWrapDefinition = TaskWrapperRegister.createBeanDefinition(registry, taskId, taskWrapperDefinition.getRefWork(), taskWrapperDefinition.getResultKey(),(i,c)->logRegister(TFLogType.TASK, i, c));
		doRegister(registry, taskRoutingWrapDefinition,taskWrapperDefinition.getRouteDefinitions(),taskId);
		
		logRegister(TFLogType.TASK, taskId, taskWrapperDefinition.toString());
		return taskRoutingWrapDefinition;
	}
	default void doRegister(BeanDefinitionRegistry registry,RootBeanDefinition taskRoutingWrapDefinition,Set<RouteDefinition> routeDefinitions,String taskId) {
		// 解析routing condition
		ManagedList<BeanDefinition> routingConditions = new ManagedList<>();
		if (routeDefinitions != null && routeDefinitions.size() > 0) {
			for(RouteDefinition routeDefinition:routeDefinitions) {
				String key = routeDefinition.getKey();
				String toTask = routeDefinition.getToTask();
				//有效的routing
				if (!StringUtils.isEmpty(key) || !StringUtils.isEmpty(toTask)) {
					RootBeanDefinition routingCondition = new RootBeanDefinition();
					// 使用Pattern匹配路由
					routingCondition.setBeanClass(PatternRoutingCondition.class);
					routingCondition.getPropertyValues().add(RoutingConditionPropName.CONDITION, key);
					routingCondition.getPropertyValues().add(RoutingConditionPropName.PATTERN, PatternType.valueOf(routeDefinition.getPattern()));
					routingCondition.getPropertyValues().add(RoutingConditionPropName.TASK_ROUTING_WRAP, new RuntimeBeanReference(toTask));
					routingConditions.add(routingCondition);
					
					//保留task.routing.extra参数
					ExtraArgsHolder.putTaskRoutingExtra(taskId, toTask, routeDefinition.getExtra());
				}
			}
		}
		RootBeanDefinition routing = new RootBeanDefinition();
		routing.setBeanClass(DefaultRouting.class);
		routing.getPropertyValues().add(TaskRoutingPropName.ROUTING_CONDITIONS, routingConditions);
		taskRoutingWrapDefinition.getPropertyValues().add(TaskRoutingPropName.ROUTING, routing);
		taskRoutingWrapDefinition.getPropertyValues().add(TaskRoutingPropName.NAME, taskId);
		
		if (registry.containsBeanDefinition(taskId)) 
			registry.removeBeanDefinition(taskId);
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskRoutingWrapDefinition, taskId), registry);
	}
}
