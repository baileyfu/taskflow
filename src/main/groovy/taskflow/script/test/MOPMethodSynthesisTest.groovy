package taskflow.script.test

/**
 * MOP方法合成
 * 适用情况：在调用时动态地确认方法的行为（合成的方法直到调用时才会作为独立的方法存在）
 *	“拦截，缓存，调用”*/
/*（一）		使用methodMissing/propertyMissing
 *拦截不存在方法的调用从而动态添加行为
 *如果对象实现了GroovyInterceptable接口，则无论方法是否存在都会调用其实现的invokeMethod，只有当对象将控制权转移给其
 *MetaClass的invokeMethod时，methodMissing才会被调用**/
class HardWorker implements GroovyInterceptable{//为演示所以实现GroovyInterceptable
	def work() {'working...'}
	def plays=['game','basketball','music']
	def methodMissing(String name,args) {
		def method=plays.find{it==name.split('play')[1].toLowerCase()}
		if(method) {
			def impl= {vargs->
				"playing ${name.split('play')[1]}..."
			}
			HardWorker instance=this
			instance.metaClass."${name}"=impl//将实现缓存到metaClass
			impl(args)
		}else {
			throw new MissingMethodException(name,HardWorker.class,args)
		}
	}
	def invokeMethod(String name,args) {
		def originalMethod=metaClass.getMetaMethod(name, args);
		if(originalMethod) {
			originalMethod.invoke(this,args)
		}else {
			//此时才可能会调用methodMissing
			metaClass.invokeMethod(this, name, args)
		}
	}
}
w=new HardWorker()
println w.work()
println w.playGame()
println w.playMusic()
println w.playGame()

/*（二）		使用ExpandoMetaClass
 * 当无权编辑源文件或非POGO类时使用*/



