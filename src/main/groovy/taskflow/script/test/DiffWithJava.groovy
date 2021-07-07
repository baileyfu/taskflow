package taskflow.script.test

import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.Paths

import groovy.transform.PackageScope

/**
 * Groovy脚本
 * Groovy 编译器会生成一个 Script 的子类，类名和脚本文件的文件名一样，
 * 而脚本中的代码会被包含在一个名为run的方法中，同时还会生成一个main方法，作为整个脚本的入口
 * */

/**
 * 方法重载
 * 根据运行时实参的类型来选择方法
 */
void sayHello(Object arg) {
	println "Hello ${arg}"
}
void sayHello(String arg) {
	println "Greeting ${arg}"
}
def arg="Kaiser"
sayHello(arg)
arg=123
sayHello(arg)

/**
 * 数组初始化
 * {...} 语句块是留给闭包使用的，所以不能像 Java中一样使用int[] arr= {1,2,3}种方式初始化数组
 */
int[] arr=[1,2,3]
println arr.getClass()

/**
 * POJO
 * Groovy 默认会隐式的创建getter、setter方法，并且会提供带参的构造器
 * */
class Person {
	String name
	@PackageScope int age//包访问权限
}
def p=new Person(name:'Kaiser',age:12)
println p.getName()+'==='+p

/**
 * ARM（Automatic Resource Management，自动资源管理）
 * Groovy 并不支持，但提供多种基于闭包的方法操作更便捷
 * */
String filePath='C:\\Users\\linzi\\test.txt'
///In Java
//Path file = Paths.get(filePath);
//Charset charset = Charset.forName("UTF-8");
//try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
//	String line;
//	while ((line = reader.readLine()) != null) {
//		System.out.println(line);
//	}
//} catch (IOException e) {
//	e.printStackTrace();
//}
///In Groovy
new File(filePath).eachLine('UTF-8') {
	println it
}
///Or
//print new File(filePath).text

/**
 * 内部类
 * 支持内部类并且实现跟 Java 是一样的
 * 在Groovy中内部类看起来有点类似 groovy.lang.Closure 类的实现
 * */
class A{
	static class B{}
}
println new A.B()
interface I{
	def getName()
}
println new I() {
	@Override
	Object getName() {
		'Kaiser'
	}
}.getName()
I i= {'NewName'}//采用闭包来实现Lambda
println i.getName()

/**
 * GString
 * 当某个类中的 String 字面量含有美元字符（$）时，那么利用 Groovy 和 Java 编译器进行编译时，Groovy 很可能就会编译失败
 * 或者产生与 Java 编译所不同的结果
 * 要小心那些形参为 Object 的 Java API，需要检查它们的实际类型
 * 双引号创建的字面量则可能是 String 或 GString 对象，具体分类由字面量中是否有插值来决定
 * */
println "aa".getClass()
println "aa${arg}".getClass()

