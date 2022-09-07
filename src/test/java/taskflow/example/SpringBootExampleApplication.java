package taskflow.example;


import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import taskflow.annotation.EnableTaskFlow;
import taskflow.task.Task;
import taskflow.work.Work;
import taskflow.work.builder.WorkBuilder;
import taskflow.work.context.WorkContext;

@RestController
@Configuration
@EnableTaskFlow
@SpringBootApplication
public class SpringBootExampleApplication implements ApplicationContextAware{
	ApplicationContext applicationContext;
	@Bean
	public Task a() {
		return (WorkContext workContext) -> {
			//do something by some service
			System.out.println("Task A be called!");
			//set some parameter for next task
			workContext.put("paramName","value from taskA");
			//some condition to decide next task
			boolean condition = 1 + 1 > 2;
			if (condition) {
				workContext.setRoutingKey("toB");
			} else {
				workContext.setRoutingKey("toC");
			}
		};
	}
	@Bean
	public Task b() {
		return (WorkContext workContext) -> {
			//do something by some service
			System.out.println("Task B be called and received param ->" + workContext.get("paramName"));
		};
	}
	@Bean
	public Task c() {
		return (WorkContext workContext) -> {
			//do something by some service
			System.out.println("Task C be called and received param ->" + workContext.get("paramName"));
		};
	}
	@Bean
	public Task finalTask() {
		return (WorkContext workContext) -> {
			//do something by some service
			System.out.println("Task final be called and set the result");
			//final result
			workContext.setResult("Result value : " + workContext.getAll());
		};
	}
	//a->(b or c)->finalTask
	private Work routeAbleWork() {
		Task a = applicationContext.getBean("a",Task.class);
		Task b = applicationContext.getBean("b",Task.class);
		Task c = applicationContext.getBean("c",Task.class);
		Task finalTask = applicationContext.getBean("finalTask",Task.class);
		return WorkBuilder.newRouteableInstance()
												.addTask(a).putRouting("toB", b.getId())
														   .putRouting("toC", c.getId())
												.addTask(b)
												.addTask(c)
												.addEnd(finalTask)
												.setStart(a.getId())
												.build("routeAbleWork");
	}
	//a->b->c->finalTask
	private Work sequentialWork() {
		Task a = applicationContext.getBean("a",Task.class);
		Task b = applicationContext.getBean("b",Task.class);
		Task c = applicationContext.getBean("c",Task.class);
		Task finalTask = applicationContext.getBean("finalTask",Task.class);
		return WorkBuilder.newSequentialInstance()
												.addTask(a)
												.addTask(b)
												.addTask(c)
												.addTask(finalTask)
												.build("sequentialWork");
	}
	
	@GetMapping("/r")
	public String executeRouteAbleWork() {
		return routeAbleWork().run().getResult();
	}
	@GetMapping("/s")
	public String executeSequentialWork() {
		return sequentialWork().run().getResult();
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootExampleApplication.class, args);
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
