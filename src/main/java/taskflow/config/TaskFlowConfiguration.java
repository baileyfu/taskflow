package taskflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import taskflow.config.bean.TaskflowConfiguration;

/**
 * TaskFlow配置器</p>
 * 使用方必须注入TaskflowConfiguration对象
 */
@Configuration
public class TaskFlowConfiguration {
	@Bean
	public TaskFlowBeanFactoryPostProcessor taskFlowBeanFactoryPostProcessor(TaskflowConfiguration taskflowConfiguration,ConfigurableEnvironment environment) {
		return new TaskFlowBeanFactoryPostProcessor(taskflowConfiguration);
	}
}
