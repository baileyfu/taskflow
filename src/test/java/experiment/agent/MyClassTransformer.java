package experiment.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class MyClassTransformer implements ClassFileTransformer{
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		System.out.println("-------->"+className);
		if(className.equals("experiment/agent/AgentRunner")) {
			System.out.println("transform class : "+className+" , classloader : "+loader+" , classBeingRedefined : "+classBeingRedefined+" , protectionDomain : "+protectionDomain);
			try {
                // 从ClassPool获得CtClass对象
                final ClassPool classPool = ClassPool.getDefault();
                final CtClass clazz = classPool.get("experiment.agent.AgentRunner");
                CtMethod sayHello = clazz.getDeclaredMethod("sayHello");
                String methodBody = "{System.out.println(\"HaHaHaHa...\");}";
                sayHello.setBody(methodBody);

                // 返回字节码，并且detachCtClass对象
                byte[] byteCode = clazz.toBytecode();
                //detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
                clazz.detach();
                return byteCode;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
		}
		// 如果返回null则字节码不会被修改
		return classfileBuffer;
	}
}
