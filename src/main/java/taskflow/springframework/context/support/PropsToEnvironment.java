package taskflow.springframework.context.support;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * 将配置信息加入到Environment
 * 
 * @author bailey
 * @date 2022年2月14日
 */
public class PropsToEnvironment extends PropertySourcesPlaceholderConfigurer{
	private ConfigurableEnvironment environment;
	private String beanName;
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment)environment;
		super.setEnvironment(environment);
	}

	@Override
	protected Properties mergeProperties() throws IOException {
		Properties props = super.mergeProperties();
		((ConfigurableEnvironment)environment).getPropertySources().addFirst(new PropertiesPropertySource(beanName,props));
		return props;
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
		super.setBeanName(beanName);
	}


}
