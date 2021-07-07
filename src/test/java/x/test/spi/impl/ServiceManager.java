package x.test.spi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import x.test.spi.SomeService;

public class ServiceManager {
	private static List<SomeService> services=new ArrayList<>();
	static {
		ServiceLoader<SomeService> loader = ServiceLoader.load(SomeService.class);
		loader.iterator().forEachRemaining((x)->{
		});
		
	}
	//由具体实现类初始化时注册自己
	static void registerService(SomeService service){
		ServiceManager.services.add(service);
	}
	public static SomeService getService() {
		return services.get(0);
	}
	
	public static SomeService getService(String someCondition) {
		return services.get(someCondition==null?0:1);
	}
	
}
