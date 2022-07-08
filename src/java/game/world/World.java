package game.world;

import core.Mouse;
import game.Simulation;
import game.materials.MaterialEntity;
import game.materials.MaterialManager;

import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;

public class World {
    private final Simulation simulation;

    public World(final Simulation simulation) {
        this.simulation = simulation;
    }

    public void update() {
        if (Mouse.isButtonDown(0)) {
            circleAt(Mouse.getPosition().x, Mouse.getPosition().y, simulation.placeCircleRadius, simulation.selectedMaterial);
        }

        if (Mouse.isButtonDown(1)) {
            circleAt(Mouse.getPosition().x, Mouse.getPosition().y, simulation.placeCircleRadius, (byte) 0);
        }
    }

    private void circleAt(final int x, final int y, final int r, final byte id) {
        final int rr = r * r;
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                if (i * i + j * j > rr) {
                    continue;
                }

//                final Vector2D dist = new Vector2D(i, j).normalize().add(Vector2D.randomVector(-0.5f, 0.5f)).mult(20);

                final MaterialEntity material = new MaterialEntity(id);
//                material.getComponents().velocity = dist;

                setMaterial(i + x, j + y, material);
            }
        }
    }

    public void setMaterial(final int x, final int y, final MaterialEntity material) {
        if (!World.inBounds(x, y)) {
            return;
        }

        final int chunkX = x / simulation.mapChunkSize;
        final int chunkY = y / simulation.mapChunkSize;
        final Chunk chunk = simulation.chunkManager.getChunkChunkSpace(chunkX, chunkY, true);
        chunk.extendTo(x, y);

        final int wakeX = (x % simulation.mapChunkSize == 0) ? -1 : (((x + 1) % simulation.mapChunkSize == 0) ? 1 : 0);
        final int wakeY = (y % simulation.mapChunkSize == 0) ? -1 : (((y + 1) % simulation.mapChunkSize == 0) ? 1 : 0);

        if (wakeX != 0 || wakeY != 0) {
            final Chunk neighbor = simulation.chunkManager.getChunkChunkSpace(chunkX + wakeX, chunkY + wakeY, false);
            if (neighbor != null) {
                neighbor.extendTo(x + wakeX, y + wakeY);
            }
        }

        chunk.setMaterial(x, y, material);
    }

    public MaterialEntity getMaterial(final int x, final int y) {
        if (!World.inBounds(x, y)) {
            return new MaterialEntity(MaterialManager.boundID, null);
        }

        final int chunkX = x / simulation.mapChunkSize;
        final int chunkY = y / simulation.mapChunkSize;
        final Chunk chunk = simulation.chunkManager.getChunkChunkSpace(chunkX, chunkY, true);

        return chunk.getMaterial(x, y);
    }

    private static int getIDX(final int x, final int y) {
        return x + y * WIDTH;
    }

    public static boolean inBounds(final int x, final int y) {
        final int r = 5;
        return !(x < r || y < r || x >= WIDTH - r || y >= HEIGHT - r);
    }
}
