package x.demo;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import taskflow.worker.WorkerFactory;
import xcache.core.CacheManager;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public class DemoApplication {
	
	
    public static void main(String[] args) throws Exception{
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:status-bus-config.xml");

//        CacheManager cm=CacheManager.getInstance();
//        cm.putToLocal("A", "V");
//        cm.putToRemote("T", "V");
//        
//        System.out.println(cm.getLocal("A"));
//        System.out.println(cm.getRemote("T"));
        
        StatusHolderBus testBus = (StatusHolderBus)WorkerFactory.createNewBus("testStatusBus");
//        System.out.println(testBus.getClass());
        testBus.setUserName("158");
        testBus.run();
        
        
//        System.out.println(testBus.getCacheName("AAA"));
//        System.out.println(testBus.getCacheName("AAA"));
        Thread.sleep(1000l);
        System.out.println("------------");
        testBus.run();
    }
}
