package taskflow.work;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import taskflow.context.WorkContext;
import taskflow.context.TaskTrace;
import taskflow.context.MapWorkContext;
import taskflow.exception.TaskFlowException;
import taskflow.task.TaskRoutingWrap;

/**
 * 一般Work
 */
public class DefaultWork implements Work {
	private Object start;
	private int maxPath;

	private Object exception;
	private Object finish;
	private WorkContext workContext;

	private List<TaskTrace> busPathRecords = new ArrayList<TaskTrace>();

	private int arriveStationNums;

	public DefaultWork() {
		workContext = new MapWorkContext();
	}

	public WorkContext run() {
		try {
			if (!(start instanceof TaskRoutingWrap)) {
				throw new IllegalArgumentException("task type error");
			}
			((TaskRoutingWrap) start).doTask(this);
			System.out.println(((TaskRoutingWrap) start).getName()+"---------------->"+((TaskRoutingWrap) start).getTask().getName());
		} catch (Exception e) {
			dealExcpetion(e);
		} finally {
			if (finish != null && finish instanceof TaskRoutingWrap) {
				((TaskRoutingWrap) finish).doTask(this);
			}
		}
		return workContext;
	}

	public void dealExcpetion(Exception e) {
		workContext.holderException(e);
		if (exception != null && exception instanceof TaskRoutingWrap) {
			((TaskRoutingWrap) exception).doTask(this);
		} else {
			System.err.println(e.getMessage());
		}
	}
	
	public void receive(TaskRoutingWrap stationRoutingWrap) throws Exception {
		if (maxPath <= arriveStationNums++) {
			throw new TaskFlowException("max path is:" + maxPath);
		}
		busPathRecords.add(new TaskTrace(stationRoutingWrap.getName(), JSON.toJSONString(getWorkContext())));
	}

	public WorkContext getWorkContext() {
		return workContext;
	}

	public Object getStart() {
		return start;
	}

	public void setStart(Object start) {
		this.start = start;
	}

	public Object getException() {
		return exception;
	}

	public void setException(Object exception) {
		this.exception = exception;
	}

	public Object getFinish() {
		return finish;
	}

	public void setFinish(Object finish) {
		this.finish = finish;
	}

	public int getArriveStationNums() {
		return arriveStationNums;
	}

	public void setArriveStationNums(int arriveStationNums) {
		this.arriveStationNums = arriveStationNums;
	}

	public int getMaxPath() {
		return maxPath;
	}

	public void setMaxPath(int maxPath) {
		this.maxPath = maxPath;
	}

	public void putContext(String key, Object value) {
		workContext.put(key, value);
	}

	public Object getContextValue(String key) {
		return workContext.getValue(key);
	}

	public void setWorkContext(WorkContext workContext) {
		this.workContext = workContext;
	}

	public void setRoutingKey(String key) {
		workContext.setRoutingKey(key);
	}
}
