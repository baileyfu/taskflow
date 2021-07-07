package experiment.agent;

public class AgentRunner {

	public static void sayHello(String name) {
		System.out.println(name+" Hello World!");
	}
	static {
		sayHello("static block");
	}
	public static void main(String[] args) throws Exception{
		sayHello("main method");
        Thread.sleep(1000*5);
	}

}
