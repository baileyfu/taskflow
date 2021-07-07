package x.test.spi;

import x.test.spi.impl.ServiceManager;

public class SpiRunner {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Running...");
		//模仿JDBC获取Connection
		SomeService service=ServiceManager.getService();
		service.sayHello("Boo");
		service=ServiceManager.getService("get another instance");
		service.sayHello("Foo");
	}

}
