package experiment.agent;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.jar.JarFile;

public class MyInstrumentation implements Instrumentation{

	//增加一个Class 文件的转换器，转换器用于改变 Class 二进制流的数据，参数 canRetransform 设置是否允许重新转换
	@Override
	public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
		// TODO Auto-generated method stub
		
	}
	//在类加载之前，重新定义 Class 文件，ClassDefinition 表示对一个类新的定义，如果在类加载之后，需要使用 retransformClasses 方法重新定义。
	//addTransformer方法配置之后，后续的类加载都会被Transformer拦截。
	//对于已经加载过的类，可以执行retransformClasses来重新触发这个Transformer的拦截。
	//类加载的字节码被修改后，除非再次被retransform，否则不会恢复
	@Override
	public void addTransformer(ClassFileTransformer transformer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeTransformer(ClassFileTransformer transformer) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isRetransformClassesSupported() {
		// TODO Auto-generated method stub
		return false;
	}
	//在类加载之后，重新定义 Class。这个很重要，该方法是1.6 之后加入的，事实上，该方法是 update 了一个类
	@Override
	public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRedefineClassesSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void redefineClasses(ClassDefinition... definitions)
			throws ClassNotFoundException, UnmodifiableClassException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isModifiableClass(Class<?> theClass) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class[] getAllLoadedClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class[] getInitiatedClasses(ClassLoader loader) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getObjectSize(Object objectToSize) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appendToSystemClassLoaderSearch(JarFile jarfile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNativeMethodPrefixSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
		// TODO Auto-generated method stub
		
	}

}
