package demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.task.TaskA;
import demo.task.TaskB;
import demo.task.TaskC;
import taskflow.annotation.EnableTaskFlow;
import taskflow.config.TaskFlowBeanReloadProcessor;
import taskflow.config.bean.TaskBeanDefinition;
import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.WorkDefinition;
import taskflow.config.bean.WorkDefinition.TaskRef;
import taskflow.logger.TFLogger;
import taskflow.work.SequentialRouteWork;
import taskflow.work.Work;
import taskflow.work.WorkFactory;
import taskflow.work.context.TaskTrace;

/**
 * taskflow2.0.1 <br/>
 * 支持XML、YML、Prop配置Task/Work；支持热加载；建议将Work/Task的配置存放到数据库，修改更方便<br/>
 * 相关配置见application.yml
 * 
 * @author bailey
 * @date 2021年8月19日
 */
@EnableTaskFlow
@SpringBootApplication
@RestController
@Configuration
@ImportResource("classpath*:task-config.xml")
public class ReloadableApplication {
	//自定义TFLogger将日志输出到指定地方；默认输出到Console
	//@EnableTaskFlow方式开启，才会输出Work/Task的注册/重载信息
	//@Bean
	public TFLogger RegisterLogger() {
		return new TFLogger() {
			@Override
			protected Consumer<String> getLogPrinter() {
				return System.out::println;
			}
		};
	}
	/** 可以单独定义TaskBean/Task/Work，也可以一次定义一组(Collection)，或者直接定义成TaskflowConfiguration */
	//定义多个TaskBean
	@Bean
	public Set<TaskBeanDefinition> taskflowTaskBeans() {
		Set<TaskBeanDefinition> taskBeanDefinitions = new HashSet<>();
		TaskBeanDefinition taskBeanDefinition = new TaskBeanDefinition();
		taskBeanDefinition.setBeanId("TaskABean");
		taskBeanDefinition.setBeanClazz(TaskA.class.getName());
		taskBeanDefinitions.add(taskBeanDefinition);
		taskBeanDefinition = new TaskBeanDefinition();
		taskBeanDefinition.setBeanId("TaskCBean");
		taskBeanDefinition.setBeanClazz(TaskC.class.getName());
		taskBeanDefinitions.add(taskBeanDefinition);
		return taskBeanDefinitions;
	}
	//定义多个Task
	@Bean
	public List<TaskDefinition> taskflowTasks() {
		List<TaskDefinition> taskDefinitions = new ArrayList<>();
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
		return taskDefinitions;
	}
	//定义一个Work
	@Bean
	public WorkDefinition taskflowWork() {
		WorkDefinition workDefinition = new WorkDefinition();
		workDefinition.setWorkId("Work1");
		workDefinition.setWorkClazz(SequentialRouteWork.class.getName());
		workDefinition.setTraceable(true);
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
		return workDefinition;
	}

	@Autowired
	TaskFlowBeanReloadProcessor taskFlowBeanReloadProcessor;

	@RequestMapping("/")
	public String index() {
		StringBuilder welcome=new StringBuilder("Welcome to TaskFlow Demo!<br/>");
		welcome.append("You can<br/>");
		welcome.append("<a href=\"/s\">test SequentialWork</a> and <a href=\"/r\">test RouteAbleWork</a><br/>");
		welcome.append("or<br/>");
		welcome.append("<a href=\"/reload\">reload</a> the <i>SequentialWork</i> above , then try test it again.");
		return welcome.toString();
	}
	
	@RequestMapping("/s")
	public String sequentialWork() {
		Work Work1 = WorkFactory.createWork("Work1");
		Object result = Work1.run();
		ArrayList<TaskTrace> taskTraces = Work1.getTaskTraces();
		if (taskTraces != null) {
			for (TaskTrace tt : taskTraces) {
				System.out.println("Work1-[TRACE]" + tt);
			}
		}
		return result.toString();
	}
	
	@RequestMapping("/r")
	public String routeAbleWork() {
		Work testWork = WorkFactory.createWork("testWork");
		List<Integer> input = Arrays.asList(5, 7, 1, 0, 1, 3, 4, 5, 6, 4);
        testWork.putContext("intList", input);
		String result = testWork.run().getResult();
		ArrayList<TaskTrace> taskTraces = testWork.getTaskTraces();
		if (taskTraces != null) {
			for (TaskTrace tt : taskTraces) {
				System.out.println("testWork-[TRACE]" + tt);
			}
		}
		return result;
	}

	//修改Work1的Task执行序列
	@RequestMapping("/reload")
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

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ReloadableApplication.class, args);
	}

}
