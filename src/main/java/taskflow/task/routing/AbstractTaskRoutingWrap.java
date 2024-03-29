package taskflow.task.routing;

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
		// 将key重置为null,防止前一个task设置的key对当前task的执行造成影响
		work.getWorkContext().setRoutingKey(null);
		// 执行Task的业务方法
		invokeTaskMethod(work);
		if (routing != null) {
			//当仅设置了key而未设置toTask，或toTask不存在时，此处next为null
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
