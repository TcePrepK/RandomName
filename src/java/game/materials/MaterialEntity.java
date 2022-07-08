package game.materials;

public class MaterialEntity {
    private final byte id;
    private final BasicMaterial material;
    private final MaterialComponents components;

    public MaterialEntity(final byte id, final MaterialComponents components) {
        this.id = id;
        material = MaterialManager.getMaterialByID(id);
        this.components = components;
    }

    public MaterialEntity(final byte id) {
        this.id = id;
        material = MaterialManager.getMaterialByID(id);
        components = MaterialManager.createComponents(id);
    }

    // Functions
    public boolean canMove() {
        return material.canMove();
    }

    public boolean isReplaceable() {
        return material.isReplaceable();
    }

    public boolean isPickable() {
        return material.isPickable();
    }
    // Functions

    public byte getID() {
        return id;
    }

    public MaterialComponents getComponents() {
        return components;
    }
}
