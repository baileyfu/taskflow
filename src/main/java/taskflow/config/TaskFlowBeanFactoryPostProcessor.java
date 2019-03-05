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
	public TaskFlowBeanFactoryPostProcessor(TaskflowConfiguration taskflowConfiguration) {
		this.taskflowConfiguration=taskflowConfiguration;
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
					registerTaskBean(beanFactory, taskBeanDefinition);
				}
				int registeredTask = 0;
				int registeredWork = 0;
				Set<TaskDefinition> taskDefinitions = taskflowConfiguration.getTaskDefinitions();
				if (taskDefinitions != null && taskDefinitions.size() > 0) {
					registeredTask = taskDefinitions.size();
					//注册Task
					for (TaskDefinition taskDefinition : taskDefinitions) {
						registerTask(beanFactory, taskDefinition);
						//创建Task实例
						beanFactory.getBean(taskDefinition.getTaskId());
					}
					Set<WorkDefinition> workDefinitions=taskflowConfiguration.getWorkDefinitions();
					if (workDefinitions != null && workDefinitions.size() > 0) {
						registeredWork = workDefinitions.size();
						//注册Work
						for (WorkDefinition workDefinition : workDefinitions) {
							registerWork(beanFactory, workDefinition);
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
