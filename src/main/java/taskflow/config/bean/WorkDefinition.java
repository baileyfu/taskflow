package taskflow.config.bean;

import java.util.ArrayList;

import taskflow.work.Work;

/**
 * Work定义
 */
public class WorkDefinition {
	private String workId;
	private String start;
	private String finish;
	private int maxTasks;
	private boolean record;
	private Class<? extends Work> workClazz;
	//支持同一Task执行多次
	private ArrayList<TaskRef> taskRefs;

	public WorkDefinition() {
		maxTasks = 10000;
		record = false;
	}
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getFinish() {
		return finish;
	}
	public void setFinish(String finish) {
		this.finish = finish;
	}
	public int getMaxTasks() {
		return maxTasks;
	}
	public void setMaxTasks(int maxTasks) {
		this.maxTasks = maxTasks < 1 ? 1 : maxTasks;
	}
	public boolean isRecord() {
		return record;
	}
	public void setRecord(boolean record) {
		this.record = record;
	}
	public Class<? extends Work> getWorkClazz() {
		return workClazz;
	}
	public void setWorkClazz(Class<? extends Work> workClazz) {
		this.workClazz = workClazz;
	}
	public ArrayList<TaskRef> getTaskRefs() {
		return taskRefs;
	}
	public void setTaskRefs(ArrayList<TaskRef> taskRefs) {
		this.taskRefs = taskRefs;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((workId == null) ? 0 : workId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkDefinition other = (WorkDefinition) obj;
		if (workId == null) {
			if (other.workId != null)
				return false;
		} else if (!workId.equals(other.workId))
			return false;
		return true;
	}
	public static class TaskRef{
		private String taskId;
		private String extra;
		public String getTaskId() {
			return taskId;
		}
		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}
		public String getExtra() {
			return extra;
		}
		public void setExtra(String extra) {
			this.extra = extra;
		}
	}
}
