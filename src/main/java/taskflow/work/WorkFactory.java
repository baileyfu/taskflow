package taskflow.work;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 手动创建Work
 */
public class WorkFactory implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        WorkFactory.beanFactory = beanFactory;
    }

    public static Work createNewBus(String id) {
        return beanFactory!=null?beanFactory.getBean(id, Work.class):null;
    }
}
