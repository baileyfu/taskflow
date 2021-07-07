package experiment.jmx;

public interface HelloMBean {
	public void setName(String name);
	public String getName();
	public void say();
	public void say(String content);
	public void loadClass();
	public void noLoadClass();
}
