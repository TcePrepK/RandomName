package core;

import org.lwjgl.BufferUtils;
import toolbox.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static display.DisplayManager.HEIGHT;
import static display.DisplayManager.WIDTH;
import static org.lwjgl.opengl.GL11C.*;

public class ScreenShotManager {
    private static final String path = "res/screenshots";
    private static final File file = new File(path);

    private static boolean screenshot = false;

    public static void init() {
        if (!file.exists() && !file.mkdir()) {
            Logger.error("Wasn't able to generate screenshots folder");
        }

        Keyboard.keyPressed.add(() -> {
            screenshot = true;
        }, "F12");
    }

    public static void update() {
        if (!screenshot) {
            return;
        }

        final Date date = Calendar.getInstance().getTime();
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss");
        final String name = dateFormat.format(date);

        final String format = "png";
        final File imageFile = new File(path, name + "." + format);

        final BufferedImage image = getImage();

        try {
            ImageIO.write(image, format, imageFile);
        } catch (final Exception e) {
            Logger.error(e.getMessage());
        }

        screenshot = false;
    }

    private static BufferedImage getImage() {
        final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        final ByteBuffer buffer = BufferUtils.createByteBuffer(WIDTH * HEIGHT * 3);

        // Creates a new buffer and stores the displays data into it.
        glReadPixels(0, 0, WIDTH, HEIGHT, GL_RGB, GL_UNSIGNED_BYTE, buffer);

        // Transfers the data from the buffer into the image. This requires bit shifts to get the components data.
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                final int idx = (x + y * WIDTH) * 3;

                final byte red = buffer.get(idx);
                final byte green = buffer.get(idx + 1);
                final byte blue = buffer.get(idx + 2);

                image.setRGB(x, HEIGHT - y - 1, (red << 16) + (green << 8) + blue);
            }
        }

        return image;
    }

    public static void takeScreenshot() {
        screenshot = true;
    }
}
