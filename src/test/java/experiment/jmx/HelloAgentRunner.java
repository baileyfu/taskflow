package experiment.jmx;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

//Agent层,MBeanServer
public class HelloAgentRunner {

	public static void main(String[] args) throws Exception{
		//MBeanServer是MBean的运行容器
		MBeanServer server=ManagementFactory.getPlatformMBeanServer();
		//规范(域名:name=MBean名称)
		//域名和MBean的名称可以任意取。唯一标识MBean的实现类
		ObjectName mbeanName = new ObjectName("jmxBean:name=hello");
		//注册mbean
		Hello hello=new Hello();
		server.registerMBean(hello, mbeanName);
		
		ObjectName notifiableMBeanName=new ObjectName("jmxBean:name=notifiable");
		Notifiable notifiable=new Notifiable();
		//可以将MBean实现类扩展为NotificationListener，方便注册监听器
		notifiable.addNotificationListener(new CommonNotificationListener(), new CommonNotificationFilter(), hello);
		server.registerMBean(notifiable, notifiableMBeanName);
		
		//配置Adaptor提供其它访问方式;Adaptor也是个MBean;由jdmk提供
//		ObjectName adapterName = new ObjectName("MyJMXAgent:name=htmladapter,port=8082");   
//        HtmlAdaptorServer adapter = new HtmlAdaptorServer(); 
//        server.registerMBean(adapter, adapterName);  
//        adapter.start();
        
        //远程方式
		//注册一个端口，绑定url后用于客户端通过rmi方式连接JMXConnectorServer
		LocateRegistry.createRegistry(9999);
		//URL路径的结尾可以随意指定，但如果需要用Jconsole来进行连接，则必须使用jmxrmi
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
		JMXConnectorServer jcs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
        System.out.println("begin rmi start");
        System.out.println("java.rmi.server.RMIClassLoaderSpi : "+System.getProperty("java.rmi.server.RMIClassLoaderSpi"));
        jcs.start();
	}

}
