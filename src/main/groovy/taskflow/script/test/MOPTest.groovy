package taskflow.script.test

/**
 * 元对象编程(MetaObject Protocol)
 * 可以用于动态调用方法，并且可以即时创建类和方法
 * 原理：实现GroovyInterceptable接口挂钩到Groovy的执行过程
 * 对于POJO，Groovy维护了一个MetaClassRegistry，而POGO有一个到其MetaClass的直接引用
 */
class Teacher{
	def x
	protected dynamicProps=[:]
	/** 缺失属性实现元编程 */
	//1.有一个称为dynamicProps的参数，将用于保存即时创建的成员变量的值
	//2.方法getproperty和setproperty已被实现以在运行时获取和设置类的属性的值
	void setProperty(String pName,val) {
	   dynamicProps[pName] = val
	}	
	def getProperty(String pName) {
	   dynamicProps[pName]
	}
	/** 缺失方法实现元编程 */
	//实现GroovyInterceptable接口的invokeMethod方法，它被调用，而不管该方法是否存在
	//否则当方法缺失时会优先执行methodMissing
	def invokeMethod(String name, def args) {
		"called invokeMethod $name $args"
	}
	//Groovy支持methodMissing的概念
	//此方法与invokeMethod的不同之处在于，它仅在失败的方法分派的情况下被调用，当没有找到给定名称和/或给定参数的方法时
	def methodMissing(String name, def args) {
		"Missing method"
	}
}
Teacher s=new Teacher();
s.a='name'
s.b=1
println s.a + '-' + s.x
println s.showName(1,2,'3')
/**
 * 元类MetaClass
 * */
def str='hello'
def methodName='toUpperCase'
def mi=str.metaClass.getMetaMethod(methodName)
println mi.invoke(str)
///元方法
println String.metaClass.getMetaMethods()
println String.metaClass.getStaticMetaMethod('toUpperCase', 'testParameter')
//确定对象是否会响应方法的调用,指定参数类型
println String.metaClass.respondsTo(str,'toUpperCase',1)
println String.metaClass.respondsTo(str,'compareTo','test')

///元属性
println String.metaClass.getMetaProperty('value')
println String.metaClass.hasProperty('value')
//属性/方法访问的方式
println str.properties
println str['bytes']
def v='toUpperCase'
def vp='latin1'
println str."$v"()
println str.invokeMethod(v,null)
println str."$vp"