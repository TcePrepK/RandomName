package game.materials;

import core.SSBO;
import game.materials.types.liquid.Water;
import game.materials.types.solid.immovable.Stone;
import game.materials.types.solid.movable.Sand;
import game.materials.types.stranges.Bound;
import game.materials.types.stranges.Space;
import toolbox.Color;
import toolbox.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15C.GL_DYNAMIC_READ;

public class MaterialManager {
    public static byte lastID = 0;

    private static final List<BasicMaterial> materialList = new ArrayList<>();

    private static final SSBO colorsSSBO = new SSBO(0, GL_DYNAMIC_READ);

    public static byte spaceID;
    public static byte boundID;

    public static MaterialEntity spaceEntity;
    public static MaterialEntity boundEntity;

    public static void init() {
        registerMaterial(new Space());
        spaceID = (byte) (lastID - 1);

        registerMaterial(new Sand());
        registerMaterial(new Water());
        registerMaterial(new Stone());

        registerMaterial(new Bound());
        boundID = (byte) (lastID - 1);

        generateColorList();

        generateEntities();
    }

    private static void generateEntities() {
        spaceEntity = new MaterialEntity(spaceID, null);
        boundEntity = new MaterialEntity(boundID, null);
    }

    public static MaterialComponents createComponents(final int id) {
        final MaterialComponents components = new MaterialComponents();

        return getMaterialByID(id).initComponents(components);
    }

    private static void generateColorList() {
        final float[] colorList = new float[materialList.size() * 4];

        for (int i = 0; i < materialList.size(); i++) {
            final Color color = materialList.get(i).getColor();

            colorList[i * 4] = color.r;
            colorList[i * 4 + 1] = color.g;
            colorList[i * 4 + 2] = color.b;
            colorList[i * 4 + 3] = color.a;
        }

        colorsSSBO.create(colorList);
    }

    private static void registerMaterial(final BasicMaterial material) {
        final int id = material.getID();
        final String name = material.getClass().getSimpleName();

        material.init(name);

        Logger.out(name + "(" + id + ") has been registered.");

        materialList.add(material);
    }

    public static BasicMaterial getMaterialByID(final int id) {
        return materialList.get(id);
    }

    public static int getIDByMaterial(final BasicMaterial material) {
        return materialList.indexOf(material);
    }

    public static int getElementAmount() {
        return lastID;
    }

    public static SSBO getColorsSSBO() {
        return colorsSSBO;
    }
}
