package experiment.jdkproxy;

public class SubjectReal implements SubjectA,SubjectB{
	@Override
	public void say(String name) {
		System.out.println(name+" says 'Hello World!'");
	}

	@Override
	public void run() {
		System.out.println("running...");		
	}
	public String notBeInvocated() {
		return "XXX";
	}
}
