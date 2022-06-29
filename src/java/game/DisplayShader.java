package game;

import shaders.BaseShader;
import toolbox.Vector2D;

import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;

public class DisplayShader extends BaseShader {
    private int resolution;

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
    }

    public void loadResolution() {
        load2DVector(resolution, new Vector2D(WIDTH, HEIGHT));
    }
}
