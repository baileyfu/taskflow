package demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.work.MySequentialWork;
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

		try {
			Work testWork = WorkFactory.createWork("mySequentialTaskWork");
			testWork.run();
			System.out.println("Work's tagName is "+((MySequentialWork)testWork).getTagName());
		}catch(Exception e) {
			throw e;
		}finally {
			context.close();
		}
	}
}
