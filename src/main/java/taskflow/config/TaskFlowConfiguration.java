package taskflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import taskflow.config.bean.TaskflowConfiguration;
import taskflow.constants.PropertyNameAndValue;

/**
 * TaskFlow配置器</p>
 * 使用方必须注入TaskflowConfiguration对象</p>
 * 注解装配优先级高于XML配置形式
 */
@Configuration
public class TaskFlowConfiguration {
	@Bean(initMethod="init")
	public TaskFlowBeanFactoryPostProcessor taskFlowBeanFactoryPostProcessor(TaskflowConfiguration taskflowConfiguration,Environment environment) {
		PropertyNameAndValue.setProperties(environment::getProperty);
		return new TaskFlowBeanFactoryPostProcessor(taskflowConfiguration);
	}
	@Bean(initMethod="init")
	public TaskFlowBeanReloadProcessor taskFlowBeanReloadProcessor() {
		return new TaskFlowBeanReloadProcessor();
	}
}
