package taskflow.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import taskflow.constants.PropertyNameAndValue;

public class TaskFlowPropertySetterBean implements EnvironmentAware{
	public static final String NAME_IN_CONTEXT = "taskFlowPropertySetter";
	public static final String NAME_OF_INIT_METHOD = "initProperties";
	
	private Environment environment;

	public void initProperties() {
		if (environment != null) {
			PropertyNameAndValue.setProperties(environment::getProperty);
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
