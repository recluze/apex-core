public class Racer implements Runnable {

	/**
	 * @uml.property  name="d"
	 */
	int d = 42;

	public void run() {
		System.out.println("RUN(method)");
		doSomething(1001); // (1)
		d = 0; // (2)
	}

	public static void main(String[] args) {
		Racer racer = new Racer();
		Thread t = new Thread(racer);
		for (int i = 0; i <= 4; i++) {
			t.start();
		}

		doSomething(1000); // (3)
		System.out.println("do something......");
		int c = 420 / racer.d; // (4)
		System.out.println(c);
	}

	static void doSomething(int n) {
		// not very interesting..
		try {
			Thread.sleep(n);
		} catch (InterruptedException ix) {
		}
	}
}
