package lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MySimpleLock implements ILock {

    private final MySync sync;

    public MySimpleLock() {
        this.sync = MySync.getMySyncInstance();
    }

    @Override
    public void lock() {
        sync.lock();
    }

    @Override
    public void release() {
        sync.release();
    }

    private static class MySync extends AbstractQueuedSynchronizer {

        private static volatile MySync sync;

        private static MySync getMySyncInstance() {
            if (sync == null) {
                synchronized (MySync.class) {
                    if (sync == null) {
                        sync = new MySync();
                        return sync;
                    }
                }
            }
            return sync;
        }

        protected void lock(){
            this.acquire(1);
        }
        protected void release(){
            this.release(1);
        }

        @Override
        protected final boolean tryAcquire(int i) {
            if (getExclusiveOwnerThread() != null && getExclusiveOwnerThread() != Thread.currentThread()) {
                return false;
            }
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected final boolean tryRelease(int i) {
            if (getExclusiveOwnerThread() != Thread.currentThread()) {
                return false;
            }
            if (compareAndSetState(1, 0)) {
                setExclusiveOwnerThread(null);
                return true;
            }
            return false;
        }
    }
}
