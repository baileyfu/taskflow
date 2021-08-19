package demo;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import taskflow.work.Work;
import taskflow.work.WorkFactory;

public class DemoApplication {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:task-config.xml");

        Work testWork = WorkFactory.createWork("testWork");
        List<Integer> input = Arrays.asList(5, 7, 1, 0, 1, 3, 4, 5, 6, 4);
        testWork.putContext("intList", input);
        testWork.run();

        testWork = WorkFactory.createWork("testWork");
        input = Arrays.asList(52, 7, 1, -10, 1, 3, 4, 5, 6, 4);
        testWork.putContext("intList", input);
        testWork.run();
        
        context.close();
	}

}
