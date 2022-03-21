package taskflow.work;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Work工厂,利用Spring容器创建Work
 */
public class WorkFactory implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    	WorkFactory.beanFactory = beanFactory;
    }
    @SuppressWarnings("unchecked")
	public static <T extends Work>T createWork(String workId) {
    	return beanFactory!=null?(T)beanFactory.getBean(workId, Work.class):null;
    }
}
