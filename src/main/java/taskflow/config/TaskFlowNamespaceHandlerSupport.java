package taskflow.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import taskflow.enums.Tag;

/**
 * bf注册
 * Created by lizhou on 2017/3/14/014.
 */
public class TaskFlowNamespaceHandlerSupport extends NamespaceHandlerSupport{
	public void init() {
		TaskDefinitionParser taskDefinitionParser = new TaskDefinitionParser();
		registerBeanDefinitionParser(Tag.TASK.VALUE, taskDefinitionParser);
		registerBeanDefinitionParser(Tag.TASKWRAPPER.VALUE, taskDefinitionParser);
		registerBeanDefinitionParser(Tag.WORK.VALUE, new WorkDefinitionParser());
		registerBeanDefinitionParser(Tag.WORK_FACTORY.VALUE, new WorkFactoryDefinitionParser());
	}
}
