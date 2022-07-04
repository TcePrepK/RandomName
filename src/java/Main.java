import core.Keyboard;
import core.Mouse;
import core.ScreenShotManager;
import core.Timer;
import display.DisplayManager;
import org.lwjgl.glfw.GLFW;
import toolbox.Logger;
import toolbox.Noise;

import static core.GlobalVariables.*;
import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;

public class Main {
    public static void main(final String[] args) {
        DisplayManager.createDisplay();

        // Init
        Noise.init(mapSeed);
        Keyboard.init();
        Mouse.init();
        ScreenShotManager.init();
        // Init

        // Thread
//        threadManager.addThread(new ChunkGenerationThread()).start();
//        threadManager.addThread(new ChunkUpdateThread());
        // Thread

        // Timer Setup
        final Timer mainTimer = new Timer();
        // Timer Setup

        // Test
//        chunkManager.getChunkChunkSpace(0, 0, true);
//        chunkManager.getChunkChunkSpace(1, 0, true);
        // Test

        // Chunks
        for (int i = 0; i < WIDTH / mapChunkSize; i++) {
            for (int j = 0; j < HEIGHT / mapChunkSize; j++) {
                chunkManager.getChunkChunkSpace(i, j, true);
            }
        }
        // Chunks

        // Game Loop
        Logger.out("~ First Frame Starting");
        while (!GLFW.glfwWindowShouldClose(DisplayManager.getWindow())) {
            currentFrame++;

            mainTimer.startTimer();
            Mouse.update();
            threadManager.update();
            final float updateTime = (float) mainTimer.stopTimer() * 1000;

            chunkManager.update();

            renderer.render();
            imGuiManager.update(updateTime);

            ScreenShotManager.update();
            renderer.finishRendering();

            DisplayManager.updateDisplayTimer();
        }

        imGuiManager.cleanUp();
        DisplayManager.closeDisplay();
        renderer.cleanUp();
        threadManager.cleanUp();
        loader.cleanUp();
    }
}
