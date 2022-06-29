package core;

import display.DisplayManager;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import toolbox.Logger;

import static core.GlobalVariables.renderer;

public class ImGuiManager {
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public ImGuiManager() {
        ImGui.createContext();
        imGuiGlfw.init(DisplayManager.getWindow(), true);
        imGuiGl3.init("#version 450");

        Logger.out("~ ImGui Initialized Successfully");
    }

    public void update(final float updateTime) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
        ImGui.begin("Cool Window");

        // FPS
        ImGui.text("FPS: " + DisplayManager.getFPS());
        ImGui.text("Average FPS: " + DisplayManager.getAverageFPS());
        ImGui.spacing();
        ImGui.text("Update time: " + updateTime + "ms");
        ImGui.text("Binding time: " + renderer.getBindTime() + "ms");
        ImGui.text("Tracing time: " + renderer.getTraceTime() + "ms");
        ImGui.text("Other time: " + renderer.getOtherTime() + "ms");
        ImGui.text("Total time: " + DisplayManager.getDelta() * 1000 + "ms");
        ImGui.spacing();
        ImGui.spacing();
        // FPS

        ImGui.end();
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public void cleanUp() {
        imGuiGlfw.dispose();
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }
}
