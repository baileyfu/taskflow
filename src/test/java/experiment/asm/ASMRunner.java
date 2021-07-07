package experiment.asm;

public class ASMRunner {

	public static void main(String[] args) throws Exception {
		Class clazz=ClassGenerator.gen();
		Object o=clazz.newInstance();
		clazz.getDeclaredMethod("code").invoke(o);
		
	}

}
