package experiment.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SubjectRealInvocationHandler implements InvocationHandler{
	SubjectReal sub;
	public SubjectRealInvocationHandler(SubjectReal sub) {
		this.sub=sub;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(method.getName()+" before...");
		Object result=method.invoke(sub, args);
		System.out.println(method.getName()+" after..."+result);
		return result;
	}

}
