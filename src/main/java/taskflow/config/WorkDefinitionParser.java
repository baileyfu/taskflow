package taskflow.config;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ID_ATTRIBUTE;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
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
        String maxTasks = element.getAttribute("maxTasks");
        String clazz = element.getAttribute("class");

        RootBeanDefinition work = new RootBeanDefinition();
        try {
            Class<?> workClass = Class.forName(clazz);
            work.setBeanClass(workClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("not found work class:" + clazz);
        }
        //保证每次获取的都是新的对象
        work.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);

        if(work.getBeanClass()==CustomRouteWork.class) {//只有CustomRouteWork才解析start和finish
        	String start = element.getAttribute("start");
            String finish = element.getAttribute("finish");
        	
        	RuntimeBeanReference startBean = new RuntimeBeanReference(start);
            work.getPropertyValues().add("start", startBean);
            if (!StringUtils.isEmpty(finish)) {
                RuntimeBeanReference finishBean = new RuntimeBeanReference(finish);
                work.getPropertyValues().add("finish", finishBean);
            }
        }else if(work.getBeanClass()==SequentialRouteWork.class) {//只有SerialRouteWork才解析sequence
        	ManagedMap<String, RuntimeBeanReference> tasksMap=new ManagedMap<>();
        	int length = element.getChildNodes().getLength();
    		for (int i = 0; i < length; i++) {
    			org.w3c.dom.Node node = element.getChildNodes().item(i);
    			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
    				String taskRef=((Element) node).getAttribute("value");
    				if(StringUtils.isEmpty(taskRef)) {
    					throw new NullPointerException("task-ref can not be empty");
    				}
    				tasksMap.put(taskRef, new RuntimeBeanReference(taskRef));
    			}
    		}
        	ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
			constructorArgumentValues.addIndexedArgumentValue(0, tasksMap);
			work.setConstructorArgumentValues(constructorArgumentValues);
        }
        work.getPropertyValues().add("maxTasks", Integer.valueOf(maxTasks));
        
        BeanDefinitionHolder holder = new BeanDefinitionHolder(work, id);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
        return work;
    }


}
