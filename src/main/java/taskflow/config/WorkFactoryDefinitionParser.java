package taskflow.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import taskflow.config.bean.TaskFlowPropertySetterBean;
import taskflow.config.register.WorkRegister;
import taskflow.enums.ConfigSource;
import taskflow.work.WorkFactory;

/**
 * 处理<workFactory>标签，开启该标签以后才能使用{@link WorkFactory}
 * Created by lizhou on 2017/3/14/014.
 */
public class WorkFactoryDefinitionParser implements BeanDefinitionParser,WorkRegister {
    public BeanDefinition parse(Element element, ParserContext parserContext) {
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		// 兼职注册TaskFlowPropertySetterBean
		if (!registry.containsBeanDefinition(TaskFlowPropertySetterBean.NAME_IN_CONTEXT)) {
			RootBeanDefinition taskFlowPropertySetter=new RootBeanDefinition(TaskFlowPropertySetterBean.class);
			taskFlowPropertySetter.setInitMethodName(TaskFlowPropertySetterBean.NAME_OF_INIT_METHOD);
			BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(taskFlowPropertySetter, TaskFlowPropertySetterBean.NAME_IN_CONTEXT), registry);
		}
        return registerWorkFactory(registry);
    }

	@Override
	public ConfigSource getConfigSource() {
		return ConfigSource.XML;
	}
}
