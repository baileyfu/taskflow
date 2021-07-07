package experiment.lang;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class CreatObjectRunner {
	String name;
	public CreatObjectRunner() {
		System.out.println("---I'm created!");
	}
	public CreatObjectRunner(String name) {
		this.name=name;
		System.out.println("---I'm created and my name is "+name);
	}
	public static void main(String[] args) throws Exception{
		//1
		CreatObjectRunner co=new CreatObjectRunner();
		System.out.println(co);
		//2
		co=CreatObjectRunner.class.newInstance();
		System.out.println(co);
		//推断构造函数参数,选择合适的构造器
		co=CreatObjectRunner.class.getDeclaredConstructor(String.class).newInstance("jack");
		System.out.println(co);
		//3
		//new编译后形成三条指令
		//new #1				根据类型分配一块内存区域
		//dup					把第一条指令返回的内存地址压入操作数栈顶
		//invokespecial #19		调用类的构造函数
		//Unsafe.allocateInstance()方法值做了第一步和第二步，即分配内存空间，返回内存地址，没有做第三步调用构造函数。
		//所以Unsafe.allocateInstance()方法创建的对象都是只有初始值，没有默认值也没有构造函数设置的值，
		//因为它完全没有使用new机制，直接操作内存创建了对象
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		Unsafe unsafe = (Unsafe) f.get(null);
		co=(CreatObjectRunner)unsafe.allocateInstance(CreatObjectRunner.class);
		System.out.println(co);
	}
	@Override
	public String toString() {
		return "CreatObject [name=" + name + "]";
	}

}
