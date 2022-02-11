package taskflow.task;

import taskflow.routing.Routing;
import taskflow.work.Work;
import taskflow.work.WorkAgent;

/**
 * 仅持有routing
 */
public abstract class AbstractTaskRoutingWrap implements TaskRoutingWrap {
	protected String name;
	private Routing routing;

	public void doTask(Work work) throws Exception {
		WorkAgent.callReceive(work, this);
		// 执行Task的业务方法
		invokeTaskMethod(work);
		if (routing != null) {
			TaskRoutingWrap next = routing.doRouting(work.getWorkContext());
			if (next != null) {
				next.doTask(work);
			}
		}
	}
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Routing getRouting() {
		return routing;
	}

	public void setRouting(Routing routing) {
		this.routing = routing;
	}

	/**
	 * 执行task的业务方法
	 * 
	 * @param work
	 */
	protected abstract void invokeTaskMethod(Work work)throws Exception;
}
