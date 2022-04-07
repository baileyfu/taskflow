package taskflow.config;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ID_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.REF_ATTRIBUTE;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import taskflow.config.bean.TaskDefinition;
import taskflow.config.bean.TaskDefinition.RouteDefinition;
import taskflow.config.register.TaskRegister;
import taskflow.enums.ConfigSource;
import taskflow.enums.Tag;
import taskflow.enums.TagAttribute;
import taskflow.task.routing.TaskRoutingWrap;

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
		String extra = element.getAttribute(TagAttribute.TASK_EXTRA.NAME);
		
		TaskDefinition taskDefinition=new TaskDefinition();
		taskDefinition.setTaskId(id);
		taskDefinition.setTaskBeanId(ref);
		taskDefinition.setMethod(method);
		taskDefinition.setExtra(extra);
		Set<RouteDefinition> routeDefinitions=new HashSet<>();
		int length = element.getChildNodes().getLength();
		for (int i = 0; i < length; i++) {
			org.w3c.dom.Node node = element.getChildNodes().item(i);
			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				Element e = (Element) node;
				if (Tag.ROUTING.getTagName().equals(e.getTagName())) {
					RouteDefinition routeDefinition=new RouteDefinition();
					routeDefinition.setKey(e.getAttribute(TagAttribute.TASK_ROUTING_KEY.NAME));
					String toWork = e.getAttribute(TagAttribute.TASK_ROUTING_TO_WORK.NAME);
					String toTask = e.getAttribute(TagAttribute.TASK_ROUTING_TO_TASK.NAME);
					if (!StringUtils.isEmpty(toWork)) {
						routeDefinition.setItWork(true);
						routeDefinition.setToTask(toWork);
						Assert.isTrue(StringUtils.isEmpty(toTask), "error on task '"+id+"' : toTask or toWork only one should be selected!");
					} else {
						routeDefinition.setToTask(toTask);
					}
					routeDefinition.setPattern(e.getAttribute(TagAttribute.TASK_ROUTING_PATTERN.NAME));
					routeDefinition.setExtra(e.getAttribute(TagAttribute.TASK_ROUTING_EXTRA.NAME));
					routeDefinitions.add(routeDefinition);
				}
			}
		}
		taskDefinition.setRouteDefinitions(routeDefinitions);
		return registerTask(parserContext.getRegistry(), taskDefinition);
	}

	@Override
	public ConfigSource getConfigSource() {
		return ConfigSource.XML;
	}
}
