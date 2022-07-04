package core;

public abstract class BasicThread implements Runnable {
    private final Thread mainThread;
    private final String threadName;

    protected final Signal threadDied = new Signal();
    protected final Signal threadSleep = new Signal();

    protected boolean isDead = false;
    protected boolean isSleeping = false;

    private final Timer loopTimer = new Timer();
    private float threadAliveTime = 0;
    private int loopTime = 0;
    private float threadFrame = 0;

    public BasicThread(final String name) {
        mainThread = new Thread(this, name);
        threadName = name;
    }

    @Override
    public void run() {
        while (!isDead) {
            if (isSleeping) {
                breath();
                continue;
            }

            threadFrame++;
            loopTimer.startTimer();

            loop();
            breath();

            final double delta = loopTimer.stopTimer();
            loopTime = (int) Math.floor(delta * 1000);

            threadAliveTime += delta;
        }
    }

    protected abstract void loop();

    public BasicThread start() {
        if (isDead) {
            return this;
        }

        if (isSleeping) {
            isSleeping = false;
            return this;
        }

        if (mainThread.isAlive()) {
            return this;
        }

        mainThread.start();
        return this;
    }

    public void wakeUp() {
        isSleeping = false;
    }

    public void waitDeath() {
        try {
            mainThread.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void whenSleep(final Runnable runnable) {
        threadSleep.add(runnable);
    }

    public void whenDied(final Runnable runnable) {
        threadDied.add(runnable);
    }

    protected static void breath() {
        try {
            Thread.sleep(0);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void kill() {
        isDead = true;
    }

    protected void sleep() {
        isSleeping = true;
        threadSleep.dispatch();
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public String name() {
        return threadName;
    }

    public float getLoopTime() {
        return loopTime;
    }

    public float getThreadAliveTime() {
        return threadAliveTime;
    }
}
