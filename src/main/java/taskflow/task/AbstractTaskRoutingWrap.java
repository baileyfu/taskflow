package taskflow.task;

import taskflow.routing.Routing;
import taskflow.work.Work;

/**
 * 仅持有routing
 */
public abstract class AbstractTaskRoutingWrap implements TaskRoutingWrap {
	private String name;
	private Routing routing;
	public AbstractTaskRoutingWrap(){
		this.name=this.getClass().getSimpleName();
	}
	public void doTask(Work work) {
		try {
			work.receive(this);
			//执行Task的业务方法
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
	public abstract void invokeTaskMethod(Work work)throws Exception;
}
