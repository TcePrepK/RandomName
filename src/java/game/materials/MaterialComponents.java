package game.materials;

import toolbox.Points.Point2D;
import toolbox.Vector2D;

public abstract class MaterialComponents {
    protected Vector2D velocity = Vector2D.randomVector(-1, 1);

    public Point2D update(final int x, final int y) {
        return new Point2D(x, y);
    }
}
