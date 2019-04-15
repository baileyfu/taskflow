package taskflow.config;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import taskflow.config.bean.TaskflowConfiguration;
import taskflow.config.bean.WorkDefinition;
import taskflow.constants.ConfigParams;

/**
 * TaskFlow配置器</p>
 * 使用方必须注入TaskflowConfiguration对象</p>
 * 注解装配优先级高于XML配置形式
 */
@Configuration
public class TaskFlowConfiguration {
	@Bean
	public TaskFlowBeanFactoryPostProcessor taskFlowBeanFactoryPostProcessor(TaskflowConfiguration taskflowConfiguration,ConfigurableEnvironment environment) {
		Boolean traceable = environment.getProperty(ConfigParams.WORK_TRACEABLE, Boolean.class, Boolean.FALSE);
		Set<WorkDefinition> workDefinitions=taskflowConfiguration.getWorkDefinitions();
		if (workDefinitions != null && workDefinitions.size() > 0) {
			for (WorkDefinition wd : workDefinitions) {
				if(wd.getTraceable()==null) {
					wd.setTraceable(traceable);
				}
			}
		}
		Boolean ignoreNoExists = environment.getProperty(ConfigParams.WORK_NO_EXISTS_IGNORABLE, Boolean.class, Boolean.FALSE);
		return new TaskFlowBeanFactoryPostProcessor(taskflowConfiguration,ignoreNoExists);
	}
	@Bean
	public TaskFlowBeanReloadProcessor taskFlowBeanReloadProcessor(ConfigurableEnvironment environment) {
		Boolean reloadable = environment.getProperty(ConfigParams.RELOAD_ENABLE, Boolean.class, Boolean.TRUE);
		return new TaskFlowBeanReloadProcessor(reloadable);
	}
}
