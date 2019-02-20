package taskflow.config;

import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import taskflow.config.bean.TaskBeanDefinition;
import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskflowConfiguration;
import taskflow.config.bean.WorkDefinition;
import taskflow.config.register.TaskBeanRegister;
import taskflow.config.register.TaskRegister;
import taskflow.config.register.WorkRegister;
import taskflow.work.WorkFactory;

/**
 * 容器初始化完成后注册TaskFlow的Bean
 */
public class TaskFlowBeanFactoryPostProcessor implements BeanFactoryAware,
		ApplicationListener<ContextRefreshedEvent>, Ordered, TaskBeanRegister, TaskRegister, WorkRegister {
	private DefaultListableBeanFactory beanFactory;
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
				Set<TaskDefinition> taskDefinitions = taskflowConfiguration.getTaskDefinitions();
				if (taskDefinitions != null && taskDefinitions.size() > 0) {
					Set<WorkDefinition> workDefinitions=taskflowConfiguration.getWorkDefinitions();
					if (workDefinitions != null && workDefinitions.size() > 0) {
						//注册TaskBean
						for (TaskBeanDefinition taskBeanDefinition : taskBeanDefinitions) {
							registerTaskBean(beanFactory, taskBeanDefinition);
						}
						//注册Task
						for (TaskDefinition taskDefinition : taskDefinitions) {
							registerTask(beanFactory, taskDefinition);
							//创建Task实例
							beanFactory.getBean(taskDefinition.getTaskId());
						}
						//注册Work
						for (WorkDefinition workDefinition : workDefinitions) {
							registerWork(beanFactory, workDefinition);
						}
						//注册WorkFactory
						registerWorkFactory(beanFactory);
						beanFactory.getBean(WorkFactory.class);
					}
				}
			}
		}
	}
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		Assert.isTrue(beanFactory instanceof DefaultListableBeanFactory,"the TaskFlow requires DefaultListableBeanFactory");
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
}
