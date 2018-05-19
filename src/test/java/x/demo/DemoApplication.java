package x.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import taskflow.work.SequentialRouteWork;

public class DemoApplication {

	public static void main(String[] args) throws Exception {
		String file="classpath:status-bus-config.xml";
		file="classpath:serial-task-config.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(file);

		SequentialRouteWork testBus = (SequentialRouteWork)context.getBean("sequentialTaskWork");
		testBus.run();

		Thread.sleep(1000l);
		System.out.println("------------");
		Object result = testBus.run().getResult();
		System.out.println(result);
		context.close();
	}
}
