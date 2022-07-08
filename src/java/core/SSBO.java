package core;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

public class SSBO {
    public int id;

    private final int index, usage;

    public SSBO(final int index, final int usage) {
        this.index = index;
        this.usage = usage;
    }

    private void start() {
        id = glGenBuffers();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, id);
    }

    private void finish() {
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, index, id);
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
    }

    public void create(final long[] data) {
        start();
        glBufferData(GL_SHADER_STORAGE_BUFFER, data, usage);
        finish();
    }

    public void create(final float[] data) {
        start();
        glBufferData(GL_SHADER_STORAGE_BUFFER, data, usage);
        finish();
    }

    public void bind() {
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, index, id);
    }

    public void bind(final int index) {
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, index, id);
    }

    public void cleanUp() {
        glDeleteBuffers(id);
    }
}
