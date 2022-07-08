package game.materials.update;

import display.DisplayManager;
import game.Simulation;
import game.materials.MaterialComponents;
import game.materials.MaterialEntity;
import game.materials.MaterialManager;
import toolbox.Points.Point2D;
import toolbox.Vector2D;

public class MaterialUpdater {
    private static Simulation simulation;

    public static void init(final Simulation simulation) {
        MaterialUpdater.simulation = simulation;
    }

    public static UpdateReport update(final int x, final int y, final MaterialEntity material) {
        final Point2D previousPos = new Point2D(x, y);

        final MaterialComponents components = material.getComponents();
        final Vector2D velocity = components.velocity;
        final Vector2D offset = components.positionOffset;

        velocity.y += 1f;
//        velocity.x -= 0.1f;

        final float dt = DisplayManager.getDelta();
        final float vx = x + velocity.x * dt + offset.x;
        final float vy = y + velocity.y * dt + offset.y;

        offset.x = vx % 1;
        offset.y = vy % 1;

        final Point2D updatedPos = new Point2D(vx, vy);
        if (!checkPoint(updatedPos)) {
//            velocity.x *= -1;
//            velocity.y *= -1;

            return new UpdateReport(previousPos, previousPos, true);
        }

        return new UpdateReport(previousPos, updatedPos, true);
    }

    private static boolean checkPoint(final Point2D pos) {
        final MaterialEntity target = simulation.world.getMaterial(pos.x, pos.y);
        return target == MaterialManager.spaceEntity;
    }
}
