package demo;

import taskflow.config.bean.DefaultTaskExecutorFactory;
import taskflow.task.Task;
import taskflow.work.Work;
import taskflow.work.builder.RouteAbleWorkBuilder;
import taskflow.work.builder.RoutingBuilder;
import taskflow.work.builder.SequentialWorkBuilder;
import taskflow.work.builder.WorkBuilder;

/**
 * 编程式创建Work
 * @author bailey
 * @date 2022年3月21日
 */
public class WorkBuilderApplication {
	static Task A,B,C,D,ASYNC;
	static int i = 1;
	static void init() {
		A = (workContext) -> {
			System.out.println("A--" + i + "->" + workContext.getRuntimeArgs());
			workContext.setRoutingKey("toB");
		};
		B = (workContext) -> {
			System.out.println("B--" + i + "->" + workContext.getRuntimeArgs());
			if (i++ < 3) {
				workContext.setRoutingKey("toA");
			} else if (i < 8) {
				workContext.setRoutingKey("toB");
			} else if ("needD".equals(workContext.getRuntimeArgs())) {
				workContext.setRoutingKey("toD");
			}
		};
		C = (workContext)->{
			workContext.setResult("Result="+workContext.get("bewait"));
		};
		D = (workContext) -> {
			System.out.println("D--->" + workContext.getRuntimeArgs());
		};
		ASYNC = (workContext)->{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			workContext.put("bewait","from async");
			System.out.println("ASYNC--000===>"+workContext.getRuntimeArgs());
		};
	}
	//执行序列：A -> B 			-> C(依赖ASNC的参数,等待其完成后才能执行)
	//          -> ASYNC(并发)
	static void runSequential() {
		DefaultTaskExecutorFactory defaultTaskExecutorFactory = new DefaultTaskExecutorFactory();
		SequentialWorkBuilder workBuilder = WorkBuilder.newSequentialInstance();
		Work work = workBuilder.addAsyncTask(ASYNC, "palapala").addTask(A, "aaa").addTask(B, "needD").addTask(D,"ddd").addTask(C).build(defaultTaskExecutorFactory);
		work.putContext("","");
		String result = work.run().getResult();
		System.out.println(result);
		defaultTaskExecutorFactory.shutdown();
	}
	//执行序列：(A -> B){循环执行3次} -------------------------> C
	//                             -> D(根据extra决定是否执行D)
	static void runRouteable() {
		RouteAbleWorkBuilder workBuilder = WorkBuilder.newRouteableInstance();
		Work work = workBuilder.addTask(A,"a").putRouting(A, RoutingBuilder.newInstance().key("toB").toTask(B.getId()).extra("fromA").build())
				   			   .addTask(B,"b").putRouting(B, RoutingBuilder.newInstance().key("toA").toTask(A.getId()).extra("fromB2A").build())
				   			   			  .putRouting(B, RoutingBuilder.newInstance().key("toD").toTask(D.getId()).extra("fromB2D").build())
				   			   		.putRouting(B, RoutingBuilder.newInstance().key("toB").toTask(B.getId()).extra("fromB2B").build())
				   			   .addTask(D,"d").addTask(C).setStart(A.getId()).setEnd(C.getId()).build();
		String result = work.run().getResult();
		System.out.println(result);
	}
	
	public static void main(String[] args) {
		init();
//		runSequential();
		runRouteable();
	}
}
