package x.demo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import taskflow.annotation.EnableTaskFlow;
import taskflow.config.bean.TaskBeanDefinition;
import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskflowConfiguration;
import taskflow.config.bean.WorkDefinition;
import taskflow.config.bean.WorkDefinition.TaskRef;
import taskflow.work.SequentialRouteWork;
import taskflow.work.Work;
import taskflow.work.WorkFactory;
import taskflow.work.context.TaskTrace;
import x.demo.work.task.TaskA;
import x.demo.work.task.TaskC;

@EnableTaskFlow
@SpringBootApplication
@RestController
@Configuration
public class DemoApplication {
	@Bean
	public TaskflowConfiguration taskflowConfiguration() {
		Set<TaskBeanDefinition> taskBeanDefinitions = new HashSet<>();
		TaskBeanDefinition taskBeanDefinition=new TaskBeanDefinition();
		taskBeanDefinition.setBeanId("TaskABean");
		taskBeanDefinition.setBeanClazz(TaskA.class);
		taskBeanDefinitions.add(taskBeanDefinition);
		taskBeanDefinition=new TaskBeanDefinition();
		taskBeanDefinition.setBeanId("TaskCBean");
		taskBeanDefinition.setBeanClazz(TaskC.class);
		taskBeanDefinitions.add(taskBeanDefinition);
		Set<TaskDefinition> taskDefinitions = new HashSet<>();
		TaskDefinition taskDefinition=new TaskDefinition();
		taskDefinition.setTaskId("Task1");
		taskDefinition.setTaskBeanId("TaskABean");
		taskDefinition.setExtra("{EEE:'FFF'}");
		taskDefinitions.add(taskDefinition);
		taskDefinition=new TaskDefinition();
		taskDefinition.setTaskId("Task2");
		taskDefinition.setTaskBeanId("TaskCBean");
		taskDefinition.setMethod("method1");
		taskDefinition.setExtra("{AAA:'BBB'}");
		taskDefinitions.add(taskDefinition);
		taskDefinition=new TaskDefinition();
		taskDefinition.setTaskId("Task3");
		taskDefinition.setTaskBeanId("TaskCBean");
		taskDefinition.setMethod("method3");
		taskDefinition.setExtra("{BBB:'DDD'}");
		taskDefinitions.add(taskDefinition);
		Set<WorkDefinition> workDefinitions = new HashSet<>();
		WorkDefinition workDefinition=new WorkDefinition();
		workDefinition.setWorkId("Work1");
		workDefinition.setWorkClazz(SequentialRouteWork.class);
		workDefinition.setTraceable(false);
		ArrayList<TaskRef> taskRefs=new ArrayList<>();
		TaskRef taskRef=new TaskRef();
		taskRef.setTaskId("Task1");
		taskRef.setExtra("{A:'B'}");
		taskRefs.add(taskRef);
		taskRef=new TaskRef();
		taskRef.setTaskId("Task2");
		taskRef.setExtra("{C:'D'}");
		taskRefs.add(taskRef);
		taskRef=new TaskRef();
		taskRef.setTaskId("Task3");
		taskRefs.add(taskRef);
		workDefinition.setTaskRefs(taskRefs);
		workDefinitions.add(workDefinition);
		TaskflowConfiguration taskflowConfiguration=new TaskflowConfiguration();
		taskflowConfiguration.setTaskBeanDefinitions(taskBeanDefinitions);
		taskflowConfiguration.setTaskDefinitions(taskDefinitions);
		taskflowConfiguration.setWorkDefinitions(workDefinitions);
		return taskflowConfiguration;
	}
	@RequestMapping("/")
	public String index() {
		Work Work1 = WorkFactory.createWork("Work1");
		Object result=Work1.run();
		ArrayList<TaskTrace> taskTraces=Work1.getTaskTraces();
		if (taskTraces != null) {
			for (TaskTrace tt : taskTraces) {
				System.out.println("TRACE--->" + tt);
			}
		}
		return "Run result===>"+result;
	}
	@RequestMapping("/change")
	public String change() {
		return "SUCCESS";
	}
	public static void main(String[] args) throws Exception {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	public static void mains(String[] args) throws Exception {
//		AppGroovy.main();
		String file="classpath:status-bus-config.xml";
		file="classpath:serial-task-config.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(file);

		SequentialRouteWork testBus = (SequentialRouteWork)context.getBean("sequentialTaskWork");
		testBus.run();

		Thread.sleep(1000l);
		System.out.println("+++++++++++++");
		testBus = (SequentialRouteWork)context.getBean("sequentialTaskWork");
		Object result = testBus.run().getResult();
		System.out.println(result);
		context.close();
	}
}
