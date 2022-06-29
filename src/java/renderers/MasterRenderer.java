package renderers;

import core.Timer;
import display.DisplayManager;
import game.DisplayRenderer;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

public class MasterRenderer {
    public final DisplayRenderer displayRenderer = new DisplayRenderer();

    private final Timer mainTimer = new Timer();
    private float bindTime;
    private float traceTime;
    private float otherTime;

    public void render() {
        // Binding Timer
        mainTimer.startTimer();
        // Binding Timer

        displayRenderer.render();

        // Timer For Others
        otherTime = (float) mainTimer.stopTimer() * 1000;
        // Timer For Others
    }

    public void finishRendering() {
        mainTimer.startTimer();

        glfwSwapBuffers(DisplayManager.getWindow());
        glfwPollEvents();

        traceTime = (float) mainTimer.stopTimer() * 1000;
    }

//    public void bindFrameBuffer() {
//        glBindFramebuffer(GL_FRAMEBUFFER, displayBufferID);
//        glViewport(0, 0, WIDTH, HEIGHT);
//    }

//    public static void unbindFrameBuffer() {
//        glBindFramebuffer(GL_FRAMEBUFFER, 0);
//        glViewport(0, 0, WIDTH, HEIGHT);
//    }

//    private int createDisplayBuffer() {
//        final int frameBuffer = glGenFramebuffers();
//        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
//
//        final int size = attachmentManager.size();
//        final int[] attachments = new int[size];
//        for (int i = 0; i < size; i++) {
//            attachments[i] = GL_COLOR_ATTACHMENT0 + i;
//        }
//
//        glDrawBuffers(attachments);
//        MasterRenderer.unbindFrameBuffer();
//
//        return frameBuffer;
//    }

    public void cleanUp() {
        displayRenderer.cleanUp();
    }

    public float getBindTime() {
        return bindTime;
    }

    public float getTraceTime() {
        return traceTime;
    }

    public float getOtherTime() {
        return otherTime;
    }
}
