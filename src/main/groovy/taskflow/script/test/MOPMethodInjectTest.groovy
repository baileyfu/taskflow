package taskflow.script.test

import org.codehaus.groovy.reflection.MixinInMetaClass

/**
 * MOP方法注入
 * 适用情况：编写代码时知道想添加到若干个类中的方法的名字
 * 可根据需要随时往类中添加方法从而带来无限的可扩展性
 * 三种方式：Category，ExpandoMetaClass，Mixin
 */
/*（一）		分类(category)
 * 一种能修改类MetaClass的对象，仅在代码块的作用域和执行线程内有效；可嵌套
 * 每次进入use,groovy都必须检查静态方法,并将其加入到新作用域的一个方法列表中,在块的最后还要清理该作用域;不适合频繁的调用*/
class SSNAdvice{
	//不指定类型默认为Object,若指定类型则需重载支持的所有类型
	def static toSSN(self,closure) {
		if(closure)
			closure.call()
		if(self.size()==9) {
			"${self[0..2]}-${self[3..4]}-${self[5..8]}"
		}
	}
	//分类实现AOP
	def static toString(String self) {
		def method = self.metaClass.methods.find{it.name=='toString'}
		'@'+method.invoke(self, null)+'@'
	}
}
@Category(String)//类一旦定义,groovy会将类名转变为一个对该类的Class元对象的引用,即String==String.class
class FindAdviceAnnotated{
	//根据注解将该方法转变为public static toSSN(StringBuilder self)
	def findMax(closure) {
		if(closure)
			closure.call()
		def max
		this.each {
			max=max?(it>max?it:max):it
		}
		max
	}
}
//注入仅在此代码块内生效;可接收多个分类;分类的方法名冲突时以最后一个分类优先级最高
//use可嵌套,内部优先级高于外部
use(SSNAdvice,FindAdviceAnnotated){
	println "123456789".toSSN({println 'A'})
	println '987654321'.findMax()
	use(SSNAdvice){
		println 'abcdefg'.toString()
	}
}
//use代码块外调用会抛异常
//println "123456789".toSSN().

/*（二）		ExpandoMetaClass
 *通过类的MetaClass可添加方法，属性，构造器和静态方法，可可以从其它类借方法，甚至向POGO中注入方法 
 *注入的方法只能从Groovy代码内调用，不能从编译过的java代码中调用，也不能从java代码反射来使用；但可以通过元方法invokeMethod调用
 **/
daysFromNow= {
	Calendar today=Calendar.instance;
	today.add(Calendar.DAY_OF_MONTH,delegate.intValue())
	today.time
}
//注入一个getXxx方法则目标对象可以将xxx作为属性来访问
Integer.metaClass.getDaysFromNow=daysFromNow
Long.metaClass.daysFromNow=daysFromNow
Number.metaClass.getDaysFromNow=daysFromNow//注入到基类上
println 1I.daysFromNow
println 2L.daysFromNow()
println 3D.daysFromNow
Integer.metaClass.'static'.isEven= {val->val%2==0}//注入静态方法
println Integer.isEven(10)
Integer.metaClass.constructor << {Calendar c->new Integer(c.get(Calendar.DAY_OF_YEAR))}//增加一个构造器;不能用<<来覆盖构造器,要覆盖用=
println new Integer(Calendar.instance)
Integer.metaClass.constructor = {int i->
	c=Integer.class.getConstructor(Integer.TYPE)//反射使用原实现
	c.newInstance(i+100)
}//覆盖构造器;注意引起递归调用
println new Integer(1)
//EMC DSL注入方法分组，将加入到一个类中的方法集中在一起，减少了代码噪音
Number.metaClass{
	getDaysFromNow=daysFromNow
	'static'{
		isMinus= {val->val<0}
	}
	constructor= {Calendar c->new Integer(c.get(Calendar.DAY_OF_YEAR))}
}
println ''+Long.isMinus(10)+'_'+new Integer(Calendar.instance).daysFromNow
//往对象中注入方法
class Child{
	void play(){println 'playing...'+this}
}
Child jack=new Child()
Child rose=new Child()
def emc=new ExpandoMetaClass(Child)//1.创建ExpandoMetaClass来注入
emc.sing = {'singing...'+delegate}
emc.initialize()
jack.setMetaClass(emc)
jack.play()
println jack.sing()
//println rose.sing()//rose没有sing方法
rose.metaClass.dance= {'dancing...'+delegate}//2.直接通过metaClass注入
println rose.dance()
//println jack.dance()//jack没有dance方法
Child mike=new Child()
mike.metaClass{//3.分组注入
	sing = {'singing...'+delegate}
	dance= {'dancing...'+delegate}
}
println mike.sing()
println mike.dance()
mike.metaClass =null//清除注入对象的方法

