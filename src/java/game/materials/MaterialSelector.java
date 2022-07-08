package game.materials;

import core.Keyboard;
import core.Mouse;
import game.Simulation;

public class MaterialSelector {
    public static void init(final Simulation simulation) {
        Mouse.mouseMiddleMove.add(arg -> {
            simulation.placeCircleRadius += (int) arg[0];

            if (simulation.placeCircleRadius < 1) {
                simulation.placeCircleRadius = 1;
            }

            return null;
        });
    }

    public static void update(final Simulation simulation) {
        for (final String key : Keyboard.pressedList) {
            try {
                final int mat = Integer.parseInt(key);
                if (mat >= MaterialManager.getElementAmount()) {
                    continue;
                }

                if (!MaterialManager.getMaterialByID(mat).isPickable()) {
                    continue;
                }

                simulation.selectedMaterial = (byte) mat;
                return;
            } catch (final NumberFormatException ignored) {
            }
        }
    }
}
