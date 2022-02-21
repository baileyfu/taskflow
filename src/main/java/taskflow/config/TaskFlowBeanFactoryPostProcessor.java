package taskflow.config;

import java.util.Set;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

import taskflow.config.bean.TaskBeanDefinition;
import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskflowConfiguration;
import taskflow.config.bean.WorkDefinition;
import taskflow.constants.PropertyNameAndValue;
import taskflow.work.WorkFactory;

/**
 * 容器初始化完成后注册TaskFlow的Bean
 */
public class TaskFlowBeanFactoryPostProcessor extends CustomTaskFlowRegister implements ApplicationListener<ContextRefreshedEvent>, Ordered {
	private TaskflowConfiguration taskflowConfiguration;
	public TaskFlowBeanFactoryPostProcessor(TaskflowConfiguration taskflowConfiguration) {
		this.taskflowConfiguration = taskflowConfiguration;
	}

	private boolean initialized = false;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!initialized && taskflowConfiguration != null) {
			initialized = true;
			boolean ignoreNoExists = Boolean.getBoolean(PropertyNameAndValue.WORK_NO_EXISTS_IGNORABLE);
			Set<TaskBeanDefinition> taskBeanDefinitions = taskflowConfiguration.getTaskBeanDefinitions();
			if (!CollectionUtils.isEmpty(taskBeanDefinitions)) {
				//注册TaskBean
				for (TaskBeanDefinition taskBeanDefinition : taskBeanDefinitions) {
					try {
						registerTaskBean(beanFactory, taskBeanDefinition);
					} catch (Exception e) {
						if (ignoreNoExists) {
							directlyLog("Register TaskBean '"+taskBeanDefinition.getBeanId()+"' error ! ex : "+e.toString());
						} else {
							throw e;
						}
					}
				}
				Set<TaskDefinition> taskDefinitions = taskflowConfiguration.getTaskDefinitions();
				if (!CollectionUtils.isEmpty(taskDefinitions)) {
					//注册Task
					for (TaskDefinition taskDefinition : taskDefinitions) {
						try {
							registerTask(beanFactory, taskDefinition);
							//创建Task实例
							beanFactory.getBean(taskDefinition.getTaskId());
						}catch(Exception e) {
							if (ignoreNoExists) {
								directlyLog("Register Task '"+taskDefinition.getTaskId()+"' error ! ex : "+e.toString());
							} else {
								throw e;
							}
						}
					}
					Set<WorkDefinition> workDefinitions = taskflowConfiguration.getWorkDefinitions();
					if (!CollectionUtils.isEmpty(workDefinitions)) {
						// 注册Work
						for (WorkDefinition workDefinition : workDefinitions) {
							try {
								registerWork(beanFactory, workDefinition);
							}catch(Exception e) {
								if (ignoreNoExists) {
									directlyLog("Register Work '"+workDefinition.getWorkId()+"' error ! ex : "+e.toString());
								} else {
									throw e;
								}
							}
						}
					}
				}
				//注册WorkFactory
				registerWorkFactory(beanFactory);
				beanFactory.getBean(WorkFactory.class);
			}
			// 打印注册日志
			printRegisterLog(true);
		}
	}
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 1;
	}
}
