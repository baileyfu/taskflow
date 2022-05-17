package taskflow.work;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import taskflow.config.bean.TaskExecutorFactory;
import taskflow.task.routing.AbstractTaskRoutingWrap;
import taskflow.task.routing.TaskRoutingWrap;
import taskflow.work.context.AbstractWorkContext;
import taskflow.work.context.PromiseMapWorkContext;
import taskflow.work.context.WorkContext;
import taskflow.work.context.WorkContextAgent;

/**
 * 串行执行任务,由SerialWork指定任务路由顺序<br/>
 * 扩展时,为防止跟其它Map类型参数冲突,构造器参数中extraArgsMap必须放到最后<br/>
 * 支持异步执行task；但异步task中所需的入参必须是确定的，即不能由其它task传入，而同步task中的入参可以由异步/同步task传入，因为异步task若依赖外部传入参数容易引起死锁。
 * 
 * @author bailey.fu
 * @date 2018年5月17日
 * @version 1.0
 * @description
 */
public class SequentialRouteWork extends AbstractWork {
	private LinkedHashMap<String, TaskRoutingWrap> tasks;
	private TaskExecutorFactory taskExecutorFactory;
	private HashSet<String> asyncTasks;
	//不对executedAsyncTask的递增操作进行并发控制,主要因为“是否存在尚未执行的异步Task”本就是额外加的一个弱校验,其结果的有效性不对预期设计构成大的影响。
	private int executedAsyncTask;

	public SequentialRouteWork(Map<String,String> taskRefExtraMap) {
		super((work) -> new PromiseMapWorkContext((SequentialRouteWork)work, taskRefExtraMap));
	}
	public void appendTask(TaskRoutingWrap task) {
		if (task != null) {
			tasks = tasks == null ? new LinkedHashMap<>() : tasks;
			tasks.put(task.getName(), task);
		}
	}
	public void appendAsyncTask(TaskRoutingWrap task) {
		if (task != null) {
			tasks = tasks == null ? new LinkedHashMap<>() : tasks;
			tasks.put(task.getName(), task);
			if (asyncTasks == null) {
				asyncTasks = new HashSet<>();
			}
			asyncTasks.add(task.getName());
		}
	}
	@Override
	public WorkContext execute() {
		try {
			if (tasks != null && tasks.size() > 0) {
				for (TaskRoutingWrap task : tasks.values()) {
					if (asyncTasks != null && asyncTasks.contains(task.getName())) {
						executeAsynchronously(task);
					} else {
						executeSynchronously(task);
					}
				}
			}
		} catch (Exception e) {
			preDealException(e);
			dealExcpetion(e);
		}
		return workContext;
	}
	private void executeSynchronously(TaskRoutingWrap task) throws Exception{
		AbstractTaskRoutingWrap absTask = (AbstractTaskRoutingWrap) task;
		absTask.setRouting(null);
		task.doTask(this);
	}

	private void executeAsynchronously(TaskRoutingWrap task) throws Exception{
		//在定义task-ref时未指定async但在业务代码中手动调用了appendAsyncTask()方法,此时taskExecutorFactory为null
		Executor executor = taskExecutorFactory == null ? null : taskExecutorFactory.getExecutor();
		if (executor == null) {
			executeSynchronously(task);
			executedAsyncTask++;
		} else {
			executor.execute(() -> {
				try {
					executeSynchronously(task);
				} catch (Exception e) {
					preDealException(e);
					this.dealExcpetion(e);
				} finally {
					executedAsyncTask++;
				}
			});
		}
	}
	private void preDealException(Exception e) {
		WorkContextAgent.callReleaseLatch((PromiseMapWorkContext) workContext);
	}
	public void setTasks(LinkedHashMap<String, TaskRoutingWrap> tasks) {
		this.tasks = tasks;
	}
	public void setAsyncTasks(HashSet<String> asyncTasks) {
		this.asyncTasks = asyncTasks;
	}
	public void setTaskExecutorFactory(TaskExecutorFactory taskExecutorFactory) {
		this.taskExecutorFactory = taskExecutorFactory;
	}
	//当前执行的Task是否需要等待参数
	boolean isCurrentTaskNeedWait4Params() {
		//不存在异步Task或所有异步Task都已执行完成则无需等待
		if (asyncTasks == null || executedAsyncTask >= asyncTasks.size()) {
			return false;
		}
		//只有当前Task为同步时才需要等待
		return !asyncTasks.contains(((AbstractWorkContext) workContext).getCurrentTask());
	}
}
