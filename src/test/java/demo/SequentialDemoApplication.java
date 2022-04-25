package demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.work.MySequentialWork;
import taskflow.constants.PropertyNameAndValue;
import taskflow.work.AbstractWork;
import taskflow.work.Work;
import taskflow.work.WorkFactory;

/**
 * Sequential Work
 * 
 * @author bailey
 * @date 2021年8月19日
 */
public class SequentialDemoApplication {
	
	public static void main(String[] args) throws Exception{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:serial-task-config.xml");
		//后执行的优先级高；此时默认的maxTasks为66而不是application.properties中定义的值
		System.setProperty(PropertyNameAndValue.WORK_MAX_TASKS, "66");
		try {
			Work testWork = WorkFactory.createWork("mySequentialTaskWork");
			//设置当前maxTasks值为88
			((AbstractWork)testWork).setMaxTasks(88);
			testWork.run();
			System.out.println("Work's tagName is "+((MySequentialWork)testWork).getTagName());
		}catch(Exception e) {
			throw e;
		}finally {
			context.close();
		}
	}
}
