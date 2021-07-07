package experiment.jmx;

import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class RemoteClientRunner {

	public static void main(String[] args) throws Exception{
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
		Map<String, Object> environment = new HashMap<>();
	    //environment.put(JMXConnector.CREDENTIALS, new String[]{"admin", "password"});
		JMXConnector jmxc = JMXConnectorFactory.connect(url,environment);

		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
		String[] domains = mbsc.getDomains();
		System.out.println("domain...");
		for(String domain:domains) {
			System.out.println(domain);
		}
		System.out.println("Default domain:"+mbsc.getDefaultDomain());
		System.out.println("MBean count = " + mbsc.getMBeanCount());
		//ObjectName的名称与前面注册时候的保持一致
		ObjectName mbeanName = new ObjectName("jmxBean:name=hello");
		//设置指定Mbean的特定属性值
		//setAttribute、getAttribute操作只能针对bean的属性
		//例如对getName或者setName进行操作，只能使用Name，需要去除方法的前缀
		mbsc.setAttribute(mbeanName, new Attribute("Name","杭州"));
		System.out.println("MBean.hello.name:"+(String)mbsc.getAttribute(mbeanName, "Name"));
		//invoke调用bean的方法，只针对非设置属性的方法，例如invoke不能对getName方法进行调用
		mbsc.invoke(mbeanName, "say", new Object[] {"BBB"}, new String[] {String.class.getName()});
		
		HelloMBean proxy = MBeanServerInvocationHandler.newProxyInstance(mbsc, mbeanName, HelloMBean.class, false);
		proxy.setName("HangZhou");
		proxy.say("TOP");
		
		jmxc.close();
	}

}
