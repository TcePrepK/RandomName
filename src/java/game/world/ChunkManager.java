package game.world;

import game.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static core.GlobalVariables.renderDirtyRects;
import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;

public class ChunkManager {
    private final List<Chunk> chunkList = new ArrayList<>();
    private final Map<Integer, Chunk> chunkMap = new HashMap<>();

//    private final List<List<Chunk>> chunksByIndex = new ArrayList<>();

//    private final int loopSize = 2;
//    private final ChunkUpdate[] chunkThreads = new ChunkUpdate[loopSize * loopSize];

    private final List<Long> chunkImageIDs = new ArrayList<>();

    private final ExecutorService service = Executors.newFixedThreadPool(4);

    private final Simulation simulation;

    public ChunkManager(final Simulation simulation) {
        this.simulation = simulation;
    }

    public void updateBuffers() {
        chunkImageIDs.clear();

        final int visibleChunkWidth = (int) Math.ceil(WIDTH / (float) simulation.mapChunkSize);
        final int visibleChunkHeight = (int) Math.ceil(HEIGHT / (float) simulation.mapChunkSize);
        for (int y = 0; y < visibleChunkHeight; y++) {
            for (int x = 0; x < visibleChunkWidth; x++) {
                final Chunk chunk = getChunkChunkSpace(x, y, false);
                if (chunk == null) {
                    chunkImageIDs.add((long) 0);
                    continue;
                }

                chunk.updateBuffer();
                chunkImageIDs.add(chunk.getBindlessTextureID());
            }
        }
    }

    public Chunk getChunkChunkSpace(final int x, final int y, final boolean createIfNull) {
        final int idx = getChunkIndex(x, y);

        Chunk chunk = chunkMap.get(idx);
        if (chunk != null) {
            return chunk;
        }

        if (!createIfNull) {
            return null;
        }

        chunk = new Chunk(simulation, x, y);

        chunkList.add(chunk);
        chunkMap.put(idx, chunk);

        return chunk;
    }

    private static int getChunkIndex(final int x, final int y) {
        return (x & 0xFFFF) + ((y & 0xFFFF) << 0xF);
    }

    public void update(final Simulation simulation) {
        final List<Future<?>> futures = new ArrayList<>();
        for (final Chunk chunk : chunkList) {
            futures.add(service.submit(chunk::update));
        }

        while (!futures.isEmpty()) {
            if (futures.get(0).isDone()) {
                futures.remove(0);
            }
        }

        updateBuffers();
    }

    public void render() {
        if (!renderDirtyRects) {
            return;
        }

        for (final Chunk chunk : chunkList) {
            chunk.draw();
        }
    }

    public List<Long> getChunkImageIDs() {
        return chunkImageIDs;
    }
}
