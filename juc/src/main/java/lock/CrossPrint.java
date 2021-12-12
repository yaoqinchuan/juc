package lock;



public class BeansPrint implements Runnable {
    Object lock = new Object();
    private static int num = 0;

    @Override
    public void run() {
        synchronized (lock) {
            while (num % 3 != target)
        }

    }

    public static void main(String[] s) throws InterruptedException {

    }


}

