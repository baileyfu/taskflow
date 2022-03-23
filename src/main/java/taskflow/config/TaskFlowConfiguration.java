package taskflow.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import taskflow.config.bean.TaskBeanDefinition;
import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskFlowPropertySetterBean;
import taskflow.config.bean.TaskflowConfiguration;
import taskflow.config.bean.WorkDefinition;

/**
 * TaskFlow配置器</p>
 * 使用方必须注入TaskflowConfiguration对象</p>
 * 注解装配优先级高于XML配置形式
 */
@Configuration
public class TaskFlowConfiguration {
	@Bean(initMethod = TaskFlowPropertySetterBean.NAME_OF_INIT_METHOD)
	public TaskFlowPropertySetterBean taskFlowPropertySetter() {
		return new TaskFlowPropertySetterBean();
	}
	@Bean(initMethod=TaskFlowBeanReloadProcessor.NAME_OF_INIT_METHOD)
	public TaskFlowBeanReloadProcessor taskFlowBeanReloadProcessor() {
		return new TaskFlowBeanReloadProcessor();
	}

	@Bean(initMethod=TaskFlowBeanFactoryPostProcessor.NAME_OF_INIT_METHOD)
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
