package taskflow.config.bean;

import java.util.Set;

public class TaskflowConfiguration {
	private Set<TaskBeanDefinition> taskBeanDefinitions;
	private Set<TaskDefinition> taskDefinitions;
	private Set<WorkDefinition> workDefinitions;
	public Set<TaskBeanDefinition> getTaskBeanDefinitions() {
		return taskBeanDefinitions;
	}
	public void setTaskBeanDefinitions(Set<TaskBeanDefinition> taskBeanDefinitions) {
		this.taskBeanDefinitions = taskBeanDefinitions;
	}
	public Set<TaskDefinition> getTaskDefinitions() {
		return taskDefinitions;
	}
	public void setTaskDefinitions(Set<TaskDefinition> taskDefinitions) {
		this.taskDefinitions = taskDefinitions;
	}
	public Set<WorkDefinition> getWorkDefinitions() {
		return workDefinitions;
	}
	public void setWorkDefinitions(Set<WorkDefinition> workDefinitions) {
		this.workDefinitions = workDefinitions;
	}
}
