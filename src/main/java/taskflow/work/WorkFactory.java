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
    public static Work createWork(String workId) {
    	return beanFactory!=null?beanFactory.getBean(workId, Work.class):null;
    }
}
