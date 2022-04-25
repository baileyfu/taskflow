package demo;

import java.util.ArrayList;
import java.util.Arrays;

import demo.service.NumberService;
import demo.task.FindNumber;
import taskflow.constants.PropertyNameAndValue;
import taskflow.task.Task;
import taskflow.task.TaskWrapper;
import taskflow.work.Work;
import taskflow.work.builder.RouteAbleWorkBuilder;
import taskflow.work.builder.RoutingBuilder;
import taskflow.work.builder.SequentialWorkBuilder;
import taskflow.work.builder.WorkBuilder;
import taskflow.work.context.TaskTrace;

/**
 * 功能等同于classpath:task-config.xml
 * @author bailey
 * @date 2022年3月23日
 */
public class WorkBuilderDemoApplication {
	private NumberService numberService;
	private FindNumber findNumber;

	public WorkBuilderDemoApplication() {
		numberService = new NumberService();
		findNumber = new FindNumber();
		findNumber.setNumberService(numberService);
	}

	Work createSubWork() {
		SequentialWorkBuilder workBuilder = WorkBuilder.newSequentialInstance();
		return workBuilder.addTask((workContext) -> {
			int maxValue = workContext.get("maxValue");
			int minValue = workContext.get("minValue");
			if (minValue < 1)
				throw new RuntimeException();
			System.out.println("max:" + maxValue + " , min:" + minValue);
			int diff = findNumber.getDiff(maxValue, minValue, workContext);
			workContext.setResult(diff);
		},"{threshold:1}").build("getDiffWork");
	}
	Work createWork() {
		//现在有一个业务需求，需要做以下处理:
		//输入一个整型的list，找出最大值和最小值，如果最大值和最小值的差大于{threshold}跳转到“no”，否则跳转到“ok”；{threshold}通过配置extra指定
		//定义task
		Task findMaxTask = findNumber::findMax;
		Task findMinTask = findNumber::findMin;
		//将其它work包装为task
		Task getDiffTask = new TaskWrapper(createSubWork(),"diffResult");
		Task soutOutOkTask = findNumber::soutOutOk;
		Task soutOutNoTask = findNumber::soutOutNo;
		//定义work
		RouteAbleWorkBuilder workBuilder = WorkBuilder.newRouteableInstance(true);
		return workBuilder.addTask(findMaxTask).putRouting(findMaxTask,RoutingBuilder.newInstance().toTask(findMinTask.getId()).build())
				   .addTask(findMinTask).putRouting(RoutingBuilder.newInstance().toTask(getDiffTask.getId()).build())
				   .addTask(getDiffTask).putRouting(RoutingBuilder.newInstance().key("ok").toTask(soutOutOkTask.getId()).extra("HighPriority_OK").build())
				   						.putRouting(RoutingBuilder.newInstance().key("no").toTask(soutOutNoTask.getId()).extra("HighPriority_NO").build())
				   .addTask(soutOutOkTask)
				   .addTask(soutOutNoTask)
				   .setStart(findMaxTask.getId())
				   .addEnd((wc)->{System.out.println("endTask总是最后一个执行，且无论之前task是否抛异常都会执行!result of getDiffWork is "+wc.get("diffResult"));})
				   .build("myWork");
	}
	public static void main(String[] args) {
		System.setProperty(PropertyNameAndValue.WORK_TRACEABLE, "false");
		WorkBuilderDemoApplication wb = new WorkBuilderDemoApplication();
		Work work = wb.createWork();
		work.putContext("intList", Arrays.asList(5, 7, 1, 2, 1, 3, 4, 5, 6, 4));
		String result = work.run().getResult();
		System.out.println(result);
		ArrayList<TaskTrace> taskTraces = work.getTaskTraces();
		if (taskTraces != null) {
			for (TaskTrace tt : taskTraces) {
				System.out.println("testWork-[TRACE]" + tt);
			}
		}
	}
}
