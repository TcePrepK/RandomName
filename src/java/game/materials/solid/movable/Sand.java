package game.materials.solid.movable;

import game.materials.MaterialComponents;
import toolbox.Points.Point2D;

public class Sand extends MaterialComponents {
    @Override
    public Point2D update(final int x, final int y) {
        return new Point2D(x + velocity.x, y + velocity.y);
    }
}
