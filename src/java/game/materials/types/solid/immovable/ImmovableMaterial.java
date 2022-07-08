package game.materials.types.solid.immovable;

import game.materials.BasicMaterial;
import toolbox.Color;

public class ImmovableMaterial extends BasicMaterial {
    public ImmovableMaterial(final Color color) {
        super(color);
    }

    @Override
    public boolean canMove() {
        return false;
    }
}
