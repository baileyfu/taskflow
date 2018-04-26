package x.demo;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import taskflow.bus.BusFactory;
import xcache.core.CacheManager;

/**
 * Created by lizhou on 2017/3/14/014.
 */
public class DemoApplication {
	
	
    public static void main(String[] args) throws Exception{
        //input list or number
        //1. find Maximum and Minimum
        //2. write file
        //3. The difference between the two numbers is less than 10, print ok , else print no
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:status-bus-config.xml");

//        Bus testBus = BusFactory.createNewBus("testBus");
//        List<Integer> input = Arrays.asList(5, 7, 1, 0, 1, 3, 4, 5, 6, 4);
//        testBus.putContext("intList", input);
//        testBus.run();

        CacheManager cm=CacheManager.getInstance();
        cm.putToLocal("A", "V");
        cm.putToRemote("T", "V");
        
        System.out.println(cm.getLocal("A"));
        System.out.println(cm.getRemote("T"));
        
        StatusHolderBus testBus = (StatusHolderBus)BusFactory.createNewBus("testStatusBus");
        System.out.println(testBus.getClass());
        testBus.setUserName("158");
        testBus.run();
        
        
        System.out.println(testBus.getCacheName("AAA"));
        System.out.println(testBus.getCacheName("AAA"));
        Thread.sleep(1000l);
        System.out.println("------------");
        testBus.run();
    }
}
