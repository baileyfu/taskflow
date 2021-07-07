package experiment;

public class MyClassLoader extends ClassLoader{
	public MyClassLoader() {
	}
	public MyClassLoader(ClassLoader parent) {
		super(parent);
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
		    security.checkCreateClassLoader();
		}
	}
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return super.findClass(name);
	}
	public Class<?> redifine(byte[] b, int off, int len) {
		return super.defineClass(null,b, off, len);
	}
}