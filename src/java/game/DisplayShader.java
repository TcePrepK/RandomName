package game;

import shaders.BaseShader;
import toolbox.Vector2D;

import static core.GlobalVariables.mapChunkSize;
import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;

public class DisplayShader extends BaseShader {
    private int resolution;
    private int chunkSize;

    public DisplayShader() {
        super("/shaders/display.vert", "/shaders/display.frag");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        resolution = super.getUniformLocation("resolution");
        chunkSize = super.getUniformLocation("chunkSize");
    }

    public void loadResolution() {
        BaseShader.load2DVector(resolution, new Vector2D(WIDTH, HEIGHT));
    }

    public void loadChunkSize() {
        BaseShader.loadInt(chunkSize, mapChunkSize);
    }
}
