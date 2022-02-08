package taskflow.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import taskflow.config.bean.TaskBeanDefinition;
import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskflowConfiguration;
import taskflow.config.bean.WorkDefinition;
import taskflow.constants.PropertyNameAndValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * TaskFlow配置器</p>
 * 使用方必须注入TaskflowConfiguration对象</p>
 * 注解装配优先级高于XML配置形式
 */
@Configuration
public class TaskFlowConfiguration {
	@Bean(initMethod="init")
	public TaskFlowBeanReloadProcessor taskFlowBeanReloadProcessor(Environment environment) {
		PropertyNameAndValue.setProperties(environment::getProperty);
		return new TaskFlowBeanReloadProcessor();
	}

	@Bean(initMethod="init")
	public TaskFlowBeanFactoryPostProcessor taskFlowBeanFactoryPostProcessor(ApplicationContext applicationContext) {
		Set<TaskBeanDefinition> taskBeanDefinitions = new HashSet<>();
		Set<TaskDefinition> taskDefinitions = new HashSet<>();
		Set<WorkDefinition> workDefinitions = new HashSet<>();
		
		taskBeanDefinitions.addAll(applicationContext.getBeansOfType(TaskBeanDefinition.class).values());
		taskDefinitions.addAll(applicationContext.getBeansOfType(TaskDefinition.class).values());
		workDefinitions.addAll(applicationContext.getBeansOfType(WorkDefinition.class).values());
		
		for (Collection<?> defines : applicationContext.getBeansOfType(Collection.class).values()) {
			for (Object define : defines) {
				if (define instanceof TaskBeanDefinition) {
					taskBeanDefinitions.add((TaskBeanDefinition) define);
				} else if (define instanceof TaskDefinition) {
					taskDefinitions.add((TaskDefinition) define);
				} else if (define instanceof WorkDefinition) {
					workDefinitions.add((WorkDefinition) define);
				}
			}
		}
		
		for (TaskflowConfiguration tc : applicationContext.getBeansOfType(TaskflowConfiguration.class).values()) {
			taskBeanDefinitions.addAll(tc.getTaskBeanDefinitions());
			taskDefinitions.addAll(tc.getTaskDefinitions());
			workDefinitions.addAll(tc.getWorkDefinitions());
		}
		
		TaskflowConfiguration taskflowConfiguration = new TaskflowConfiguration();
		taskflowConfiguration.setTaskBeanDefinitions(taskBeanDefinitions);
		taskflowConfiguration.setTaskDefinitions(taskDefinitions);
		taskflowConfiguration.setWorkDefinitions(workDefinitions);
		return new TaskFlowBeanFactoryPostProcessor(taskflowConfiguration);
	}
}
