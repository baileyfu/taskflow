package taskflow.config.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.Assert;

import taskflow.config.bean.DefaultTaskExecutorFactory;
import taskflow.config.bean.TaskExecutorFactory;
import taskflow.config.bean.WorkDefinition;
import taskflow.config.bean.WorkDefinition.ConstructorArg;
import taskflow.config.bean.WorkDefinition.TaskRef;
import taskflow.constants.TFLogType;
import taskflow.constants.WorkPropName;
import taskflow.work.CustomRouteWork;
import taskflow.work.SequentialRouteWork;
import taskflow.work.Work;
import taskflow.work.WorkFactory;

//后注册的Bean优先级高
public interface WorkRegister extends ConfigSourceAware{

	default BeanDefinition registerWork(BeanDefinitionRegistry registry, WorkDefinition workDefinition) {
		Class<?> workClazz = null;
		try {
			workClazz = Class.forName(workDefinition.getWorkClazz());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		Assert.isTrue(Work.class.isAssignableFrom(workClazz),"Work '"+workDefinition.getWorkId()+"' must has a class of type of Work");
		RootBeanDefinition work = new RootBeanDefinition();
        work.setBeanClass(workClazz);
        work.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        //set constructor args
        ConstructorArgumentValues constructorArgumentValues=new ConstructorArgumentValues();
        if(workDefinition.getConstructorArgs()!=null) {
			for (ConstructorArg arg : workDefinition.getConstructorArgs()) {
				ValueHolder valueHolder;
				if (StringUtils.isEmpty(arg.getRef())) {
					String type = arg.getType();
					String name = arg.getName();
					valueHolder = new ValueHolder(arg.getValue(), StringUtils.isEmpty(type)?null:type, StringUtils.isEmpty(arg.getName())?null:name);
				}else {
					valueHolder = new ValueHolder(new RuntimeBeanReference(arg.getRef()));
				}
				if (arg.getIndex() >= 0) {
					constructorArgumentValues.addIndexedArgumentValue(arg.getIndex(), valueHolder);
				}else {
					constructorArgumentValues.addGenericArgumentValue(valueHolder);
				}
        	}
        }
        //set property
        work.getPropertyValues().add(WorkPropName.NAME, workDefinition.getWorkId());
        work.getPropertyValues().add(WorkPropName.TRACEABLE, workDefinition.getTraceable());
        if(CustomRouteWork.class.isAssignableFrom(work.getBeanClass())) {//只有CustomRouteWork才解析start和finish
        	String start = workDefinition.getStart();
			Assert.isTrue(!StringUtils.isEmpty(start), "Work '"+workDefinition.getWorkId()+"' must has a start task");
            String finish = workDefinition.getFinish();
        	
        	RuntimeBeanReference startBean = new RuntimeBeanReference(start);
            work.getPropertyValues().add(WorkPropName.START, startBean);
            if (!StringUtils.isEmpty(finish)) {
                RuntimeBeanReference finishBean = new RuntimeBeanReference(finish);
                work.getPropertyValues().add(WorkPropName.FINISH, finishBean);
            }
		} else if(SequentialRouteWork.class.isAssignableFrom(work.getBeanClass())) {//只有SerialRouteWork才解析sequence
        	ManagedMap<String, RuntimeBeanReference> tasksMap=new ManagedMap<>();
			ArrayList<TaskRef> taskRefs = workDefinition.getTaskRefs();
			Assert.isTrue(taskRefs != null && taskRefs.size() > 0, "the Work '"+workDefinition.getWorkId()+"' must has a task-ref at least!");
			Map<String, String> taskRefExtraMap = null;
			HashSet<String> asyncTasks = null;
			for (TaskRef taskRef : taskRefs) {
				String taskId = taskRef.getTaskId();
				if (StringUtils.isEmpty(taskId)) {
					throw new NullPointerException("the Work '"+workDefinition.getWorkId()+"' has a empty task-ref");
				}
				tasksMap.put(taskId, new RuntimeBeanReference(taskId));
				String extra = taskRef.getExtra();
				if (!StringUtils.isEmpty(extra)) {
					if (taskRefExtraMap == null) {
						taskRefExtraMap = new HashMap<>();
					}
					taskRefExtraMap.put(taskId, extra.trim());
				}
				if (taskRef.isAsync()) {
					if (asyncTasks == null) {
						asyncTasks = new HashSet<>();
					}
					asyncTasks.add(taskId);
				}
			}
			if (taskRefExtraMap != null) {
				//extra构造参数必须放到最后
				constructorArgumentValues.addIndexedArgumentValue(constructorArgumentValues.getArgumentCount(), taskRefExtraMap);
			}
			if (asyncTasks != null) {
				work.getPropertyValues().add(WorkPropName.ASYNC_TASKS, asyncTasks);
				try {
					TaskExecutorFactory taskExecutorFactoryInstance = ((BeanFactory) registry).getBean(TaskExecutorFactory.class);
					work.getPropertyValues().add(WorkPropName.TASK_EXECUTOR_FACTORY, taskExecutorFactoryInstance);
				} catch (Exception e) {
					System.out.println("***[TaskFlow] No bean definition of TaskExecutorFactory found , use default TaskExecutorFactory.");
					if(!registry.containsBeanDefinition(WorkPropName.TASK_EXECUTOR_FACTORY)) {
						RootBeanDefinition taskExecutorFactory = new RootBeanDefinition();
						taskExecutorFactory.setBeanClass(DefaultTaskExecutorFactory.class);
						BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskExecutorFactory, WorkPropName.TASK_EXECUTOR_FACTORY), registry);
					}
					work.getPropertyValues().add(WorkPropName.TASK_EXECUTOR_FACTORY, new RuntimeBeanReference(WorkPropName.TASK_EXECUTOR_FACTORY));
				}
			}
			work.getPropertyValues().add(WorkPropName.TASKS, tasksMap);
        }
        if(!constructorArgumentValues.isEmpty()) {
        	work.setConstructorArgumentValues(constructorArgumentValues);
        }
        work.getPropertyValues().add(WorkPropName.MAX_TASKS, Integer.valueOf(workDefinition.getMaxTasks()));
		if (registry.containsBeanDefinition(workDefinition.getWorkId()))
			registry.removeBeanDefinition(workDefinition.getWorkId());
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(work, workDefinition.getWorkId()), registry);
		
		logRegister(TFLogType.WORK,workDefinition.getWorkId(), workDefinition.toString());
		return work;
	}
	
	default BeanDefinition registerWorkFactory(BeanDefinitionRegistry registry) {
		String beanName = WorkFactory.class.getSimpleName();
		if (registry.containsBeanDefinition(beanName))
			return registry.getBeanDefinition(beanName);
		RootBeanDefinition workerFactory = new RootBeanDefinition();
		workerFactory.setBeanClass(WorkFactory.class);
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(workerFactory, beanName), registry);
		
		logRegister(TFLogType.WORK_FACTORY,beanName, "[beanId:"+beanName+" , class:"+WorkFactory.class.getName()+"]");
		return workerFactory;
	}
}
