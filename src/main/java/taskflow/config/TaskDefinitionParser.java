package taskflow.config;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ID_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.REF_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.VALUE_ATTRIBUTE;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskDefinition.RouteDefinition;
import taskflow.config.register.TaskRegister;
import taskflow.enums.Tag;
import taskflow.enums.TagAttribute;
import taskflow.task.TaskRoutingWrap;

/**
 * 处理<tf:task>标签 对于ref的Task，使用{@link TaskRoutingWrap}进行包装 <br/>
 * Created by lizhou on 2017/3/14/014. <br/>
 * updated by fuli on 2018/5 <br/>
 */
public class TaskDefinitionParser implements BeanDefinitionParser,TaskRegister {
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String id = element.getAttribute(ID_ATTRIBUTE);
		String ref = element.getAttribute(REF_ATTRIBUTE);
		String method = element.getAttribute(TagAttribute.TASK_METHOD.NAME);
		
		TaskDefinition taskDefinition=new TaskDefinition();
		taskDefinition.setTaskId(id);
		taskDefinition.setTaskBeanId(ref);
		taskDefinition.setMethod(method);
		Set<RouteDefinition> routeDefinitions=new HashSet<>();
		int length = element.getChildNodes().getLength();
		for (int i = 0; i < length; i++) {
			org.w3c.dom.Node node = element.getChildNodes().item(i);
			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				Element e = (Element) node;
				if (Tag.ROUTING.getTagName().equals(e.getTagName())) {
					RouteDefinition routeDefinition=new RouteDefinition();
					routeDefinition.setValue(e.getAttribute(VALUE_ATTRIBUTE));
					routeDefinition.setToTask(e.getAttribute(TagAttribute.TASK_ROUTING_TO_TASK.NAME));
					routeDefinition.setPatten(e.getAttribute(TagAttribute.TASK_ROUTING_PATTEN.NAME));
					routeDefinitions.add(routeDefinition);
				}
			}
		}
		taskDefinition.setRouteDefinitions(routeDefinitions);
		return registerTask(parserContext.getRegistry(), taskDefinition);
	}
}
