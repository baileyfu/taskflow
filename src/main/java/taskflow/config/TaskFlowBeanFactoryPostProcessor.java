package taskflow.config;

import java.util.Set;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import taskflow.config.bean.TaskBeanDefinition;
import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskflowConfiguration;
import taskflow.config.bean.WorkDefinition;
import taskflow.work.WorkFactory;

/**
 * 容器初始化完成后注册TaskFlow的Bean
 */
public class TaskFlowBeanFactoryPostProcessor extends CustomTaskFlowRegister implements ApplicationListener<ContextRefreshedEvent>, Ordered {
	private TaskflowConfiguration taskflowConfiguration;
	private boolean ignoreNoExists;
	public TaskFlowBeanFactoryPostProcessor(TaskflowConfiguration taskflowConfiguration,boolean ignoreNoExists) {
		this.taskflowConfiguration=taskflowConfiguration;
		this.ignoreNoExists = ignoreNoExists;
	}

	private boolean initialized = false;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!initialized) {
			initialized = true;
			Set<TaskBeanDefinition> taskBeanDefinitions = taskflowConfiguration.getTaskBeanDefinitions();
			if (taskBeanDefinitions != null && taskBeanDefinitions.size() > 0) {
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
				int registeredTask = 0;
				int registeredWork = 0;
				Set<TaskDefinition> taskDefinitions = taskflowConfiguration.getTaskDefinitions();
				if (taskDefinitions != null && taskDefinitions.size() > 0) {
					registeredTask = taskDefinitions.size();
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
					Set<WorkDefinition> workDefinitions=taskflowConfiguration.getWorkDefinitions();
					if (workDefinitions != null && workDefinitions.size() > 0) {
						registeredWork = workDefinitions.size();
						//注册Work
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
				
				// 打印注册日志
				directlyLog("Register List : ");
				printRegisterLog(null);
				directlyLog("Registered Total : [TaskBean : "+taskBeanDefinitions.size()+" , Task : "+registeredTask+" , Work : "+registeredWork+"]");
				directlyLog("The 'Register List' of TaskFlow has been printed!");
			}
		}
	}
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
