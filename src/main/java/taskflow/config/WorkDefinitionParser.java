package taskflow.config;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.CLASS_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ID_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.NAME_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.VALUE_ATTRIBUTE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import taskflow.work.CustomRouteWork;
import taskflow.work.SequentialRouteWork;

/**
 * 解析<tf:work>标签<br/>
 * Work都是Proptype
 * Created by lizhou on 2017/3/14/014.<br/>
 * updated by fuli on 2018/5
 */
public class WorkDefinitionParser implements BeanDefinitionParser {
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String id = element.getAttribute(ID_ATTRIBUTE);
        String maxTasks = element.getAttribute(TagAttribute.MAX_TASKS.NAME);
        String clazz = element.getAttribute(CLASS_ATTRIBUTE);

        RootBeanDefinition work = new RootBeanDefinition();
        work.getPropertyValues().add(NAME_ATTRIBUTE, id);
        try {
            Class<?> workClass = Class.forName(clazz);
            work.setBeanClass(workClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("not found work class:" + clazz);
        }
        work.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        if(CustomRouteWork.class.isAssignableFrom(work.getBeanClass())) {//只有CustomRouteWork才解析start和finish
        	String start = element.getAttribute(TagAttribute.START.NAME);
            String finish = element.getAttribute(TagAttribute.FINISH.NAME);
        	
        	RuntimeBeanReference startBean = new RuntimeBeanReference(start);
            work.getPropertyValues().add(TagAttribute.START.NAME, startBean);
            if (!StringUtils.isEmpty(finish)) {
                RuntimeBeanReference finishBean = new RuntimeBeanReference(finish);
                work.getPropertyValues().add(TagAttribute.FINISH.NAME, finishBean);
            }
        }else if(SequentialRouteWork.class.isAssignableFrom(work.getBeanClass())) {//只有SerialRouteWork才解析sequence
        	Map<String, String> extraMap = new HashMap<>();
        	ManagedMap<String, RuntimeBeanReference> tasksMap=new ManagedMap<>();
        	int length = element.getChildNodes().getLength();
    		for (int i = 0; i < length; i++) {
    			org.w3c.dom.Node node = element.getChildNodes().item(i);
    			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					Element elm = (Element) node;
    				String taskRef=elm.getAttribute(VALUE_ATTRIBUTE);
    				if(StringUtils.isEmpty(taskRef)) {
    					throw new NullPointerException("task-ref can not be empty");
    				}
    				tasksMap.put(taskRef, new RuntimeBeanReference(taskRef));
					String extra = elm.getAttribute(TagAttribute.EXTRA.NAME);
					extraMap.put(taskRef, extra);
    			}
    		}
			work.getPropertyValues().add(TagAttribute.TASKS.NAME, tasksMap);
			work.getPropertyValues().add("extraMap", extraMap);
        }
        work.getPropertyValues().add(TagAttribute.MAX_TASKS.NAME, Integer.valueOf(maxTasks));
        
        BeanDefinitionHolder holder = new BeanDefinitionHolder(work, id);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
        return work;
    }
}
