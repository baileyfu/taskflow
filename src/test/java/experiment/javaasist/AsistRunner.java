package experiment.javaasist;

//1. 所引用的类型，必须通过ClassPool获取后才可以使用
//2. 代码块中所用到的引用类型，使用时必须写全量类名
//3. 即使代码块内容写错了，只有在运行时才报错
//4. 动态修改的类，必须在修改之前，jvm中不存在这个类的实例对象。修改方法的实现必须在修改的类加载之前进行。
public class AsistRunner {

	public static void main(String[] args) throws Exception{
//		ClassGenerator.gen();
		Class<?> clazz=ClassGenerator.load();
		Object o=clazz.newInstance();
		clazz.getDeclaredMethod("code",String.class).invoke(o,"AiRong");
	}

}
