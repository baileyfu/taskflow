package experiment.security.reflect;

import sun.reflect.CallerSensitive;

/**
 * 增加vm参数：-Xbootclasspath/a:/Users/bailey/git/taskflow/target/test-classes
 * @author bailey
 * @date 2021年6月29日
 */
public class CallerRunner {
	@CallerSensitive
	void run() {
		Class<?> caller = sun.reflect.Reflection.getCallerClass(2);
		System.out.println(caller);
		caller = sun.reflect.Reflection.getCallerClass();
		System.out.println(caller);
	}
	
	public static void main(String[] args) {
		CallerRunner cr=new CallerRunner();
		CallerRun run=new CallerRun();
		run.run(cr);
		cr.run();
	}
	static class CallerRun{
		void run(CallerRunner cr) {
			cr.run();
		}
	}
}
