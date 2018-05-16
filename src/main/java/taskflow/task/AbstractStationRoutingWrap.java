package taskflow.task;

import taskflow.routing.Routing;
import taskflow.work.Work;

/**
 * 仅持有routing
 */
public abstract class AbstractStationRoutingWrap implements TaskRoutingWrap {
	private Routing routing;

	public void doTask(Work work) {
		try {
			work.receive(this);
			invokeTaskMethod(work);
		} catch (Exception e) {
			work.dealExcpetion(e);
		}
		if (routing != null) {
			TaskRoutingWrap next = routing.doRouting(work.getWorkContext());
			if (next != null) {
				next.doTask(work);
			}
		}
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
	abstract void invokeTaskMethod(Work work)throws Exception;
}
