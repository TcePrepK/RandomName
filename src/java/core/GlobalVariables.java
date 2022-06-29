package core;

import game.World;
import renderers.MasterRenderer;

import java.util.Random;

public class GlobalVariables {
    public static int currentFrame = 0;

    // Core
    public static ImGuiManager imGuiManager = new ImGuiManager();
    public static Loader loader = new Loader();
    public static ThreadManager threadManager = new ThreadManager();
    // Core

    // World
    public final static double mapSeed = new Random().nextGaussian() * 65536;

    public final static World world = new World();
    // World

    // User
    public final static Random rand = new Random((long) GlobalVariables.mapSeed);
    // User

    // Output
    public static MasterRenderer renderer = new MasterRenderer();
    // Output
}
