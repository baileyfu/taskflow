package taskflow.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import taskflow.work.WorkFactory;

/**
 * 处理<workFactory>标签，开启该标签以后才能使用{@link WorkFactory}
 * Created by lizhou on 2017/3/14/014.
 */
public class WorkFactoryDefinitionParser implements BeanDefinitionParser {
    public BeanDefinition parse(Element element, ParserContext parserContext) {


        RootBeanDefinition workerFactory = new RootBeanDefinition();
        workerFactory.setBeanClass(WorkFactory.class);

        BeanDefinitionHolder holder = new BeanDefinitionHolder(workerFactory, WorkFactory.class.getSimpleName());
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
        return workerFactory;
    }


}
