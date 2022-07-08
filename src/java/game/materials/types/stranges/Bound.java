package game.materials.types.stranges;

import game.materials.BasicMaterial;
import toolbox.Color;

public class Bound extends BasicMaterial {
    public Bound() {
        super(new Color(48, 48, 48));
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean isReplaceable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
