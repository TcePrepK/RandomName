package renderers;

import display.DisplayManager;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

public class MasterRenderer {
    public static void finishRendering() {
        glfwSwapBuffers(DisplayManager.getWindow());
        glfwPollEvents();
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
    }
}
