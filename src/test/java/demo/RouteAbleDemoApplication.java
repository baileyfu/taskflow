package demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import taskflow.constants.PropertyNameAndValue;
import taskflow.work.Work;
import taskflow.work.WorkFactory;
import taskflow.work.context.TaskTrace;

/**
 * RouteAble Work
 * @author bailey
 */
public class RouteAbleDemoApplication {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:task-config.xml");
		//XML方式配置TaskFlow通过系统Property来设置参数
		//后设置的优先级高,会覆盖task-config.xml中指定的properties文件中的配置
		System.setProperty(PropertyNameAndValue.WORK_TRACEABLE, "true");
		System.setProperty(PropertyNameAndValue.LOG_PRINTABLE, "true");
		System.setProperty(PropertyNameAndValue.LOG_PRINT_DETAIL, "true");
		
		
        Work testWork = WorkFactory.createWork("testWork");
        List<Integer> input = Arrays.asList(5, 7, 1, 0, 1, 3, 4, 5, 6, 4);
        testWork.putContext("intList", input);
		String result = testWork.run().getResult();
		System.out.println(result);

//        testWork = WorkFactory.createWork("testWork");
//        input = Arrays.asList(52, 7, 1, -10, 1, 3, 4, 5, 6, 4);
//        testWork.putContext("intList", input);
//        result = testWork.run().getResult();
//		System.out.println(result);
        
        //输出Task执行时的WorkContext快照
        ArrayList<TaskTrace> taskTraces = testWork.getTaskTraces();
		if (taskTraces != null) {
			for (TaskTrace tt : taskTraces) {
				System.out.println("testWork-[TRACE]" + tt);
			}
		}
        
        context.close();
	}

}
