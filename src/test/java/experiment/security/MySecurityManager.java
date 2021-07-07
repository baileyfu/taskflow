package experiment.security;

public class MySecurityManager extends SecurityManager{

	@Override
	public void checkRead(String file) {
		System.out.println(this+"--->"+file);
		super.checkRead(file);
	}

	@Override
	public void checkAccess(Thread t) {
		System.out.println(this+"+++>"+t);
		super.checkAccess(t);
	}

	@Override
	public void checkWrite(String file) {
		System.out.println(this+"===>"+file);
		super.checkWrite(file);
	}

}
