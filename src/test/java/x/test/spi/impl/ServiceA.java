package x.test.spi.impl;

import x.test.spi.SomeService;

public class ServiceA implements SomeService{
	static {
		ServiceManager.registerService(new ServiceA());
	}
	public void sayHello(String name) {
		System.out.println("ServiceA say : hello "+name);
	}
}
