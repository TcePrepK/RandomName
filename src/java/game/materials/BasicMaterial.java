package game.materials;

import toolbox.Color;
import toolbox.Points.Point2D;

public abstract class BasicMaterial {
    protected byte id;
    private final Color color;

    private String name;

    public BasicMaterial(final Color color) {
        this.color = color;

        id = MaterialManager.lastID++;
    }

    // Initializes
    public void init(final String name) {
        this.name = name;
    }

    public MaterialComponents initComponents(final MaterialComponents components) {
        return components;
    }
    // Initializes

    public static Point2D update(final int x, final int y, final MaterialComponents c) {
        return new Point2D(x, y);
    }

    // Functions
    public boolean canMove() {
        return true;
    }

    public boolean isReplaceable() {
        return true;
    }

    public boolean isPickable() {
        return true;
    }
    // Functions

    // Getters
    public byte getID() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
    // Getters
}
