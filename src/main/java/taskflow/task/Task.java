package taskflow.task;

import taskflow.work.context.WorkContext;

/**
 * 一个Task可以看成是一个单独的业务处理逻辑<br/>
 * 利用责任链模式，把一组Task的组合看成责任链中的一个节点，通过routing来确定下一个节点<br/>
 *  Created by lizhou on 2017/3/14/014. <br/>
 *  updated by fuli on 2018/05<br/>
 */
public interface Task {
	/**
	 * 执行业务逻辑<br/>
	 * 参数和执行结果均放入work的WorkContext
	 * 
	 * @param work
	 */
	void execute(WorkContext workContext);

	/**
	 * Task唯一性标识
	 * 
	 * @return
	 */
	default String getId() {
		return this.toString();
	}
}
