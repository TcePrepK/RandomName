package game.simulation;

import core.imageBuffers.ImageBuffer2D;
import game.materials.MaterialComponents;
import toolbox.Vector2D;

import java.nio.ByteBuffer;

import static core.GlobalVariables.mapChunkSize;
import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;
import static org.lwjgl.opengl.ARBBindlessTexture.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_RED;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL30C.GL_R8;

public class Chunk {
    private final int x, y;

    public DirtyRect activeRect;
    public DirtyRect nextRect;

    private final ByteBuffer grid = ByteBuffer.allocateDirect(mapChunkSize * mapChunkSize);
    private final MaterialComponents[] componentGrid = new MaterialComponents[mapChunkSize * mapChunkSize];
    
    private final ImageBuffer2D chunkBuffer = new ImageBuffer2D(mapChunkSize, mapChunkSize, 0, 0, GL_R8, GL_RED, GL_UNSIGNED_BYTE);
    private long bindlessTextureID = 0;

    private boolean shouldUpdateBuffer = false;

    public Chunk(final int x, final int y) {
        this.x = x * mapChunkSize;
        this.y = y * mapChunkSize;

        activeRect = new DirtyRect(this.x, this.y, mapChunkSize, mapChunkSize);
        nextRect = new DirtyRect(this.x, this.y, mapChunkSize, mapChunkSize, true);
    }

    public void update() {
        activeRect.update(nextRect);

        activeRect.copy(nextRect);
        nextRect = new DirtyRect(x, y, mapChunkSize, mapChunkSize, true);
    }

    public void updateBuffer() {
        if (!shouldUpdateBuffer) {
            return;
        }
        shouldUpdateBuffer = false;

//        chunkBuffer.update();

        glMakeTextureHandleNonResidentARB(bindlessTextureID);
        glDeleteTextures(chunkBuffer.getID());

        grid.flip();
        chunkBuffer.create(grid);
        grid.clear();

        bindlessTextureID = glGetTextureHandleARB(chunkBuffer.getID());
        glMakeTextureHandleResidentARB(bindlessTextureID);
    }

    public void setGrid(final int x, final int y, final int e) {
        grid.put(getIDX(x, y), (byte) e);
        shouldUpdateBuffer = true;
    }

    public int getGrid(final int x, final int y) {
        if (!inBounds(x, y)) {
            return 0;
        }

        return grid.get(getIDX(x, y));
    }

    public int getIDX(final int x, final int y) {
        return (x - this.x) + (y - this.y) * mapChunkSize;
    }

    private boolean inBounds(final int x, final int y) {
        return !(x < this.x || y < this.y || x >= this.x + mapChunkSize || y >= this.y + mapChunkSize);
    }

    public void extendTo(final int x, final int y) {
        nextRect.extendTo(x, y);
    }

    public void draw() {
        final Vector2D scale = new Vector2D(WIDTH / 2, HEIGHT / 2);
        final Vector2D leftTop = new Vector2D(activeRect.leftTop()).div(scale).sub(1);
        final Vector2D rightTop = new Vector2D(activeRect.rightTop()).div(scale).sub(1);
        final Vector2D leftBottom = new Vector2D(activeRect.leftBottom()).div(scale).sub(1);
        final Vector2D rightBottom = new Vector2D(activeRect.rightBottom()).div(scale).sub(1);

        glBegin(GL_LINE_LOOP);
        glVertex2f(leftTop.x, -leftTop.y);
        glVertex2f(rightTop.x, -rightTop.y);
        glVertex2f(rightBottom.x, -rightBottom.y);
        glVertex2f(leftBottom.x, -leftBottom.y);
        glEnd();
    }

    public long getBindlessTextureID() {
        return bindlessTextureID;
    }

    @Override
    public String toString() {
        return "(" + (x / mapChunkSize) + ", " + (y / mapChunkSize) + ')';
    }
}
