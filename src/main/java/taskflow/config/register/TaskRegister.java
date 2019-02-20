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
import taskflow.constants.TaskRoutingPropName;
import taskflow.routing.DefaultRouting;
import taskflow.routing.PatternRoutingCondition;
import taskflow.routing.match.PatternType;
import taskflow.task.DefaultTaskRoutingWrap;
import taskflow.task.ReflectedTaskRoutingWrap;
import taskflow.task.TaskMethodInvoker;

public interface TaskRegister {

	default BeanDefinition registerTask(BeanDefinitionRegistry registry, TaskDefinition taskDefinition) {
		// 解析ref属性，因为ref引用的也是一个TaskRoutingWrap,可能在这里还未注册
		// 因此使用RuntimeBeanReference
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
				routingCondition.getPropertyValues().add(RoutingConditionPropName.CONDITION, routeDefinition.getValue());
				routingCondition.getPropertyValues().add(RoutingConditionPropName.PATTERN, PatternType.valueOf(routeDefinition.getPatten()));
				routingCondition.getPropertyValues().add(RoutingConditionPropName.TASK_ROUTING_WRAP, new RuntimeBeanReference(routeDefinition.getToTask()));
				routingConditions.add(routingCondition);
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

		BeanDefinitionHolder holder = new BeanDefinitionHolder(taskRoutingWrapDefinition, taskDefinition.getTaskId());
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
		return taskRoutingWrapDefinition;
	}
	
}
