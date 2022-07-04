package core;

import toolbox.CustomRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Signal {
    private final HashMap<List<String>, List<Runnable>> runnableList = new HashMap<>();
    private final HashMap<List<String>, List<CustomRunnable<Void, Object>>> customRunnableList = new HashMap<>();

    public Signal() {
        runnableList.put(null, new ArrayList<>());
        customRunnableList.put(null, new ArrayList<>());
    }

    public void add(final Runnable runnable, final String... keys) {
        final List<String> keyCodes = keyCodeConverter(keys);
        final List<Runnable> runnables = runnableList.get(keyCodes);
        runnables.add(runnable);
    }

    public void add(final Runnable runnable) {
        final List<Runnable> runnables = runnableList.get(null);
        runnables.add(runnable);
    }

    public void add(final CustomRunnable<Void, Object> runnable) {
        final List<CustomRunnable<Void, Object>> customRunnables = customRunnableList.get(null);
        customRunnables.add(runnable);
    }

    public void dispatch() {
        for (final List<Runnable> runnables : runnableList.values()) {
            for (final Runnable runnable : runnables) {
                runnable.run();
            }
        }
    }

    public void dispatch(final Object arg) {
        for (final List<CustomRunnable<Void, Object>> customRunnables : customRunnableList.values()) {
            for (final CustomRunnable<Void, Object> runnable : customRunnables) {
                runnable.run(arg);
            }
        }
    }

    public void test() {
        for (final List<String> keyList : runnableList.keySet()) {
            if (keyList == null) {
                continue;
            }

            boolean available = true;
            for (final String key : keyList) {
                if (!Keyboard.isKeyDown(key)) {
                    available = false;
                    break;
                }

            }

            if (!available) {
                continue;
            }

            final List<Runnable> runnables = runnableList.get(keyList);
            for (final Runnable runnable : runnables) {
                runnable.run();
            }
        }
    }

    private List<String> keyCodeConverter(final String... keys) {
        final List<String> keyList = new ArrayList<>();
        for (final String key : keys) {
            keyList.add(key.toUpperCase());
        }

        runnableList.put(keyList, new ArrayList<>());
        return keyList;
    }
}
