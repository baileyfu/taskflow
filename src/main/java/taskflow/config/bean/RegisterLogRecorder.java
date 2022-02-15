package taskflow.config.bean;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import taskflow.config.TaskFlowBeanReloadProcessor;
import taskflow.config.register.ConfigSourceAware;
import taskflow.constants.PropertyNameAndValue;
import taskflow.constants.TFLogType;
import taskflow.logger.ConsoleLogger;
import taskflow.logger.TFLogger;

public class RegisterLogRecorder implements BeanFactoryAware{
	public static final String NAME_OF_INIT_METHOD = "init";
	protected DefaultListableBeanFactory beanFactory;
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		Assert.isTrue(beanFactory instanceof DefaultListableBeanFactory,"the TaskFlow requires DefaultListableBeanFactory");
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
	private TFLogger registerLogger = null;
	public void init() {
		if (registerLogger == null) {
			Map<String, TFLogger> rlMap = beanFactory.getBeansOfType(TFLogger.class);
			if (CollectionUtils.isEmpty(rlMap)) {
				registerLogger = new ConsoleLogger();
			} else {
				registerLogger = rlMap.values().iterator().next();
			}
		}
	}

	protected void printRegisterLog(boolean printDetail) {
		if (registerLogger != null && Boolean.getBoolean(PropertyNameAndValue.LOG_PRINTABLE)) {
			if (ConfigSourceAware.REGISTER_LOG.size() > 0) {
				boolean reload=getClass()==TaskFlowBeanReloadProcessor.class;
				registerLogger.log("Print " + (reload ? "reload" : "register") + " list");
				if (printDetail && Boolean.getBoolean(PropertyNameAndValue.LOG_PRINT_DETAIL)) {
					ConfigSourceAware.REGISTER_LOG.values().stream().forEach(registerLogger::log);
				} else {
					for (String id : ConfigSourceAware.REGISTER_LOG.keySet()) {
						registerLogger.log(StringUtils.substringBefore(ConfigSourceAware.REGISTER_LOG.get(id), "[VALUE]")+"[VALUE] : "+id);
					}
				}
				registerLogger.log((reload?"Reload":"Register")+" Summary : [TaskBean : "+ConfigSourceAware.REGISTER_COUNT.get(TFLogType.TASK_BEAN)+" , Task : "+ConfigSourceAware.REGISTER_COUNT.get(TFLogType.TASK)+" , Work : "+ConfigSourceAware.REGISTER_COUNT.get(TFLogType.WORK)+"]");
			}
		}
		ConfigSourceAware.REGISTER_LOG.clear();
		ConfigSourceAware.REGISTER_COUNT.clear();
	}
	protected void log(String content) {
		if (registerLogger != null && Boolean.getBoolean(PropertyNameAndValue.LOG_PRINTABLE)) {
			registerLogger.log(content);
		}
	}
	
	protected void directlyLog(String content) {
		registerLogger.log(content);
	}
}
