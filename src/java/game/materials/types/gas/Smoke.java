package game.materials.types.gas;

import game.materials.BasicMaterial;
import game.materials.MaterialComponents;
import toolbox.Color;

public class Smoke extends BasicMaterial {
    public Smoke() {
        super(new Color(115, 130, 118));
    }

    @Override
    public MaterialComponents initComponents(final MaterialComponents components) {
        components.velocity.y *= -1;

        return components;
    }
}
