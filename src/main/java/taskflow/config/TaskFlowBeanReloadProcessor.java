package taskflow.config;

import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import taskflow.config.bean.TaskBeanDefinition;
import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskDefinition.RouteDefinition;
import taskflow.config.bean.WorkDefinition;
import taskflow.config.bean.WorkDefinition.TaskRef;

/**
 * 运行时重载TaskFlow的bean
 */
public class TaskFlowBeanReloadProcessor extends CustomTaskFlowRegister {
	private boolean reloadable;

	public TaskFlowBeanReloadProcessor(boolean reloadable) {
		this.reloadable = reloadable;
	}

	public void reload(TaskBeanDefinition taskBeanDefinition,TaskDefinition taskDefinition,WorkDefinition workDefinition) {
		Assert.isTrue(reloadable, "TaskFlow be set to not reloadable!");
		if (taskBeanDefinition != null)
			registerTaskBean(beanFactory, taskBeanDefinition);
		
		if(taskDefinition!=null) {
			Assert.isTrue(beanFactory.containsBeanDefinition(taskDefinition.getTaskBeanId()),"the TaskBean of "+taskDefinition.getTaskBeanId()+" of depended by Task of "+taskDefinition.getTaskId()+" is not found!");
			Set<RouteDefinition> routeDefinitions=taskDefinition.getRouteDefinitions();
			if (routeDefinitions != null && routeDefinitions.size() > 0) {
				//TODO assert if route task has been registered
			}
			registerTask(beanFactory, taskDefinition);
		}
		
		if (workDefinition != null) {
			if (!StringUtils.isBlank(workDefinition.getStart())) {
				Assert.isTrue(beanFactory.containsBeanDefinition(workDefinition.getStart()), "the Work of "+workDefinition.getWorkId()+" requires Start Task named "+workDefinition.getStart()+" , but it is not found!");
			}
			if (!StringUtils.isBlank(workDefinition.getFinish())) {
				Assert.isTrue(beanFactory.containsBeanDefinition(workDefinition.getFinish()), "the Work of "+workDefinition.getWorkId()+" requires Finish Task named "+workDefinition.getFinish()+" , but it is not found!");
			}
			ArrayList<TaskRef> taskRefs = workDefinition.getTaskRefs();
			if (taskRefs != null && taskRefs.size() > 0) {
				for (TaskRef taskRef : taskRefs) {
					Assert.isTrue(beanFactory.containsBeanDefinition(taskRef.getTaskId()),"the Work of "+workDefinition.getWorkId()+" requires Task of named "+taskRef.getTaskId()+" , but it is not found!");
				}
			}
			registerWork(beanFactory, workDefinition);
		}
		// 打印注册日志
		printRegisterLog(true);
	};
}
