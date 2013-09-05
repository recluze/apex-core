public class Check {
  public static int count = 5;

  public static void main(String[] args) {
    test(2);
  }
  public static int test(int n) {
    Producer p = new Producer();
    Consumer c = new Consumer();
  
    Thread t = new Thread(c);
    t.start();
    p.run();
    try {
      t.join();
    } catch (Exception e) {
    }
    if (count != 5) throw new IllegalStateException("count=" + count);
System.out.println("count is 5. OK!");
    return count;
  }
  public static class Producer implements Runnable {
    public void run() {
      int register1 = count;
      register1++;
      System.out.println("REgister 1 is: "+register1);
      count = register1;
    }
  }
  public static class Consumer implements Runnable {
    public void run() {
      int register2 = count;
      register2--;
      System.out.println("REgister 2 is: "+register2);
      count = register2;
    }
  }
}

