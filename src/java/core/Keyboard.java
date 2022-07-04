package core;

import display.DisplayManager;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Keyboard {
    public static final List<String> pressedList = new ArrayList<>();

    public static final Signal keyPressed = new Signal();

    private static final Map<Integer, String> keyMap = new HashMap<>();

    static {
        keyMap.put(42, "LSHIFT");
        keyMap.put(57, "SPACE");
        keyMap.put(87, "F11");
        keyMap.put(88, "F12");
    }

    public static void init() {
        GLFW.glfwSetKeyCallback(DisplayManager.getWindow(), (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }

            String keyName = GLFW.glfwGetKeyName(key, scancode);
            if (keyName == null) {
                keyName = keyMap.get(scancode);
            }

            if (keyName == null) {
                keyName = Integer.toString(scancode);
            }

            if (action == GLFW.GLFW_PRESS) {
                Keyboard.pressedList.add(keyName);
            } else if (action == GLFW.GLFW_RELEASE) {
                Keyboard.pressedList.remove(keyName);
            }

            Keyboard.keyPressed.test();
        });
    }

    public static boolean isKeyDown(final String key) {
        return Keyboard.pressedList.contains(key.toUpperCase());
    }
}
