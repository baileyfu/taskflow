package taskflow.config;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ID_ATTRIBUTE;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import taskflow.pattern.PatternType;
import taskflow.routing.impl.DefaultRouting;
import taskflow.routing.impl.PatternRoutingCondition;
import taskflow.task.DefaultTaskRoutingWrap;
import taskflow.task.ReflectedTaskRoutingWrap;
import taskflow.task.TaskMethodInvoker;
import taskflow.task.TaskRoutingWrap;

/**
 * 处理<tf:task>标签 对于ref的Task，使用{@link TaskRoutingWrap}进行包装 <br/>
 * Created by lizhou on 2017/3/14/014. <br/>
 * updated by fuli on 2018/5 <br/>
 */
public class TaskDefinitionParser implements BeanDefinitionParser {
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String id = element.getAttribute(ID_ATTRIBUTE);
		String ref = element.getAttribute("ref");
		String method = element.getAttribute("method");

		// 解析ref属性，因为ref引用的也是一个TaskRoutingWrap,可能在这里还未注册
		// 因此使用RuntimeBeanReference
		RuntimeBeanReference taskRef = new RuntimeBeanReference(ref);

		RootBeanDefinition taskRoutingWrapDefinition = new RootBeanDefinition();
		// 解析routing condition
		ManagedList<BeanDefinition> routingConditions = new ManagedList<>();
		int length = element.getChildNodes().getLength();
		for (int i = 0; i < length; i++) {
			org.w3c.dom.Node node = element.getChildNodes().item(i);
			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				Element e = (Element) node;
				if (Tag.ROUTING.getTagName().equals(e.getTagName())) {
					routingConditions.add(dealHeadRouting(e));
				}
			}
		}
		RootBeanDefinition routing = new RootBeanDefinition();
		routing.setBeanClass(DefaultRouting.class);
		routing.getPropertyValues().add("routingConditions", routingConditions);

		taskRoutingWrapDefinition.getPropertyValues().add("routing", routing);
		// 设置了自定义方法
		if (StringUtils.isNotBlank(method)) {
			taskRoutingWrapDefinition.setBeanClass(ReflectedTaskRoutingWrap.class);
			
			RootBeanDefinition taskMethodInvokerBeanDefinition = new RootBeanDefinition();
			taskMethodInvokerBeanDefinition.setBeanClass(TaskMethodInvoker.class);
			ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
			constructorArgumentValues.addIndexedArgumentValue(0, taskRef);
			constructorArgumentValues.addIndexedArgumentValue(1, method);
			taskMethodInvokerBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);
			taskRoutingWrapDefinition.getPropertyValues().add("taskMethodInvoker", taskMethodInvokerBeanDefinition);
		} else {
			taskRoutingWrapDefinition.setBeanClass(DefaultTaskRoutingWrap.class);
			taskRoutingWrapDefinition.getPropertyValues().add("task", taskRef);
		}

		BeanDefinitionHolder holder = new BeanDefinitionHolder(taskRoutingWrapDefinition, id);
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
		return taskRoutingWrapDefinition;
	}

	private BeanDefinition dealHeadRouting(Element e) {
		String value = e.getAttribute("value");
		String to = e.getAttribute("to");
		// 默认是string
		String patten = e.getAttribute("patten");
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
		// 使用Pattern匹配路由
		rootBeanDefinition.setBeanClass(PatternRoutingCondition.class);
		rootBeanDefinition.getPropertyValues().add("condition", value);
		rootBeanDefinition.getPropertyValues().add("pattern", PatternType.valueOf(patten));
		rootBeanDefinition.getPropertyValues().add("taskRoutingWrap", new RuntimeBeanReference(to));

		return rootBeanDefinition;
	}
}
