package toolbox;

public interface CustomRunnable<V, T> extends Runnable {
    V run(T... arg);
}
