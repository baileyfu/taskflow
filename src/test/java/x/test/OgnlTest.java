package x.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ognl.Ognl;

public class OgnlTest {
	private int age;
	private String name;
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void say(String content) {
		System.out.println(content);
	}
	public int cal(int plus) {
		return age+plus;
	}
	public static String append(OgnlTest ot,String str) {
		return ot.name+str;
	}
	static Logger logger=LoggerFactory.getLogger(OgnlTest.class);
	public static void main(String[] args) throws Exception {
		System.out.println(OgnlTest.class.getResource("/").getPath());
		System.out.println(OgnlTest.class.getClassLoader().getResource("").getPath());
		OgnlTest ot=new OgnlTest();
		ot.age=10;
		ot.name="NAME";
		Map<String,Object> context=new HashMap<>();
		context.put("age", 100);
		System.out.println(Ognl.getValue("1",context,ot)+" : "+Ognl.getValue("age",context,ot)+" : "+Ognl.getValue("#age",context,ot));
		System.out.println(Ognl.getValue("#@java.util.LinkedHashMap@{\"name\" : \"007\", \"age\" : 45}", null));
		System.out.println(Ognl.getValue("say(\"AAA\")", ot));
		System.out.println(Ognl.getValue("cal(2)", ot));
		context.put("this",ot);
		System.out.println(Ognl.getValue("@x.test.OgnlTest@append(#this,\"KKK\")", context,ot));
		//#this为一特殊变量，指示前一段表达式的值
		System.out.println(Ognl.getValue("(#count=100).(#this>100 ? 2*#this : 20+#this)", null));
		System.out.println(Ognl.getValue("#root", ot));
		logger.debug("Logger debug!!!");
		logger.info("Logger info!!!");
	}

}
