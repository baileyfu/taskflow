package demo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.task.TaskA;
import demo.task.TaskB;
import demo.task.TaskC;
import taskflow.annotation.EnableTaskFlow;
import taskflow.config.TaskFlowBeanReloadProcessor;
import taskflow.config.bean.TaskBeanDefinition;
import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskflowConfiguration;
import taskflow.config.bean.WorkDefinition;
import taskflow.config.bean.WorkDefinition.TaskRef;
import taskflow.work.SequentialRouteWork;
import taskflow.work.Work;
import taskflow.work.WorkFactory;
import taskflow.work.context.TaskTrace;

/**
 * taskflow2.0.1 <br/>
 * 支持非XML配置，可热加载
 * 
 * @author bailey
 * @date 2021年8月19日
 */
@EnableTaskFlow
@SpringBootApplication
@RestController
@Configuration
public class ReloadableApplication {
	@Bean
	public TaskflowConfiguration taskflowConfiguration() {
		Set<TaskBeanDefinition> taskBeanDefinitions = new HashSet<>();
		TaskBeanDefinition taskBeanDefinition = new TaskBeanDefinition();
		taskBeanDefinition.setBeanId("TaskABean");
		taskBeanDefinition.setBeanClazz(TaskA.class.getName());
		taskBeanDefinitions.add(taskBeanDefinition);
		taskBeanDefinition = new TaskBeanDefinition();
		taskBeanDefinition.setBeanId("TaskCBean");
		taskBeanDefinition.setBeanClazz(TaskC.class.getName());
		taskBeanDefinitions.add(taskBeanDefinition);
		Set<TaskDefinition> taskDefinitions = new HashSet<>();
		TaskDefinition taskDefinition = new TaskDefinition();
		taskDefinition.setTaskId("Task1");
		taskDefinition.setTaskBeanId("TaskABean");
		taskDefinition.setExtra("{EEE:'FFF'}");
		taskDefinitions.add(taskDefinition);
		taskDefinition = new TaskDefinition();
		taskDefinition.setTaskId("Task2");
		taskDefinition.setTaskBeanId("TaskCBean");
		taskDefinition.setMethod("method1");
		taskDefinition.setExtra("{AAA:'BBB'}");
		taskDefinitions.add(taskDefinition);
		taskDefinition = new TaskDefinition();
		taskDefinition.setTaskId("Task3");
		taskDefinition.setTaskBeanId("TaskCBean");
		taskDefinition.setMethod("method3");
		taskDefinition.setExtra("{BBB:'DDD'}");
		taskDefinitions.add(taskDefinition);
		Set<WorkDefinition> workDefinitions = new HashSet<>();
		WorkDefinition workDefinition = new WorkDefinition();
		workDefinition.setWorkId("Work1");
		workDefinition.setWorkClazz(SequentialRouteWork.class.getName());
		workDefinition.setTraceable(false);
		ArrayList<TaskRef> taskRefs = new ArrayList<>();
		TaskRef taskRef = new TaskRef();
		taskRef.setTaskId("Task1");
		taskRef.setExtra("{A:'B'}");
		taskRefs.add(taskRef);
		taskRef = new TaskRef();
		taskRef.setTaskId("Task2");
		taskRef.setExtra("{C:'D'}");
		taskRefs.add(taskRef);
		taskRef = new TaskRef();
		taskRef.setTaskId("Task3");
		taskRefs.add(taskRef);
		workDefinition.setTaskRefs(taskRefs);
		workDefinitions.add(workDefinition);
		TaskflowConfiguration taskflowConfiguration = new TaskflowConfiguration();
		taskflowConfiguration.setTaskBeanDefinitions(taskBeanDefinitions);
		taskflowConfiguration.setTaskDefinitions(taskDefinitions);
		taskflowConfiguration.setWorkDefinitions(workDefinitions);
		return taskflowConfiguration;
	}

	@Autowired
	TaskFlowBeanReloadProcessor taskFlowBeanReloadProcessor;

	@RequestMapping("/")
	public String index() {
		Work Work1 = WorkFactory.createWork("Work1");
		Object result = Work1.run();
		ArrayList<TaskTrace> taskTraces = Work1.getTaskTraces();
		if (taskTraces != null) {
			for (TaskTrace tt : taskTraces) {
				System.out.println("TRACE--->" + tt);
			}
		}
		return "Run result===>" + result;
	}

	@RequestMapping("/change")
	public String change() {
		TaskBeanDefinition taskBeanDefinition = new TaskBeanDefinition();
		taskBeanDefinition.setBeanId("TaskBBean");
		taskBeanDefinition.setBeanClazz(TaskB.class.getName());

		TaskDefinition td = new TaskDefinition();
		td.setTaskBeanId("TaskBBean");
		td.setExtra("EXTRA");
		td.setTaskId("TaskB");

		WorkDefinition workDefinition = new WorkDefinition();
		workDefinition.setWorkId("Work1");
		workDefinition.setWorkClazz(SequentialRouteWork.class.getName());
		workDefinition.setTraceable(false);
		ArrayList<TaskRef> taskRefs = new ArrayList<>();
		TaskRef taskRef = new TaskRef();
		taskRef.setTaskId("Task1");
		taskRef.setExtra("{AA:'BB'}");
		taskRefs.add(taskRef);
		taskRef = new TaskRef();
		taskRef.setTaskId("TaskB");
		taskRefs.add(taskRef);
		taskRef = new TaskRef();
		taskRef.setTaskId("Task3");
		taskRefs.add(taskRef);
		workDefinition.setTaskRefs(taskRefs);
		taskFlowBeanReloadProcessor.reload(taskBeanDefinition, td, workDefinition);
		return "SUCCESS";
	}

	public static void mainc(String[] args) throws Exception {
		SpringApplication.run(ReloadableApplication.class, args);
	}

	public static void main(String[] args) throws Exception {
		String file;
		file = "classpath:status-bus-config.xml";
//		file="classpath:serial-task-config.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(file);

		Work testBus = WorkFactory.createWork("testStatusBus"
//				"sequentialTaskWork"
		);
		testBus.run();

		Thread.sleep(1000l);
//		System.out.println("+++++++++++++");
//		testBus = (CustomRouteWork)context.getBean("testStatusBus");
//		Object result = testBus.run().getResult();
//		System.out.println(result);
		context.close();
	}
}
