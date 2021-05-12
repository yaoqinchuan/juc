package lock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyLockTest {
    private int num = 0;
    MySimpleLock lock = new MySimpleLock();

    private void test() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.submit(() -> {
                lock.lock();
                if (num < 50) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    num++;
                }
                lock.release();
                System.out.println("num is " + num);
            });
        }
        threadPoolExecutor.shutdown();
    }

    public static void main(String[] args) {
        new MyLockTest().test();
    }
}
