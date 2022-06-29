package game;

import core.GlobalVariables;
import core.RawModel;
import shaders.BaseShader;

import static core.GlobalVariables.world;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class DisplayRenderer {
    private final DisplayShader displayShader = new DisplayShader();

    private final RawModel quad;

    public DisplayRenderer() {
        quad = GlobalVariables.loader.loadToVAO(new float[]{
                -1, 1, 1, 1, -1, -1, 1, -1
        }, 2);

        displayShader.start();
        displayShader.loadResolution();
        DisplayShader.stop();
    }

    public void render() {
        displayShader.start();

        world.updateBuffers();
        world.getWorldBuffer().bind();

        renderQuad();
        BaseShader.stop();
    }

    private void renderQuad() {
        glBindVertexArray(quad.getVaoID());
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        displayShader.cleanUp();
    }
}
