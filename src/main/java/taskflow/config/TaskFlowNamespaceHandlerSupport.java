package taskflow.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import taskflow.enums.Tag;

/**
 * bf注册
 * Created by lizhou on 2017/3/14/014.
 */
public class TaskFlowNamespaceHandlerSupport extends NamespaceHandlerSupport{
	public void init() {
		registerBeanDefinitionParser(Tag.TASK.VALUE, new TaskDefinitionParser());
		registerBeanDefinitionParser(Tag.WORK.VALUE, new WorkDefinitionParser());
		registerBeanDefinitionParser(Tag.WORK_FACTORY.VALUE, new WorkFactoryDefinitionParser());
	}
}
