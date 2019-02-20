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
import taskflow.constants.WorkPropName;
import taskflow.work.CustomRouteWork;
import taskflow.work.SequentialRouteWork;
import taskflow.work.WorkFactory;

public interface WorkRegister {

	default BeanDefinition registerWork(BeanDefinitionRegistry registry,WorkDefinition workDefinition) {
		RootBeanDefinition work = new RootBeanDefinition();
        work.getPropertyValues().add(WorkPropName.NAME, workDefinition.getWorkId());
        work.setBeanClass(workDefinition.getWorkClazz());
        work.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        
        Map<String, String> extraArgsMap = new HashMap<>();
        //TODO CustomRouteWork支持extraArgs
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
			Assert.isTrue(taskRefs != null & taskRefs.size() > 0, "the Work must has a task-ref at least!");
			for (TaskRef taskRef : taskRefs) {
				if(StringUtils.isEmpty(taskRef.getTaskId())) {
					throw new NullPointerException("Work's task-ref can not be empty");
				}
				tasksMap.put(taskRef.getTaskId(), new RuntimeBeanReference(taskRef.getTaskId()));
				if (!StringUtils.isBlank(taskRef.getExtra())) {
					extraArgsMap.put(taskRef.getTaskId(), taskRef.getExtra());
				}
			}
			work.getPropertyValues().add(WorkPropName.TASKS, tasksMap);
        }
        if (extraArgsMap.size() > 0) {
			ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
			constructorArgumentValues.addGenericArgumentValue(extraArgsMap);
			work.setConstructorArgumentValues(constructorArgumentValues);
		}
        work.getPropertyValues().add(WorkPropName.MAX_TASKS, Integer.valueOf(workDefinition.getMaxTasks()));
        
        BeanDefinitionHolder holder = new BeanDefinitionHolder(work, workDefinition.getWorkId());
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
        return work;
	}
	
	default BeanDefinition registerWorkFactory(BeanDefinitionRegistry registry) {
		RootBeanDefinition workerFactory = new RootBeanDefinition();
        workerFactory.setBeanClass(WorkFactory.class);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(workerFactory, WorkFactory.class.getSimpleName());
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
        return workerFactory;
	}
}
