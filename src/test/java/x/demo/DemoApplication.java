package x.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoApplication {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:status-bus-config.xml");

		StatusHolderBus testBus = context.getBean(StatusHolderBus.class, "15888888888");
		System.out.println(testBus);
		System.out.println(testBus.getClass());
		testBus.run();

		Thread.sleep(1000l);
		System.out.println("------------");
		testBus.run();

		context.close();
	}
}
