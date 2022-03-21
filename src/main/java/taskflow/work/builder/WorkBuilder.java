package taskflow.work.builder;

import java.util.function.Supplier;

import taskflow.constants.PropertyNameAndValue;
import taskflow.work.SequentialRouteWork;

/**
 * Work构造器,编码方式创建work
 * @author bailey
 * @date 2022年3月21日
 */
public abstract class WorkBuilder {
	static {
		//初始化默认参数
		PropertyNameAndValue.init();
	}
	public static SequentialWorkBuilder newInstance() {
		return new SequentialWorkBuilder(new SequentialRouteWork(null));
	}

	public static SequentialWorkBuilder newInstance(Supplier<SequentialRouteWork> workSupplier) {
		return new SequentialWorkBuilder(workSupplier.get());
	}
}
