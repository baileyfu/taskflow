package experiment.jdkproxy;

import java.lang.reflect.Proxy;

//类必须有实现的接口，而生成的代理类也只能代理某个类接口定义的方法，类自己定义的方法在产生的动态代理类中不会有这个方法了！
//如果类没有实现接口，那么这个类就不能用JDK产生动态代理了
public class JDKProxyRunner {

	public static void main(String[] args) {
		SubjectReal sub=new SubjectReal();
		SubjectRealInvocationHandler invocationHandler=new SubjectRealInvocationHandler(sub);
		Object proxySub=Proxy.newProxyInstance(sub.getClass().getClassLoader(), sub.getClass().getInterfaces(), invocationHandler);
		((SubjectA)proxySub).say("Hangzhou");
		((SubjectB)proxySub).run();
		//失败；代理类不能转换为具体类,只能是接口
//		String value=((SubjectReal)proxySub).notBeInvocated();
		String value=sub.notBeInvocated();
		System.out.println(value);
	}

}
