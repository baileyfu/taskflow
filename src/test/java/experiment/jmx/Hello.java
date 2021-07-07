package experiment.jmx;

import experiment.security.ClassLoaderA;
import experiment.security.ClassLoaderB;

public class Hello implements HelloMBean{
	private String name;
	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void say() {
		System.out.println(name+"--->"+this.getClass().getClassLoader());
	}

	@Override
	public void say(String content) {
		System.out.println("Hello "+content+" "+name);
	}

	@Override
	public void loadClass() {
		ClassLoaderA cla=new ClassLoaderA("/Users/bailey/Downloads/Nobody.class");
		try {
			cla.loadClass("experiment.security.Nobody");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void noLoadClass() {
		ClassLoaderB cla=new ClassLoaderB("/Users/bailey/Downloads/Nobody.class");
	}

}
