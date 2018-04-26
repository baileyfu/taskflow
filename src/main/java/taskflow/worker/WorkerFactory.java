package taskflow.worker;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 使用工厂模式创建bus,每次创建都是一个全新的Bus
 * 该bean在定义的时候是SCOPE_PROTOTYPE
 * Created by lizhou on 2017/4/8/008.
 */
public class WorkerFactory implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        WorkerFactory.beanFactory = beanFactory;
    }

    public static Worker createNewBus(String id) {
        return beanFactory.getBean(id, Worker.class);
    }
}
