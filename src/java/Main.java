import core.Keyboard;
import core.Mouse;
import core.ScreenShotManager;
import display.DisplayManager;
import game.materials.MaterialManager;
import org.lwjgl.glfw.GLFW;
import renderers.MasterRenderer;
import toolbox.Logger;
import toolbox.Noise;

import static core.GlobalVariables.*;

public class Main {
    public static void main(final String[] args) {
        DisplayManager.createDisplay();

        // Init
        Noise.init(seed);
        Keyboard.init();
        Mouse.init();
        ScreenShotManager.init();
        MaterialManager.init();
        simulation.init();
        // Init

        // Game Loop
        Logger.out("~ First Frame Starting");
        while (!GLFW.glfwWindowShouldClose(DisplayManager.getWindow())) {
            currentFrame++;

            Mouse.update();
            threadManager.update();

            simulation.update();
            simulation.render();

            imGuiManager.update();

            ScreenShotManager.update();
            MasterRenderer.finishRendering();

            DisplayManager.updateDisplayTimer();
        }

        imGuiManager.cleanUp();
        DisplayManager.closeDisplay();
        renderer.cleanUp();
        threadManager.cleanUp();
        loader.cleanUp();
    }
}
