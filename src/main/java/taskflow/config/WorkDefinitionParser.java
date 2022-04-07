package taskflow.config;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.CLASS_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ID_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.VALUE_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.INDEX_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.TYPE_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.NAME_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.REF_ATTRIBUTE;

import java.util.ArrayList;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import taskflow.config.bean.WorkDefinition;
import taskflow.config.bean.WorkDefinition.ConstructorArg;
import taskflow.config.bean.WorkDefinition.TaskRef;
import taskflow.config.register.WorkRegister;
import taskflow.enums.ConfigSource;
import taskflow.enums.Tag;
import taskflow.enums.TagAttribute;

/**
 * 解析<tf:work>标签<br/>
 * Work都是Proptype
 * Created by lizhou on 2017/3/14/014.<br/>
 * updated by fuli on 2018/5
 */
public class WorkDefinitionParser implements BeanDefinitionParser,WorkRegister {
	public BeanDefinition parse(Element element, ParserContext parserContext) {
        String id = element.getAttribute(ID_ATTRIBUTE);
        String maxTasks = element.getAttribute(TagAttribute.WORK_MAX_TASKS.NAME);
        String clazz = element.getAttribute(CLASS_ATTRIBUTE);
		String traceable = element.getAttribute(TagAttribute.WORK_TRACEABLE.NAME);

        WorkDefinition workDefinition=new WorkDefinition();
        workDefinition.setWorkId(id);
        workDefinition.setWorkClazz(clazz);
		String startWork = element.getAttribute(TagAttribute.WORK_STARTWORK.NAME);
		String start = element.getAttribute(TagAttribute.WORK_START.NAME);
		if (!StringUtils.isEmpty(startWork)) {
			workDefinition.setStartWork(true);
			workDefinition.setStart(startWork);
			Assert.isTrue(StringUtils.isEmpty(start), "error on work '"+id+"' : start or startWork only one should be selected!");
		}else {
			workDefinition.setStart(start);
		}
		String finishWork = element.getAttribute(TagAttribute.WORK_FINISHWORK.NAME);
		String finish = element.getAttribute(TagAttribute.WORK_FINISH.NAME);
		if (!StringUtils.isEmpty(finishWork)) {
			workDefinition.setFinishWork(true);
			workDefinition.setFinish(finishWork);
			Assert.isTrue(StringUtils.isEmpty(finish), "error on work '"+id+"' : finish or finishWork only one should be selected!");
		}else {
			workDefinition.setFinish(finish);
		}
        workDefinition.setMaxTasks(NumberUtils.toInt(maxTasks,0));
        workDefinition.setTraceable(BooleanUtils.toBoolean(traceable));
		ArrayList<ConstructorArg> constructorArgs = new ArrayList<>();
        ArrayList<TaskRef> taskRefs=new ArrayList<>();
        int length = element.getChildNodes().getLength();
		for (int i = 0; i < length; i++) {
			org.w3c.dom.Node node = element.getChildNodes().item(i);
			if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				Element elm = (Element) node;
				if (Tag.TASK_REF.getTagName().equalsIgnoreCase(elm.getTagName())) {
					TaskRef taskRef=new TaskRef();
					taskRef.setTaskId(elm.getAttribute(VALUE_ATTRIBUTE));
					taskRef.setExtra(elm.getAttribute(TagAttribute.TASK_EXTRA.NAME));
					taskRef.setAsync(Boolean.valueOf(elm.getAttribute(TagAttribute.TASK_ASYNC.NAME)));
					taskRefs.add(taskRef);
				} if (Tag.WORK_REF.getTagName().equalsIgnoreCase(elm.getTagName())) {
					TaskRef taskRef=new TaskRef();
					taskRef.setItWork(true);
					taskRef.setTaskId(elm.getAttribute(VALUE_ATTRIBUTE));
					taskRef.setAsync(Boolean.valueOf(elm.getAttribute(TagAttribute.TASK_ASYNC.NAME)));
					taskRefs.add(taskRef);
				} else if (Tag.CONSTRUCTOR_ARG.getTagName().equalsIgnoreCase(elm.getTagName())) {
					ConstructorArg constructorArg = new ConstructorArg();
					constructorArg.setIndex(NumberUtils.toInt(elm.getAttribute(INDEX_ATTRIBUTE), 0));
					constructorArg.setName(elm.getAttribute(NAME_ATTRIBUTE));
					constructorArg.setType(elm.getAttribute(TYPE_ATTRIBUTE));
					constructorArg.setValue(elm.getAttribute(VALUE_ATTRIBUTE));
					constructorArg.setRef(elm.getAttribute(REF_ATTRIBUTE));
					constructorArgs.add(constructorArg);
				}
			}
		}
		workDefinition.setConstructorArgs(constructorArgs);
        workDefinition.setTaskRefs(taskRefs);
        return registerWork(parserContext.getRegistry(), workDefinition);
    }

	@Override
	public ConfigSource getConfigSource() {
		return ConfigSource.XML;
	}
}
