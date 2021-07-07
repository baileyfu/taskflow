package experiment.security;

import sun.reflect.misc.ReflectUtil;

public class ClassLoaderRunner {

	public static void main(String[] args) throws Exception{
		ClassLoaderA a=new ClassLoaderA("/Users/bailey/Downloads/Nobody.class");
		//每new一个classloader都是一个安全域,class都会被再加载一次
		ClassLoaderA a2=new ClassLoaderA("/Users/bailey/Downloads/Nobody.class");
		ClassLoaderB b=new ClassLoaderB("/Users/bailey/Downloads/Nobody.class");
		ClassLoaderB sombody=new ClassLoaderB("/Users/bailey/Downloads/SomeBody.class");
		
		System.out.println(a.getClass().getClassLoader()+"---"+a.getParent());
		System.out.println(b.getClass().getClassLoader()+"+++"+b.getParent());
		
		Class ca=a.loadClass("experiment.security.Nobody");
		Class ca2=a2.loadClass("experiment.security.Nobody");
		Class cb=b.loadClass("experiment.security.Nobody");
		Class sb=sombody.loadClass("experiment.security.SomeBody");
		
		System.out.println(ca.getClassLoader()+"---==="+ca);
		System.out.println(ca2.getClassLoader()+"---==="+ca2);
		System.out.println(cb.getClassLoader()+"+++==="+cb);
		System.out.println(sb.getClassLoader()+"+++="+sb);
		
		Object oa=ca.newInstance();
		Object oa2=ca2.newInstance();
		Object ob=cb.newInstance();
		Object ob2=cb.newInstance();
		Object sb1=sb.newInstance();
		Object sb2=sb.newInstance();
		sb1.getClass().getDeclaredMethod("say",Object.class).invoke(sb2, oa);
		
		System.out.println(oa.getClass().getClassLoader()+" a: "+oa);
		System.out.println(oa2.getClass().getClassLoader()+" a: "+oa2);
		System.out.println(ob.getClass().getClassLoader()+" b: "+ob);
		System.out.println(ob2.getClass().getClassLoader()+" b: "+ob2);
		
		ReflectUtil.checkPackageAccess("null");
	}

}
