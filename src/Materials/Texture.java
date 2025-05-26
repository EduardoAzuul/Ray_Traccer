package Materials;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import vectors.Vector3D;

/**
 * Handles texture loading and sampling for materials.
 * Supports reading image files and providing color information
 * based on UV coordinates (texture coordinates).
 *
 * @author José Eduardo Moreno Paredes
 */
public class Texture {
    /** The loaded image data */
    private BufferedImage image;
    /** Width of the texture in pixels */
    private int width;
    /** Height of the texture in pixels */
    private int height;
    /** Path to the texture file */
    private String filePath;

    /**
     * Creates a new texture from an image file.
     * @param filePath Path to the texture image file (supports formats readable by ImageIO)
     * @throws RuntimeException if the texture fails to load
     */
    public Texture(String filePath) {
        this.filePath = filePath;
        loadTexture(filePath);
    }

    /**
     * Loads the texture image from disk.
     * @param filePath Path to the image file
     * @throws RuntimeException if the image cannot be loaded
     */
    private void loadTexture(String filePath) {
        try {
            File file = new File(filePath);
            image = ImageIO.read(file);
            if (image == null) {
                throw new IOException("Unsupported image format");
            }
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load texture: " + filePath, e);
        }
    }

    /**
     * Gets the color at specific UV coordinates.
     * Handles texture wrapping (repeats texture outside [0,1] range).
     * @param u Horizontal texture coordinate (0-1)
     * @param v Vertical texture coordinate (0-1)
     * @return The color at (u,v) packed as 0xRRGGBB
     */
    public int getColorAt(double u, double v) {
        // Normalize coordinates to [0,1] range with wrapping
        u = u - Math.floor(u);
        v = v - Math.floor(v);

        if (u < 0) u += 1;
        if (v < 0) v += 1;

        // Convert to pixel coordinates with clamping
        int x = Math.min(Math.max((int)(u * width), 0), width - 1);
        int y = Math.min(Math.max((int)((1 - v) * height), 0), height - 1);  // Flip V coordinate

        return image.getRGB(x, y);
    }

    /**
     * Gets the color at UV coordinates as a normalized Vector3D.
     * @param u Horizontal texture coordinate (0-1)
     * @param v Vertical texture coordinate (0-1)
     * @return Color as Vector3D with components in range [0,1]
     */
    public Vector3D getColorVector(double u, double v) {
        int rgb = getColorAt(u, v);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        return new Vector3D(r / 255.0, g / 255.0, b / 255.0);
    }

    /**
     * Gets the texture width in pixels.
     * @return Width of the texture
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the texture height in pixels.
     * @return Height of the texture
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the path to the texture file.
     * @return Original file path used to load the texture
     */
    public String getFilePath() {
        return filePath;
    }
}