import core.Keyboard;
import core.Mouse;
import core.Timer;
import display.DisplayManager;
import org.lwjgl.glfw.GLFW;
import toolbox.Logger;
import toolbox.Noise;

import static core.GlobalVariables.*;

public class Main {
    public static void main(final String[] args) {
        DisplayManager.createDisplay();

        // Init
        Noise.init(mapSeed);
        Keyboard.init();
        Mouse.init();
        // Init

        // Thread
//        threadManager.addThread(new ChunkGenerationThread()).start();
//        threadManager.addThread(new ChunkUpdateThread());
        // Thread

        // Test
//        final BitManager manager = new BitManager(256, 256, 256, 4);
//
//        manager.writeValue(new Vector3D(1, 0, 0), 0b1111);
//        manager.writeValue(new Vector3D(0, 1, 0), 0b1111);
//        manager.writeValue(new Vector3D(0, 0, 1), 0b1111);

//        System.out.println(manager.readByte(0));
        // Test

        // Timer Setup
        final Timer mainTimer = new Timer();
        // Timer Setup

        // Game Loop
        Logger.out("~ First Frame Starting");
        while (!GLFW.glfwWindowShouldClose(DisplayManager.getWindow())) {
            currentFrame++;

            mainTimer.startTimer();
            Mouse.update();
            threadManager.update();
            final float updateTime = (float) mainTimer.stopTimer() * 1000;

            renderer.render();
            imGuiManager.update(updateTime);
            renderer.finishRendering();

//            renderer.loadOldCameraVariables();
            DisplayManager.updateDisplayTimer();
        }

        threadManager.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        imGuiManager.cleanUp();
        DisplayManager.closeDisplay();
    }
}
