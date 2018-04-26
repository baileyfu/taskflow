package x.demo;


import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        
        StatusHolderBus testBus = context.getBean(StatusHolderBus.class,"15888888888");
		System.out.println(testBus);
//        System.out.println(testBus.getClass());
        testBus.run();
        
        
//        System.out.println(testBus.getCacheName("AAA"));
//        System.out.println(testBus.getCacheName("AAA"));
        Thread.sleep(1000l);
        System.out.println("------------");
        testBus.run();
        
        context.close();
    }
}
