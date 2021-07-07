package experiment.security;

public class SomeBody {
	static {
		System.out.println("SomeBody is loaded...");
	}
	public void say(Object nobody) {
		System.out.println("SomeBody "+this+" say something to nobody "+nobody);
	}
}