/*（三）		Mixin
 * 将一个类混入多个类中，或将多个类混入一个类中
 *调用一个方法时先将调用路由到混入的类中，如果存在则调用，否则由主类处理；相同方法最后混入的优先级最高(其它被隐藏)*/
class Friend{
	def listen(){"$name is listening as a friend"}
}
@Mixin(Friend)//1.注解混入;Groovy已不推荐，改由trait来实现
class Driver{
	def name
}
class Manager{
	static{mixin Friend}//2.静态初始化器混入(依然是minxin方法混入)
	def getName() {'louis'}
}
class Nurser{
	def name
}
Nurser.mixin Friend//3.mixin()方法运行时动态混入
dine=new Driver(name:'dine')
println dine.listen()
jobs=new Manager()
println jobs.listen()
lisa=new Nurser(name:'lisa')
println lisa.listen()
class Dog{
	def name
}
socks=new Dog(name:'socks')
socks.metaClass.mixin(Friend)//4.往对象混入
println socks.listen()

//***使用多个有相同方法的Mixin（相同的方法不隐藏而是依次调用）
class Writer{
	def target=new StringBuilder()
	void write(message) {
		target.append(message.toString())
	}
	String toString() {target.toString()}
}
def doWrite(writer) {
	writer.write('This is stupid')
	println writer
}
def create(writer,Object[] filters) {
	filters.each { writer.metaClass.mixin it }
	writer
}
class UppercaseFilter{
	void write(message) {
		def allUpper=message.toString().toUpperCase()
		invokeOnPreviousMixin(metaClass,'write',allUpper)
	}
}
class ProfanityFilter{
	void write(message) {
		def filtered=message.replaceAll('stupid','***')
		invokeOnPreviousMixin(metaClass,'write',filtered)
	}
}
//此处是关键：Groovy提供一个名为mixedIn（LinkedHashSet）的属性，为对象保存有序的Mixin列表，遍历此链条找到新添加的Mixin或最终的目标对象
//链条中最左侧即最终目标对象
Object.metaClass.invokeOnPreviousMixin= {
	MetaClass currentMixinMetaClass,String method,Object[] args->
	def previousMixin=delegate.getClass()
	for(mixin in mixedIn.mixinClasses) {
		if(mixin.mixinClass.theClass==currentMixinMetaClass.delegate.theClass)break;
		previousMixin=mixin.mixinClass.theClass
	}
	mixedIn[previousMixin]."$method"(*args)
}
Writer w=new Writer()
doWrite(create(w,UppercaseFilter,ProfanityFilter))
doWrite(create(w=new Writer(),ProfanityFilter,UppercaseFilter))
for(x in w.mixedIn.mixinClasses) {
	println ''+x.mixinClass+'-'+x.mixinClass.theClass
}
//mixedIn实际类型为groovy.lang.ExpandoMetaClass$MixedInAccessor，其有一个mixinClasses属性为LinkedHashSet，持有所有Minxin类，但不包括目标类
//MixedInAccessor为目标对象和Mixin对象生成了代理，可通过mixedIn[目标类]访问目标对象
println w.mixedIn
w.mixedIn[w.getClass()].write('\nHelloWorld')
println w


