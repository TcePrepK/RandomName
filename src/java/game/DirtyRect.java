package game;

import toolbox.CustomRunnable;
import toolbox.Points.Point2D;

public class DirtyRect {
    public int x, y;
    public int w, h;

    public DirtyRect(final int x, final int y, final int w, final int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public static void createRectWithAllPoints(final CustomRunnable<Point2D, Point2D> func) {

    }
}
