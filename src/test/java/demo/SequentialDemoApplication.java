package demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import taskflow.work.Work;
import taskflow.work.WorkFactory;

/**
 * Sequential Work
 * 
 * @author bailey
 * @date 2021年8月19日
 */
public class SequentialDemoApplication {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:serial-task-config.xml");

		Work testWork = WorkFactory.createWork("sequentialTaskWork");
		testWork.run();

		context.close();
	}
}
