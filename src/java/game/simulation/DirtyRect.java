package game.simulation;

import toolbox.CustomRunnable;
import toolbox.Maths;
import toolbox.Points.Point2D;

import java.util.List;
import java.util.Objects;

import static core.GlobalVariables.mapChunkSize;

public class DirtyRect {
    private int x, y;
    private int w, h;

    private final int ox;
    private final int oy;
    private final int ow;
    private final int oh;

    public DirtyRect(final int x, final int y, final int w, final int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        ox = x;
        oy = y;
        ow = w;
        oh = h;
    }

    public DirtyRect(final int x, final int y, final int w, final int h, final boolean old) {
        this.x = Integer.MAX_VALUE;
        this.y = Integer.MAX_VALUE;
        this.w = -Integer.MAX_VALUE;
        this.h = -Integer.MAX_VALUE;

        ox = x;
        oy = y;
        ow = w;
        oh = h;
    }

    public DirtyRect update(final DirtyRect nextRect) {
        updateRandomly(nextRect);

        x = nextRect.x;
        y = nextRect.y;
        w = nextRect.w;
        h = nextRect.h;

        return this;
    }

    private void updateRandomly(final DirtyRect nextRect) {
        final List<Integer> randX = Maths.shuffleNumbers(left(), right());
        if (randX.size() > mapChunkSize) {
            System.out.println("Test");
        }
        
        for (final int x : randX) {
            final List<Integer> randY = Maths.shuffleNumbers(top(), bottom());
            for (final int y : randY) {
                final int mat = World.getGrid(x, y);
                if (mat == 0 || mat == 2) {
                    continue;
                }

                boolean updated = false;
                if (World.getGrid(x, y + 1) == 0) {
                    World.setGrid(x, y + 1, mat);
                    nextRect.extendTo(x, y + 1);

                    updated = true;
                } else if (World.getGrid(x + 1, y + 1) == 0) {
                    World.setGrid(x + 1, y + 1, mat);
                    nextRect.extendTo(x + 1, y + 1);

                    updated = true;
                } else if (World.getGrid(x - 1, y + 1) == 0) {
                    World.setGrid(x - 1, y + 1, mat);
                    nextRect.extendTo(x - 1, y + 1);

                    updated = true;
                }

                if (!updated && mat == 3) {
                    if (World.getGrid(x + 1, y) == 0) {
                        World.setGrid(x + 1, y, mat);
                        nextRect.extendTo(x + 1, y);

                        updated = true;
                    } else if (World.getGrid(x - 1, y) == 0) {
                        World.setGrid(x - 1, y, mat);
                        nextRect.extendTo(x - 1, y);

                        updated = true;
                    }
                }

                if (updated) {
                    World.setGrid(x, y, 0);
                    nextRect.extendTo(x, y);
                }
            }
        }

        nextRect.extendAllSides(1).fixBounds();
    }

    public void extendTo(final int x, final int y) {
        final Point2D targetPoint = new Point2D(x, y);

        final Point2D leftTop = Maths.min(new Point2D(left(), top()), targetPoint);
        final Point2D rightBottom = Maths.max(new Point2D(right(), bottom()), targetPoint);
        final Point2D scale = rightBottom.sub(leftTop);

        this.x = leftTop.x;
        this.y = leftTop.y;
        w = scale.x;
        h = scale.y;
    }

    public DirtyRect extendAllSides(final int n) {
        x -= n;
        y -= n;
        w += 2 * n;
        h += 2 * n;

        return this;
    }

    public void runForAll(final CustomRunnable<Void, Object> func, final List<Point2D> pointList) {
        for (int x = this.x; x < this.x + w; x++) {
            for (int y = this.y; y < this.y + h; y++) {
                func.run(new Point2D(x, y), pointList);
            }
        }
    }

    public DirtyRect copy(final DirtyRect rect) {
        x = rect.x;
        y = rect.y;
        w = rect.w;
        h = rect.h;

        return this;
    }

    public void reset() {
        x = ox;
        y = oy;
        w = ow;
        h = oh;
    }

    public DirtyRect fixBounds() {
        x = (int) Maths.clamp(x, ox, ox + ow);
        y = (int) Maths.clamp(y, oy, oy + oh);
        w = (int) Maths.clamp(w, 0, ow - (x - ox) - 1);
        h = (int) Maths.clamp(h, 0, oh - (y - oy) - 1);

        return this;
    }

    public boolean isEmpty() {
        if (w <= 0 || h <= 0) {
            return true;
        }

        return !equals(fixBounds());
    }

    public int top() {
        return y;
    }

    public int right() {
        return x + w;
    }

    public int bottom() {
        return y + h;
    }

    public int left() {
        return x;
    }

    public Point2D leftTop() {
        return new Point2D(left(), top());
    }

    public Point2D rightTop() {
        return new Point2D(right(), top());
    }

    public Point2D leftBottom() {
        return new Point2D(left(), bottom());
    }

    public Point2D rightBottom() {
        return new Point2D(right(), bottom());
    }

    public boolean equals(final DirtyRect v) {
        if (this == v) {
            return true;
        }

        if (v == null) {
            return false;
        }

        return x == v.x && y == v.y && w == v.w && h == v.h;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, w, h);
    }

    @Override
    public String toString() {
        return "DirtyRect(" +
                "X: " + x +
                ", Y: " + y +
                ", W: " + w +
                ", H: " + h +
                ')';
    }
}
