package experiment.agent;

import java.lang.instrument.Instrumentation;

public class AgentMainDefiner {
	//采用attach机制，被代理的目标程序VM有可能很早之前已经启动，当然其所有类已经被加载完成，
	//这个时候需要借助Instrumentation#retransformClasses(Class<?>... classes)让对应的类可以重新转换，
	//从而激活重新转换的类执行ClassFileTransformer列表中的回调
	public static void agentmain (String agentArgs, Instrumentation inst) {
		System.out.println("agentmain be invoked...");
		inst.addTransformer(new MyClassTransformer(),true);
	}

	public static void agentmain (String agentArgs) {
		
	}
}
