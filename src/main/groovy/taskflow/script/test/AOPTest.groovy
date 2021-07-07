package taskflow.script.test

import groovy.ui.SystemOutputInterceptor

/**
 * 基于MOP的AOP实现（一）
 * 对象拦截:实现GroovyInterceptable接口即可
 */
class AOPOne implements GroovyInterceptable{
	def sayHello(){
		'Hello world'
	}
	//不管方法是否存在,首先调用此方法
	public Object invokeMethod(String name, Object args) {
		if('sayHello'==name) {
			//实现AOP
			System.out.println("the AOPOne's method ${name} be intercepted")
			return AOPOne.metaClass.getMetaMethod(name).invoke(this, null)
		}
		def validMethod=AOPOne.metaClass.getMetaMethod(name, args)
		if(validMethod!=null) {
			return validMethod.invoke(this,args);
		}
		return name;
	}
}
AOPOne ao=new AOPOne();
println ao.sayHello()
println ao.sayGoodbye()
/**
 * 基于MOP的AOP实现（二）
 * MetaClass拦截,适用于非目标类作者/目标类是Java类/想动态拦截的情况
 */
class AOPTwo{
	def sayHello(){
		'Hello world'
	}
}
AOPTwo.metaClass.invokeMethod= {String name,def args->
	//delegate指向要拦截其方法的目标对象
	if('sayHello'==name) {
		//实现AOP
		System.out.println("the AOPTwo's method ${name} be intercepted")
		return AOPTwo.metaClass.getMetaMethod(name,args).invoke(delegate, args)
	}
	def validMethod=AOPTwo.metaClass.getMetaMethod(name, args)
	if(validMethod!=null) {
		return validMethod.invoke(delegate,args);
	}else {
		//会抛groovy.lang.MissingMethodException
		//return AOPTwo.metaClass.invokeMissingMethod(delegate,name,args)
		return 'missingMethod : '+name
	}
}
AOPTwo at=new AOPTwo()
println at.sayHello()
println at.sayGoodbye()

String.metaClass.invokeMethod{String name,def args->
	//利用元编程对String.toUpperCase方法拦截,使其失效
	if('toUpperCase'==name) {
		return delegate
	}
	def validMethod=String.metaClass.getMetaMethod(name, args)
	if(validMethod!=null) {
		return validMethod.invoke(delegate,args);
	}else {
		return String.metaClass.invokeMissingMethod(delegate,name,args)
	}
}
println 'heLLo'.toLowerCase()
println 'heLLo'.toUpperCase()
