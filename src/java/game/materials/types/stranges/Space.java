package game.materials.types.stranges;

import game.materials.BasicMaterial;
import toolbox.Color;

public class Space extends BasicMaterial {
    public Space() {
        super(new Color(85, 85, 85));
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
