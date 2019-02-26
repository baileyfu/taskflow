package taskflow.config.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.Assert;

import taskflow.config.bean.WorkDefinition;
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
		Assert.isTrue(Work.class.isAssignableFrom(workClazz),"Work must has a class of type of Work");
		RootBeanDefinition work = new RootBeanDefinition();
        work.setBeanClass(workClazz);
        work.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        work.getPropertyValues().add(WorkPropName.NAME, workDefinition.getWorkId());
        work.getPropertyValues().add(WorkPropName.TRACEABLE, workDefinition.getTraceable());
        
        if(CustomRouteWork.class.isAssignableFrom(work.getBeanClass())) {//只有CustomRouteWork才解析start和finish
        	String start = workDefinition.getStart();
			Assert.isTrue(!StringUtils.isEmpty(start), "Work's start can not be empty");
            String finish = workDefinition.getFinish();
        	
        	RuntimeBeanReference startBean = new RuntimeBeanReference(start);
            work.getPropertyValues().add(WorkPropName.START, startBean);
            if (!StringUtils.isEmpty(finish)) {
                RuntimeBeanReference finishBean = new RuntimeBeanReference(finish);
                work.getPropertyValues().add(WorkPropName.FINISH, finishBean);
            }
        }else if(SequentialRouteWork.class.isAssignableFrom(work.getBeanClass())) {//只有SerialRouteWork才解析sequence
        	ManagedMap<String, RuntimeBeanReference> tasksMap=new ManagedMap<>();
			ArrayList<TaskRef> taskRefs = workDefinition.getTaskRefs();
			Assert.isTrue(taskRefs != null && taskRefs.size() > 0, "the Work must has a task-ref at least!");
			Map<String, String> taskRefExtraMap = new HashMap<>();
			for (TaskRef taskRef : taskRefs) {
				if(StringUtils.isEmpty(taskRef.getTaskId())) {
					throw new NullPointerException("Work's task-ref can not be empty");
				}
				tasksMap.put(taskRef.getTaskId(), new RuntimeBeanReference(taskRef.getTaskId()));
				if (!StringUtils.isBlank(taskRef.getExtra())) {
					taskRefExtraMap.put(taskRef.getTaskId(), taskRef.getExtra());
				}
			}
			work.getPropertyValues().add(WorkPropName.TASKS, tasksMap);
			if (taskRefExtraMap.size() > 0) {
				ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
				constructorArgumentValues.addGenericArgumentValue(taskRefExtraMap);
				work.setConstructorArgumentValues(constructorArgumentValues);
			}
        }
        work.getPropertyValues().add(WorkPropName.MAX_TASKS, Integer.valueOf(workDefinition.getMaxTasks()));
		if (registry.containsBeanDefinition(workDefinition.getWorkId()))
			registry.removeBeanDefinition(workDefinition.getWorkId());
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(work, workDefinition.getWorkId()), registry);
		
		RegisterLogger.log(getConfigSource(), TFLogType.WORK,workDefinition.getWorkId(), workDefinition.toString());
		return work;
	}
	
	default BeanDefinition registerWorkFactory(BeanDefinitionRegistry registry) {
		String beanName = WorkFactory.class.getSimpleName();
		if (registry.containsBeanDefinition(beanName))
			return registry.getBeanDefinition(beanName);
		RootBeanDefinition workerFactory = new RootBeanDefinition();
		workerFactory.setBeanClass(WorkFactory.class);
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(workerFactory, beanName), registry);
		
		RegisterLogger.log(getConfigSource(), TFLogType.WORK_FACTORY,beanName, "[beanId:"+beanName+" , class:"+WorkFactory.class.getName()+"]");
		return workerFactory;
	}
}
