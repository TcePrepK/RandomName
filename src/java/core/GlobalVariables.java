package core;

import game.Simulation;
import renderers.MasterRenderer;

import java.util.Random;

public class GlobalVariables {
    public static int currentFrame = 0;

    // Debugging
    public static boolean renderDirtyRects = false;
    // Debugging

    // Core
    public static ImGuiManager imGuiManager = new ImGuiManager();
    public static Loader loader = new Loader();
    public static ThreadManager threadManager = new ThreadManager();
    // Core

    // Simulation
    public final static double seed = new Random().nextGaussian() * 65536;

    public static Simulation simulation = new Simulation();
    // Simulation

    // User
    public final static Random rand = new Random((long) GlobalVariables.seed);
    // User

    // Output
    public static MasterRenderer renderer = new MasterRenderer();
    // Output
}
