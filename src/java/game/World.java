package game;

import core.Mouse;
import core.imageBuffers.ImageBuffer2D;

import java.nio.ByteBuffer;

import static core.GlobalVariables.rand;
import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;
import static org.lwjgl.opengl.GL11C.GL_RED;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL30C.GL_R8;

public class World {
    private final ByteBuffer grid = ByteBuffer.allocateDirect(WIDTH * HEIGHT);

    private final ImageBuffer2D worldBuffer = new ImageBuffer2D(WIDTH, HEIGHT, 0, 0, GL_R8, GL_RED, GL_UNSIGNED_BYTE);

    private boolean updateBuffers = false;

    public void updateBuffers() {
//        circleAt(rand.nextInt(WIDTH), rand.nextInt(HEIGHT), rand.nextInt(10));
        if (Mouse.isButtonDown(0)) {
            circleAt(Mouse.getPosition().x, Mouse.getPosition().y, 50);
        }

        randomUpdate();

        if (!updateBuffers) {
            return;
        }

        worldBuffer.update();

        grid.flip();
        worldBuffer.create(grid);
        grid.clear(); // 5463

        updateBuffers = false;

    }

    private void randomUpdate() {
        final int x = rand.nextInt(WIDTH);
        final int y = rand.nextInt(HEIGHT);
        final int r = 100;

        for (int i = -r; i <= r; i++) {
            for (int j = r; j >= -r; j--) {
                final int ox = x + i;
                final int oy = y + j;
                if (getGrid(ox, oy) == 0) {
                    continue;
                }

                final int bottom = getGrid(ox, oy + 1);
                if (bottom == 0) {
                    setGrid(ox, oy, 0);
                    setGrid(ox, oy + 1, 1);
                }
            }
        }
    }

    public void circleAt(final int x, final int y, final int r) {
        final int rr = r * r;
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                if (i * i + j * j > rr) {
                    continue;
                }

                setGrid(i + x, j + y, 1);
            }
        }
    }

    private void setGrid(final int x, final int y, final int e) {
        if (!inBounds(x, y)) {
            return;
        }

        grid.put(getIDX(x, y), (byte) e);

        updateBuffers = true;
    }

    private int getGrid(final int x, final int y) {
        if (!inBounds(x, y)) {
            return 0;
        }

        return grid.get(getIDX(x, y));
    }

    private static int getIDX(final int x, final int y) {
        return x + y * WIDTH;
    }

    private static boolean inBounds(final int x, final int y) {
        return !(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT);
    }

    public ImageBuffer2D getWorldBuffer() {
        return worldBuffer;
    }
}
