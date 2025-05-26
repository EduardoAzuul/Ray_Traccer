/**
 * Denoisser.java
 *
 * This class provides methods to apply image denoising using
 * Box Blur and Gaussian Blur techniques.
 *
 * Author: José Eduardo Moreno Paredes
 * Date: 2025
 */

package Denoisser;

import java.awt.image.BufferedImage;

public class Denoisser {

    /**
     * Applies a Box Blur filter to the given image.
     * This filter averages the colors of a pixel and its 8 surrounding neighbors (3x3 grid).
     *
     * @param image The original image to be blurred.
     * @return A new image with the Box Blur effect applied.
     */
    public static BufferedImage boxBlur(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());

        // Iterate over each pixel, excluding the border pixels
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int r = 0, g = 0, b = 0;

                // Sum RGB values of the 3x3 neighborhood
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        int rgb = image.getRGB(x + i, y + j);
                        r += (rgb >> 16) & 0xFF; // Red component
                        g += (rgb >> 8) & 0xFF;  // Green component
                        b += rgb & 0xFF;         // Blue component
                    }
                }

                // Calculate average color values
                r /= 9;
                g /= 9;
                b /= 9;

                // Compose new RGB value and set it in the result image
                int newRGB = (r << 16) | (g << 8) | b;
                result.setRGB(x, y, newRGB);
            }
        }

        return result;
    }

    /**
     * Applies a Gaussian Blur filter to the given image.
     * Uses a 3x3 Gaussian kernel with weights approximating a normal distribution.
     *
     * @param image The original image to be blurred.
     * @return A new image with the Gaussian Blur effect applied.
     */
    public static BufferedImage gaussianBlur(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());

        // 3x3 Gaussian kernel with sigma ≈ 1
        double[][] kernel = {
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}
        };
        double kernelSum = 16.0; // Total weight of the kernel

        // Iterate over each pixel, excluding the border pixels
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double r = 0, g = 0, b = 0;

                // Apply convolution using the Gaussian kernel
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        int rgb = image.getRGB(x + i, y + j);
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        double weight = kernel[j + 1][i + 1];
                        r += red * weight;
                        g += green * weight;
                        b += blue * weight;
                    }
                }

                // Normalize the RGB values by dividing by the kernel sum
                int newR = Math.min(255, Math.max(0, (int)(r / kernelSum)));
                int newG = Math.min(255, Math.max(0, (int)(g / kernelSum)));
                int newB = Math.min(255, Math.max(0, (int)(b / kernelSum)));

                // Compose the new RGB value and set it in the result image
                int newRGB = (newR << 16) | (newG << 8) | newB;
                result.setRGB(x, y, newRGB);
            }
        }

        return result;
    }
}
