package experiment.lang;

public class LockRunner {
	Object lock=new Object();
	void test() {
		synchronized(lock) {
			try {
				System.out.println("a waitting...");
				lock.wait();
				System.out.println("a continue");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	void testa() {
		synchronized(lock) {
			try {
				System.out.println("b running...");
				Thread.sleep(10*1000);
				lock.notify();
				System.out.println("b notified...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println(System.getProperty("java.io.tmpdir"));
		LockRunner l=new LockRunner();
		Thread a=new Thread(new Runnable() {
			@Override
			public void run() {
				l.test();
			}
		},"a");
		Thread b=new Thread(new Runnable() {
			@Override
			public void run() {
				l.testa();
			}
		},"b");
		a.start();
		b.start();
		Thread.sleep(50*1000);
	}

}
