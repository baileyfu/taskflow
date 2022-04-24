package taskflow.config;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ID_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.REF_ATTRIBUTE;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskDefinition.RouteDefinition;
import taskflow.config.bean.TaskDefinition.TaskWrapperDefinition;
import taskflow.config.register.TaskRegister;
import taskflow.enums.ConfigSource;
import taskflow.enums.Tag;
import taskflow.enums.TagAttribute;
import taskflow.task.routing.TaskRoutingWrap;

/**
 * 处理<tf:task>标签 对于ref的Task，使用{@link TaskRoutingWrap}进行包装 <br/>
 * 处理<tf:taskWrapper>标签 对于ref的Work，包装成{@link taskflow.task.TaskWrapper}，然后再使用{@link TaskRoutingWrap}进行包装 <br/>
 * Created by lizhou on 2017/3/14/014. <br/>
 * updated by fuli on 2018/5 <br/>
 */
public class TaskDefinitionParser implements BeanDefinitionParser,TaskRegister {
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		if (element.getTagName().equals(Tag.TASKWRAPPER.getTagName())) {
			return parseTaskwrapper(element, parserContext);
		}
		String id = element.getAttribute(ID_ATTRIBUTE);
		String ref = element.getAttribute(REF_ATTRIBUTE);
		String method = element.getAttribute(TagAttribute.TASK_METHOD.NAME);
		String extra = element.getAttribute(TagAttribute.TASK_EXTRA.NAME);
		
		TaskDefinition taskDefinition=new TaskDefinition();
		taskDefinition.setTaskId(id);
		taskDefinition.setTaskBeanId(ref);
		taskDefinition.setMethod(method);
		taskDefinition.setExtra(extra);
		taskDefinition.setRouteDefinitions(parseRouting(element));
		return registerTask(parserContext.getRegistry(), taskDefinition);
	}

	private BeanDefinition parseTaskwrapper(Element element, ParserContext parserContext) {
		String id = element.getAttribute(ID_ATTRIBUTE);
		String refWork = element.getAttribute(TagAttribute.TASKWRAPPER_REF_WORK.NAME);
		String resultKey = element.getAttribute(TagAttribute.TASKWRAPPER_RESULT_KEY.NAME);
		
		TaskWrapperDefinition taskWrapperDefinition = new TaskWrapperDefinition();
		taskWrapperDefinition.setTaskId(id);
		taskWrapperDefinition.setRefWork(refWork);
		taskWrapperDefinition.setResultKey(resultKey);
		
		taskWrapperDefinition.setRouteDefinitions(parseRouting(element));
		return registerTask(parserContext.getRegistry(), taskWrapperDefinition);
	}
	
	private Set<RouteDefinition> parseRouting(Element element){
		Set<RouteDefinition> routeDefinitions=new HashSet<>();
		int length = element.getChildNodes().getLength();
		for (int i = 0; i < length; i++) {
			org.w3c.dom.Node node = element.getChildNodes().item(i);
			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				Element e = (Element) node;
				if (Tag.ROUTING.getTagName().equals(e.getTagName())) {
					RouteDefinition routeDefinition=new RouteDefinition();
					routeDefinition.setKey(e.getAttribute(TagAttribute.TASK_ROUTING_KEY.NAME));
					routeDefinition.setToTask(e.getAttribute(TagAttribute.TASK_ROUTING_TO_TASK.NAME));
					routeDefinition.setPattern(e.getAttribute(TagAttribute.TASK_ROUTING_PATTERN.NAME));
					routeDefinition.setExtra(e.getAttribute(TagAttribute.TASK_ROUTING_EXTRA.NAME));
					routeDefinitions.add(routeDefinition);
				}
			}
		}
		return routeDefinitions;
	}
	
	@Override
	public ConfigSource getConfigSource() {
		return ConfigSource.XML;
	}
}
