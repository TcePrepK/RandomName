package game.materials.update;

import toolbox.Points.Point2D;

public class UpdateReport {
    private final Point2D previousPos;
    private final Point2D updatedPos;
    private final boolean updated;

    public UpdateReport(final Point2D previousPos, final Point2D updatedPos, final boolean updated) {
        this.previousPos = previousPos;
        this.updatedPos = updatedPos;
        this.updated = updated;
    }

    public UpdateReport(final Point2D pos) {
        previousPos = pos;
        updatedPos = pos;
        updated = false;
    }

    public Point2D getPreviousPos() {
        return previousPos;
    }

    public Point2D getUpdatedPos() {
        return updatedPos;
    }

    public boolean isUpdated() {
        return updated;
    }
}
