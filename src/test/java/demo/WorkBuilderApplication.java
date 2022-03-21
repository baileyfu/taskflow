package demo;

import taskflow.config.bean.DefaultTaskExecutorFactory;
import taskflow.work.Work;
import taskflow.work.builder.SequentialWorkBuilder;
import taskflow.work.builder.WorkBuilder;

/**
 * 编程式创建Work
 * @author bailey
 * @date 2022年3月21日
 */
public class WorkBuilderApplication {

	public static void main(String[] args) {
		DefaultTaskExecutorFactory defaultTaskExecutorFactory = new DefaultTaskExecutorFactory();
		SequentialWorkBuilder workBuilder = WorkBuilder.newInstance();
		workBuilder.addTask((workContext)->{
			System.out.println("A--->"+workContext.getRuntimeArgs());
		},"aaa");
		workBuilder.addSyncTask((workContext)->{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			workContext.put("bewait","from async");
			System.out.println("ASYNC--000===>"+workContext.getRuntimeArgs());
		},"palapala");
		workBuilder.addTask((workContext)->{
			System.out.println("B--->"+workContext.getRuntimeArgs());
		},"bbb");
		workBuilder.addTask((workContext)->{
			workContext.setResult("Result="+workContext.get("bewait"));
		});
		Work work = workBuilder.build(defaultTaskExecutorFactory);
		work.run();
		String result=work.getWorkContext().getResult();
		System.out.println(result);
		defaultTaskExecutorFactory.shutdown();
	}

}
