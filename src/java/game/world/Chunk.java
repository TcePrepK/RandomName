package game.world;

import core.imageBuffers.ImageBuffer2D;
import game.Simulation;
import game.materials.MaterialComponents;
import game.materials.MaterialEntity;
import game.materials.MaterialManager;
import org.jetbrains.annotations.NotNull;
import toolbox.Vector2D;

import java.nio.ByteBuffer;

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

    private final ByteBuffer idGrid;
    private final MaterialComponents[] componentGrid;

    private final ImageBuffer2D chunkBuffer;
    private long bindlessTextureID = 0;

    private boolean shouldUpdateBuffer = true;

    private final Simulation simulation;

    public Chunk(final Simulation simulation, final int x, final int y) {
        this.simulation = simulation;

        final int chunkSize = simulation.mapChunkSize;

        idGrid = ByteBuffer.allocateDirect(chunkSize * chunkSize);
        componentGrid = new MaterialComponents[chunkSize * chunkSize];

        chunkBuffer = new ImageBuffer2D(chunkSize, chunkSize, 0, 0, GL_R8, GL_RED, GL_UNSIGNED_BYTE);

        this.x = x * chunkSize;
        this.y = y * chunkSize;

        activeRect = new DirtyRect(simulation, this.x, this.y, chunkSize, chunkSize);
        nextRect = new DirtyRect(simulation, this.x, this.y, chunkSize, chunkSize, true);

        for (int i = 0; i < chunkSize; i++) {
            for (int j = 0; j < chunkSize; j++) {
                final int ox = this.x + i;
                final int oy = this.y + j;
                if (World.inBounds(ox, oy)) {
                    continue;
                }

                idGrid.put(i + chunkSize * j, (byte) MaterialManager.boundID);
            }
        }
    }

    public void update() {
        activeRect.update(nextRect);

        activeRect.copy(nextRect);
        nextRect = new DirtyRect(simulation, x, y, simulation.mapChunkSize, simulation.mapChunkSize, true);
    }

    public void updateBuffer() {
        if (!shouldUpdateBuffer) {
            return;
        }
        shouldUpdateBuffer = false;

//        chunkBuffer.update();

        glMakeTextureHandleNonResidentARB(bindlessTextureID);
        glDeleteTextures(chunkBuffer.getID());

        idGrid.flip();
        chunkBuffer.create(idGrid);
        idGrid.clear();

        bindlessTextureID = glGetTextureHandleARB(chunkBuffer.getID());
        glMakeTextureHandleResidentARB(bindlessTextureID);
    }

    public void setMaterial(final int x, final int y, @NotNull final MaterialEntity material) {
        final int idx = getIDX(x, y);

        final int previousID = idGrid.get(idx);
        if (material.getID() != 0 && !MaterialManager.getMaterialByID(previousID).isReplaceable()) {
            return;
        }

        idGrid.put(idx, material.getID());
        componentGrid[idx] = material.getComponents();

        shouldUpdateBuffer = true;
    }

    public MaterialEntity getMaterial(final int x, final int y) {
        final int idx = getIDX(x, y);

        final byte id = idGrid.get(idx);
        if (id == MaterialManager.spaceID) {
            return MaterialManager.spaceEntity;
        } else if (id == MaterialManager.boundID) {
            return MaterialManager.boundEntity;
        }

        final MaterialComponents components = componentGrid[idx];

        return new MaterialEntity(id, components);
    }

    public int getIDX(final int x, final int y) {
        return (x - this.x) + (y - this.y) * simulation.mapChunkSize;
    }

    private boolean inBounds(final int x, final int y) {
        return !(x < this.x || y < this.y || x >= this.x + simulation.mapChunkSize || y >= this.y + simulation.mapChunkSize);
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
        return "(" + (x / simulation.mapChunkSize) + ", " + (y / simulation.mapChunkSize) + ')';
    }
}
