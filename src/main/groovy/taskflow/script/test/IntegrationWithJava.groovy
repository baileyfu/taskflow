package taskflow.script.test

import javax.script.Bindings
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings

/**
 * Eval
 * groovy.util.Eval 类是最简单的用来在运行时动态执行 Groovy 代码的类，
 * 提供了几个静态工厂方法供使用，内部其实就是对GroovyShell的封装
 */
//执行Groovy代码
Eval.me("println 'hello world'")
//绑定自定义参数
Object result = Eval.me("age", 22, "if(age < 18){'未成年'}else{'成年'}")
assert result=="成年"
//绑定一个名为 x 的参数的简单计算
assert Eval.x(4, "2*x")==8
//带有两个名为 x 与 y 的绑定参数的简单计算
assert Eval.xy(4, 5, "x*y")==20
//带有三个绑定参数（x、y 和 z）的简单计算
assert Eval.xyz(4, 5, 6, "x*y+z")==26

/**
 * GroovyShell
 * groovy.lang.GroovyShell除了可以执行 Groovy 代码外，提供了更丰富的功能，
 * 比如可以绑定更多的变量，从文件系统、网络加载代码等
 */
GroovyShell shell = new GroovyShell()
//可以绑定更多变量
shell.setVariable("age",22)
//直接求值
shell.evaluate("if(age < 18){'未成年'}else{'成年'}")
//解析为脚本，延迟执行或者缓存起来
Script script = shell.parse("if(age < 18){'未成年'}else{'成年'}")
assert script.run()=="成年"
//可以从更多位置加载/执行脚本
//shell.evaluate(new File("script.groovy"));
//shell.evaluate(new URI("http://wwww.a.com/script.groovy"));

/**
 * GroovyClassLoader
 * groovy.lang.GroovyClassLoader是一个定制的类加载器，可以在运行时加载 Groovy 代码，生成 Class 对象
 * */
GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
String scriptText = '''\
package com.bl 
class Hello { void hello() { println 'hello' } }
''';
//将Groovy脚本解析为Class对象
Class clazz = groovyClassLoader.parseClass(scriptText);
//Class clazz = groovyClassLoader.parseClass(new File("script.groovy"));
assert clazz.getName()=="com.bl.Hello"
println clazz
clazz.getMethod("hello").invoke(clazz.getDeclaredConstructor().newInstance());

/**
 * GroovyScriptEngine
 * groovy.util.GroovyScriptEngine能够处理任何 Groovy 代码的动态编译与加载
 * 可以从统一的位置加载脚本，并且能够监听脚本的变化，当脚本发生变化时会重新加载
 * */
//GroovyScriptEngine scriptEngine = new GroovyScriptEngine("C://Users//linzi//");
//Binding binding = new Binding();
//binding.setVariable("name", "groovy");
//while (true){
//	scriptEngine.run("test.groovy", binding);
//	break;
//}

/***
 * JSR 223 javax.script API
 * JSR-223 是 Java 中调用脚本语言的标准 API。
 * 从 Java 6 开始引入进来，主要目的是用来提供一种统一的框架，以便在 Java 中调用多种脚本语言。
 * JSR-223 支持大部分流行的脚本语言，比如JavaScript、Scala、JRuby、Jython和Groovy等
 */
ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy")
Bindings bindings = new SimpleBindings()
bindings.put("age", 22)
Object value = engine.eval("if(age < 18){'未成年'}else{'成年'}",bindings)
assert value=="成年"
//engine.eval(new FileReader("script/groovy/hello.groovy"));

