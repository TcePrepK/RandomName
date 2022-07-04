package game;

import core.GlobalVariables;
import core.RawModel;
import core.SSBO;
import shaders.BaseShader;

import java.util.List;

import static core.GlobalVariables.chunkManager;
import static core.GlobalVariables.world;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15C.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class DisplayRenderer {
    private final DisplayShader displayShader = new DisplayShader();

    private final RawModel quad;

    private final SSBO chunkImages = new SSBO(0, GL_DYNAMIC_READ);

    public DisplayRenderer() {
        quad = GlobalVariables.loader.loadToVAO(new float[]{
                -1, 1, 1, 1, -1, -1, 1, -1
        }, 2);

        displayShader.start();
        displayShader.loadResolution();
        displayShader.loadChunkSize();
        DisplayShader.stop();
    }

    public void render() {
        world.update();
        chunkManager.updateBuffers();

        final List<Long> idList = chunkManager.getChunkImageIDs();
        final long[] ids = new long[idList.size()];
        for (int i = 0; i < idList.size(); i++) {
            ids[i] = idList.get(i);
        }

        displayShader.start();

        chunkImages.create(ids);
        chunkImages.bind();

        renderQuad();
        BaseShader.stop();

        chunkImages.cleanUp();

        chunkManager.draw();
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
