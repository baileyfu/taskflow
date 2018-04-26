package taskflow.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import taskflow.worker.WorkerFactory;

/**
 * 处理<workerFactory>标签，开启该标签以后才能使用{@link WorkerFactory}
 * Created by lizhou on 2017/3/14/014.
 */
public class WorkerFactoryDefinitionParser implements BeanDefinitionParser {
    public BeanDefinition parse(Element element, ParserContext parserContext) {


        RootBeanDefinition workerFactory = new RootBeanDefinition();
        workerFactory.setBeanClass(WorkerFactory.class);

        BeanDefinitionHolder holder = new BeanDefinitionHolder(workerFactory, WorkerFactory.class.getSimpleName());
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
        return workerFactory;
    }


}
