package game.simulation;

import core.Keyboard;
import core.Mouse;
import toolbox.CustomRunnable;

import static core.GlobalVariables.chunkManager;
import static core.GlobalVariables.mapChunkSize;
import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;

public class World {
    private int r = 50;
    private int selectedMat = 1;

    public World() {
        Mouse.mouseMiddleMove.add(new CustomRunnable<Void, Object>() {
            @Override
            public void run() {

            }

            @Override
            public Void run(final Object... arg) {
                r += (int) arg[0];

                return null;
            }
        });

        Keyboard.keyPressed.add(() -> {
            selectedMat = 1;
        }, "1");

        Keyboard.keyPressed.add(() -> {
            selectedMat = 2;
        }, "2");

        Keyboard.keyPressed.add(() -> {
            selectedMat = 3;
        }, "3");
    }

    public void update() {
//        circleAt(rand.nextInt(WIDTH), rand.nextInt(HEIGHT), rand.nextInt(10));

        if (Mouse.isButtonDown(0)) {
            circleAt(Mouse.getPosition().x, Mouse.getPosition().y, r, selectedMat);
        }

        if (Mouse.isButtonDown(1)) {
            circleAt(Mouse.getPosition().x, Mouse.getPosition().y, r, 0);
        }
    }

    private static void circleAt(final int x, final int y, final int r, final int e) {
        final int rr = r * r;
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                if (i * i + j * j > rr) {
                    continue;
                }

                setGrid(i + x, j + y, e);
            }
        }
    }

    public static void setGrid(final int x, final int y, final int e) {
        if (!World.inBounds(x, y)) {
            return;
        }

        final int chunkX = x / mapChunkSize;
        final int chunkY = y / mapChunkSize;
        final Chunk chunk = chunkManager.getChunkChunkSpace(chunkX, chunkY, true);
        if (e != 0) {
            chunk.extendTo(x, y);
        }

        final int wakeX = (x % mapChunkSize == 0) ? -1 : (((x + 1) % mapChunkSize == 0) ? 1 : 0);
        final int wakeY = (y % mapChunkSize == 0) ? -1 : (((y + 1) % mapChunkSize == 0) ? 1 : 0);

        if (wakeX != 0 || wakeY != 0) {
            final Chunk neighbor = chunkManager.getChunkChunkSpace(chunkX + wakeX, chunkY + wakeY, false);
            if (neighbor != null) {
                neighbor.extendTo(x + wakeX, y + wakeY);
            }
        }

        chunk.setGrid(x, y, e);
    }

    public static int getGrid(final int x, final int y) {
        if (!World.inBounds(x, y)) {
            return 0;
        }

        final int chunkX = x / mapChunkSize;
        final int chunkY = y / mapChunkSize;
        final Chunk chunk = chunkManager.getChunkChunkSpace(chunkX, chunkY, true);

        return chunk.getGrid(x, y);
    }

    private static int getIDX(final int x, final int y) {
        return x + y * WIDTH;
    }

    private static boolean inBounds(final int x, final int y) {
        return !(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT);
    }
}
