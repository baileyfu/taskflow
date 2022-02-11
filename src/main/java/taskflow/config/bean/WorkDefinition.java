package taskflow.config.bean;

import java.util.ArrayList;

/**
 * Work定义
 */
public class WorkDefinition {
	private String workId;
	private String start;
	private String finish;
	private int maxTasks;
	private boolean traceable;
	private String workClazz;
	private ArrayList<ConstructorArg> constructorArgs;
	//支持同一Task执行多次
	private ArrayList<TaskRef> taskRefs;

	public WorkDefinition() {
		maxTasks = 0;
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
		this.maxTasks = maxTasks;
	}
	public boolean getTraceable() {
		return traceable;
	}
	public void setTraceable(boolean traceable) {
		this.traceable = traceable;
	}
	public String getWorkClazz() {
		return workClazz;
	}
	public void setWorkClazz(String workClazz) {
		this.workClazz = workClazz;
	}
	public ArrayList<TaskRef> getTaskRefs() {
		return taskRefs;
	}
	public void setTaskRefs(ArrayList<TaskRef> taskRefs) {
		this.taskRefs = taskRefs;
	}
	public ArrayList<ConstructorArg> getConstructorArgs() {
		return constructorArgs;
	}
	public void setConstructorArgs(ArrayList<ConstructorArg> constructorArgs) {
		this.constructorArgs = constructorArgs;
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
	@Override
	public String toString() {
		return "WorkDefinition [workId=" + workId + ", start=" + start + ", finish=" + finish + ", maxTasks=" + maxTasks
				+ ", traceable=" + traceable + ", workClazz=" + workClazz + ", constructorArgs=" + constructorArgs
				+ ", taskRefs=" + taskRefs + "]";
	}
	public static class ConstructorArg{
		private int index;
		private String type;
		private String name;
		private String ref;
		private String value;
		public ConstructorArg() {
			index = -1;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getRef() {
			return ref;
		}
		public void setRef(String ref) {
			this.ref = ref;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	public static class TaskRef{
		private String taskId;
		private String extra;
		private boolean async;
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
		public boolean isAsync() {
			return async;
		}
		public void setAsync(boolean async) {
			this.async = async;
		}
		@Override
		public String toString() {
			return "TaskRef [taskId=" + taskId + ", extra=" + extra + ", async=" + async + "]";
		}
	}
}
