package x.test.spi.impl;

import x.test.spi.SomeService;

public class ServiceB implements SomeService{
	static {
		ServiceManager.registerService(new ServiceB());
	}
	
	public void sayHello(String name) {
		System.out.println("ServiceB say : hello "+name);
	}
}
