package taskflow.work.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import taskflow.exception.TaskFlowException;
import taskflow.routing.DefaultRouting;
import taskflow.routing.PatternRoutingCondition;
import taskflow.routing.RoutingCondition;
import taskflow.task.DefaultTaskRoutingWrap;
import taskflow.task.Task;
import taskflow.task.TaskRoutingWrap;
import taskflow.work.CustomRouteWork;
import taskflow.work.Work;
import taskflow.work.builder.RoutingBuilder.Routing;
import taskflow.work.context.ExtraArgsHolder;

/**
 * CustomRouteWork构造器
 * 
 * @author bailey
 * @date 2022年3月21日
 */
public final class RouteAbleWorkBuilder extends WorkBuilder {
	private Map<String, Task> taskMap = null;
	private Map<String, List<Routing>> taskRouting = null;
	private CustomRouteWork work;
	private String startTaskId;
	private String endTaskId;
	private String lastAddedTaskId;
	RouteAbleWorkBuilder() {
		this.taskMap = new HashMap<>();
		taskRouting = new HashMap<>();
	}

	RouteAbleWorkBuilder(CustomRouteWork work) {
		this.work = work;
		this.taskMap = new HashMap<>();
		taskRouting = new HashMap<>();
	}
	
	public RouteAbleWorkBuilder setStart(String startTaskId) {
		this.startTaskId = startTaskId;
		return this;
	}
	
	public RouteAbleWorkBuilder setEnd(String endTaskId) {
		this.endTaskId = endTaskId;
		return this;
	}

	public RouteAbleWorkBuilder addTask(Task task) {
		taskMap.put(task.getId(), task);
		lastAddedTaskId = task.getId();
		return this;
	}
	public RouteAbleWorkBuilder addTask(Task task,String extra) {
		addExtra(task.getId(), extra);
		return addTask(task);
	}
	/**
	 * 给最后一个add的task添加routing
	 * @param routing
	 * @return
	 */
	public RouteAbleWorkBuilder putRouting(Routing routing) {
		if (lastAddedTaskId == null) {
			throw new TaskFlowException("add a task before putting routing.");
		}
		return putRouting(lastAddedTaskId, routing);
	}
	/**
	 * 给指定的task添加routing
	 * @param task
	 * @param routing
	 * @return
	 */
	public RouteAbleWorkBuilder putRouting(Task task,Routing routing) {
		return putRouting(task.getId(), routing);
	}
	public RouteAbleWorkBuilder putRouting(String taskId,Routing routing) {
		List<Routing> routings = taskRouting.get(taskId);
		if (routings == null) {
			routings = new ArrayList<>();
			taskRouting.put(taskId, routings);
		}
		//排除重复routing
		if (routing.isEffective() && !routings.contains(routing)) {
			routings.add(routing);
		}
		return this;
	}
	
	public Work build() {
		if (startTaskId == null || startTaskId.trim().equals("")) {
			throw new TaskFlowException("build Work error : the start task can not be empty!");
		}
		work = work == null ? new CustomRouteWork() : work;
		work.setStart(assembleRoutingWrap(startTaskId));
		if (endTaskId != null && !endTaskId.equals("")) {
			Task endTask = taskMap.get(endTaskId);
			if (endTask == null) {
				throw new TaskFlowException("build Work error : the end task named '" + endTaskId + "' not exist!");
			}
			work.setFinish(new DefaultTaskRoutingWrap(endTask));
		}
		if (taskRefExtraMap != null) {
			for (Entry<String, String> entry : taskRefExtraMap.entrySet()) {
				ExtraArgsHolder.putTaskExtra(entry.getKey(), entry.getValue());
			}
		}
		return postBuild(work);
	}
	private Map<String, DefaultTaskRoutingWrap> assembledRoutingWrap = new HashMap<>();

	private TaskRoutingWrap assembleRoutingWrap(String taskId) {
		DefaultTaskRoutingWrap routingWrap=assembledRoutingWrap.get(taskId);
		if (routingWrap == null) {
			Task task = taskMap.get(taskId);
			if (task == null) {
				throw new TaskFlowException("the task named '" + taskId + "' not exist!");
			}
			routingWrap = new DefaultTaskRoutingWrap(task);
			routingWrap.setName(taskId);
			assembledRoutingWrap.put(taskId, routingWrap);
			List<Routing> routings = taskRouting.get(taskId);
			if (routings != null) {
				routingWrap.setRouting(resolveRouting(taskId, routings));
			}
			
		}
		return routingWrap;
	}

	private DefaultRouting resolveRouting(String taskId, List<Routing> routings) {
		DefaultRouting routing = new DefaultRouting();
		List<RoutingCondition> routingConditions = new ArrayList<>();
		for (Routing r : routings) {
			PatternRoutingCondition rc = new PatternRoutingCondition();
			rc.setCondition(r.getKey());
			rc.setPattern(r.getPattern());
			//允许路由到当前Task以循环执行
			rc.setTaskRoutingWrap(assembleRoutingWrap(r.getToTask()));
			routingConditions.add(rc);
			
			ExtraArgsHolder.putTaskRoutingExtra(taskId, r.getToTask(), r.getExtra());
		}
		routing.setRoutingConditions(routingConditions);
		return routing;
	}
}
