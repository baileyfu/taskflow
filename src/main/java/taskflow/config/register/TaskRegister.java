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
import taskflow.constants.RoutingConditionPropName;
import taskflow.constants.TFLogType;
import taskflow.constants.TaskRoutingPropName;
import taskflow.routing.DefaultRouting;
import taskflow.routing.PatternRoutingCondition;
import taskflow.routing.match.PatternType;
import taskflow.task.DefaultTaskRoutingWrap;
import taskflow.task.ReflectedTaskRoutingWrap;
import taskflow.task.TaskMethodInvoker;
import taskflow.work.context.ExtraArgsHolder;
//后注册的Bean优先级高
public interface TaskRegister extends ConfigSourceAware{

	default BeanDefinition registerTask(BeanDefinitionRegistry registry, TaskDefinition taskDefinition) {
		RuntimeBeanReference taskRef = new RuntimeBeanReference(taskDefinition.getTaskBeanId());
		RootBeanDefinition taskRoutingWrapDefinition = new RootBeanDefinition();
		taskRoutingWrapDefinition.getPropertyValues().add(TaskRoutingPropName.NAME, taskDefinition.getTaskId());
		// 解析routing condition
		ManagedList<BeanDefinition> routingConditions = new ManagedList<>();
		Set<RouteDefinition> routeDefinitions=taskDefinition.getRouteDefinitions();
		if(routeDefinitions!=null&&routeDefinitions.size()>0) {
			for(RouteDefinition routeDefinition:routeDefinitions) {
				RootBeanDefinition routingCondition = new RootBeanDefinition();
				// 使用Pattern匹配路由
				routingCondition.setBeanClass(PatternRoutingCondition.class);
				routingCondition.getPropertyValues().add(RoutingConditionPropName.CONDITION, routeDefinition.getKey());
				routingCondition.getPropertyValues().add(RoutingConditionPropName.PATTERN, PatternType.valueOf(routeDefinition.getPatten()));
				routingCondition.getPropertyValues().add(RoutingConditionPropName.TASK_ROUTING_WRAP, new RuntimeBeanReference(routeDefinition.getToTask()));
				routingConditions.add(routingCondition);
				//保留task.routing.extra参数
				ExtraArgsHolder.putTaskRoutingExtra(taskDefinition.getTaskId(), routeDefinition.getToTask(), routeDefinition.getExtra());
			}
		}
		RootBeanDefinition routing = new RootBeanDefinition();
		routing.setBeanClass(DefaultRouting.class);
		routing.getPropertyValues().add(TaskRoutingPropName.ROUTING_CONDITIONS, routingConditions);

		taskRoutingWrapDefinition.getPropertyValues().add(TaskRoutingPropName.ROUTING, routing);
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
			taskRoutingWrapDefinition.getPropertyValues().add(TaskRoutingPropName.TASK, taskRef);
		}
		if (registry.containsBeanDefinition(taskDefinition.getTaskId())) 
			registry.removeBeanDefinition(taskDefinition.getTaskId());
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskRoutingWrapDefinition, taskDefinition.getTaskId()), registry);
		
		//保留task.extra参数
		ExtraArgsHolder.putTaskExtra(taskDefinition.getTaskId(), taskDefinition.getExtra());
		
		RegisterLogger.log(getConfigSource(), TFLogType.TASK,taskDefinition.getTaskId(), taskDefinition.toString());
		return taskRoutingWrapDefinition;
	}
	
}
