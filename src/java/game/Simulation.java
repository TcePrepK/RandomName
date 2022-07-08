package game;

import game.materials.MaterialSelector;
import game.materials.update.MaterialUpdater;
import game.renderer.DisplayRenderer;
import game.world.ChunkManager;
import game.world.World;

import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;

public class Simulation {
    public final int mapChunkSize = 128;

    public byte selectedMaterial = 1;
    public int placeCircleRadius = 50;

    private final DisplayRenderer displayRenderer = new DisplayRenderer(this);
    public final ChunkManager chunkManager = new ChunkManager(this);
    public final World world = new World(this);

    public void init() {
        for (int i = 0; i < WIDTH / mapChunkSize; i++) {
            for (int j = 0; j < HEIGHT / mapChunkSize; j++) {
                chunkManager.getChunkChunkSpace(i, j, true);
            }
        }

        MaterialSelector.init(this);
        MaterialUpdater.init(this);
    }

    public void update() {
        MaterialSelector.update(this);

        world.update();

        chunkManager.update(this);
    }

    public void render() {
        displayRenderer.render(this);
    }
}
