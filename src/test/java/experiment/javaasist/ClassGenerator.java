package experiment.javaasist;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import experiment.MyClassLoader;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;

public class ClassGenerator {

	static byte[] gen() throws Exception{
		//javassist的类池，使用ClassPool类可以跟踪和控制所操作的类，它的工作方式与 JVM 类装载器非常相似
		ClassPool pool = ClassPool.getDefault();
		//加载一个已知的类
		CtClass stringClass = pool.get("java.lang.String");
		//提供了类的操作，如在类中动态添加新字段、方法和构造函数、以及改变类、父类和接口的方法
        CtClass cc= pool.makeClass("experiment.GenByJAsist");
        //类的属性，通过它可以给类创建新的属性，还可以修改已有的属性的类型，访问修饰符等
        CtField field = new CtField(stringClass,"name",cc);
        field.setModifiers(Modifier.PUBLIC);
        cc.addField(field);
        //类中的方法，通过它可以给类创建新的方法，还可以修改返回类型，访问修饰符等， 甚至还可以修改方法体内容代码
        CtMethod method = CtNewMethod.make("public void code(String company){}", cc);
        //参数1:方法的返回类型，参数2：名称，参数3：方法的参数，参数4：方法所属的类
//        CtMethod method=new CtMethod(CtClass.voidType, "code", new CtClass[]{stringClass}, cc);
        //方法体中$1代表第一个参数，$2代表第二个参数
        method.setBody("System.out.println($1);");
        //插入方法代码
        method.insertBefore("System.out.println(\"I'm a \"+name+\",Just Coding.....\");");
        method.insertAfter("System.out.println(new java.util.Date());",true);
        cc.addMethod(method);
        //定义构造方法
        CtConstructor constructor=CtNewConstructor.make("public GenByJAsist(){this.name=\"JACK\";}", cc);
        cc.addConstructor(constructor);
        //保存生成的字节码  
        //cc.writeFile("/Users/bailey/Downloads"); 
        return cc.toBytecode();
	}
	static Class load1() throws Exception {
		MyClassLoader mcl=new MyClassLoader();
		File file = new File(".");  
        InputStream  input = new FileInputStream("/Users/bailey/Downloads/experiment/GenByJAsist.class");  
        byte[] result = new byte[1024];
        int count = input.read(result);
        return mcl.redifine(result, 0, count);
	}
	static Class load() throws Exception{
		byte[] code=gen();
		return new MyClassLoader().redifine(code, 0, code.length);
	}
}
