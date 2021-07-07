package experiment.agent;

import java.lang.instrument.Instrumentation;

public class PreMainDefiner {
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("premain be invoked...");
		inst.addTransformer(new MyClassTransformer(),true);
	}
	public static void premain(String agentArgs) {
	}
}
