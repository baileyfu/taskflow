package experiment;


import java.sql.Array;
import java.sql.Connection;
import java.util.Map;

import experiment.agent.B;

public class A extends B{
	
	public void say(String name){
		Class bClazz=null;
		try {
			bClazz = Class.forName("experiment.agent.B");
			Object a=new A();
			if(a instanceof experiment.agent.B) {
				
			}
		} catch (Exception e) {
			System.out.println(e.getClass());
			System.out.println(e.getClass().getSuperclass());
		}
		if(bClazz!=null) {
			System.out.println(new B().toString());
		}else {
			System.out.println("Hello world! "+name);
		}
	}
	
	public static void main(String[] args) throws Exception{
		A a=new A();
		a.say(args!=null&&args.length>0?args[0]:null);
		System.out.println(B.class.isAssignableFrom(B.class));
//		System.setProperty("jqf.tracing.TRACE_GENERATORS","true");
//		System.setProperty("AAA","100");
		System.out.println(Boolean.getBoolean("jqf.tracing.TRACE_GENERATORS")+"---"+Integer.getInteger("AAA"));
	}
	
}
