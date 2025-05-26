import Denoisser.Denoisser;
import Lights.Light;
import Lights.PointLight;
import Materials.Texture;
import Objects.*;
import PreBuildScenes.HunterScene;
import PreBuildScenes.MuseumAngels;
import PreBuildScenes.Office;
import PreBuildScenes.Temple;
import ResuableElements.TipicalMaterials;
import vectors.Vector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ParrotScene {
    private static final int TOTAL_FRAMES = 120;
    private static final double LIGHT_RADIUS = 15.0;
    private static final double LIGHT_HEIGHT = 13.0;

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Starting animation render at: " + LocalDateTime.now().format(formatter));
        LocalDateTime startTime = LocalDateTime.now();

        // Create output directory for frames
        File framesDir = new File("frames");
        if (!framesDir.exists()) {
            framesDir.mkdir();
        }

        try {
            // Load the parrot object once (outside the frame loop for efficiency)
            System.out.println("Loading parrot object...");
            LocalDateTime loadObjStart = LocalDateTime.now();

            String pathObj = "ObjFiles/Objs/heavyParrot.obj";
            Vector3D color = new Vector3D(255, 255, 255);
            Vector3D rotation = new Vector3D(0, 180, 0);
            Vector3D position = new Vector3D(0, -5, -11);
            Vector3D scale = new Vector3D(3, 3, 3);
            Texture texture = new Texture("Textures/heavyParrot.png");

            LocalDateTime loadObjEnd = LocalDateTime.now();
            Duration loadObjDuration = Duration.between(loadObjStart, loadObjEnd);
            System.out.println("Time to load OBJ object: " + loadObjDuration.toMillis() + " milliseconds");

            // Render each frame
            for (int frame = 0; frame < TOTAL_FRAMES; frame++) {
                System.out.println("Rendering frame " + (frame + 1) + "/" + TOTAL_FRAMES);
                LocalDateTime frameStart = LocalDateTime.now();

                // Create the camera
                Camera camera = new Camera(
                        new Vector3D(0, 0, 0),   // origin
                        new Vector3D(0, 0, 0),   // rotation
                        1.5,                     // near plane
                        600,                     // far plane
                        4096,                    // width
                        2160,                    // height
                        60                       // field of view
                );
                camera.setBounces(2);

                // Create the scene
                Scene scene = new Scene(camera);

                // Add the parrot object
                ObjObject parrot = new ObjObject(TipicalMaterials.WOOD, color, rotation, position, scale, pathObj);
                parrot.setTexture(texture);
                scene.addObject(parrot);

                // Calculate light positions for this frame
                double angle1 = (2.0 * Math.PI * frame) / TOTAL_FRAMES;
                double angle2 = angle1 + Math.PI; // Second light 180 degrees opposite

                // First light (white) - moves clockwise
                Vector3D lightPos1 = new Vector3D(
                        LIGHT_RADIUS * Math.cos(angle1),
                        LIGHT_HEIGHT,
                        -2 + LIGHT_RADIUS * Math.sin(angle1)
                );
                Vector3D lightColor1 = new Vector3D(255, 255, 255);
                scene.addLight(new PointLight(lightPos1, lightColor1, 30));

                // Second light (blue-tinted) - moves counter-clockwise
                Vector3D lightPos2 = new Vector3D(
                        LIGHT_RADIUS * Math.cos(-angle2),
                        -LIGHT_HEIGHT,
                        -2 + LIGHT_RADIUS * Math.sin(-angle2)
                );
                Vector3D lightColor2 = new Vector3D(180, 180, 255);
                scene.addLight(new PointLight(lightPos2, lightColor2, 15));

                // Render the scene
                BufferedImage image = scene.render();
                image = Denoisser.gaussianBlur(image);

                // Save the frame with zero-padded filename
                String filename = String.format("frames/frame_%03d.png", frame);
                ImageIO.write(image, "PNG", new File(filename));

                LocalDateTime frameEnd = LocalDateTime.now();
                Duration frameDuration = Duration.between(frameStart, frameEnd);
                System.out.println("Frame " + (frame + 1) + " completed in " + frameDuration.toMillis() + " milliseconds");
            }

            System.out.println("All frames rendered successfully!");

            // Create video using FFmpeg
            createVideo();

        } catch (Exception e) {
            System.err.println("Error during rendering: " + e.getMessage());
            e.printStackTrace();
        }

        // Log the total duration
        Duration totalDuration = Duration.between(startTime, LocalDateTime.now());
        System.out.println("Total animation duration: " + totalDuration.toMillis() + " milliseconds");
        System.out.println("Animation completed at: " + LocalDateTime.now().format(formatter));
    }

    private static void createVideo() {
        try {
            System.out.println("Creating video from frames...");

            // First, create a text file listing each frame twice for the double loop
            createFrameList();

            // FFmpeg command to create video at 12fps using the frame list
            String[] command = {
                    "ffmpeg",
                    "-y", // Overwrite output file if it exists
                    "-f", "concat", // Use concat demuxer
                    "-safe", "0", // Allow unsafe file paths
                    "-i", "frame_list.txt", // Input file list
                    "-framerate", "12", // 12 fps
                    "-c:v", "libx264", // Video codec
                    "-pix_fmt", "yuv420p", // Pixel format for compatibility
                    "-crf", "18", // High quality (lower is better, 18 is visually lossless)
                    "parrot_animation.mp4" // Output filename
            };

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO(); // Show FFmpeg output
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Video created successfully: parrot_animation.mp4");
                System.out.println("Video duration: 20 seconds (120 frames × 2 loops ÷ 12fps)");
                // Clean up the temporary file list
                new File("frame_list.txt").delete();
            } else {
                System.err.println("FFmpeg failed with exit code: " + exitCode);
                System.out.println("You can manually create the video using:");
                System.out.println("1. Create frame_list.txt with the frame sequence");
                System.out.println("2. ffmpeg -f concat -safe 0 -i frame_list.txt -framerate 12 -c:v libx264 -pix_fmt yuv420p -crf 18 parrot_animation.mp4");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error creating video: " + e.getMessage());
            System.out.println("You can manually create the video by:");
            System.out.println("1. Creating a frame_list.txt file listing each frame twice");
            System.out.println("2. Using: ffmpeg -f concat -safe 0 -i frame_list.txt -framerate 12 -c:v libx264 -pix_fmt yuv420p -crf 18 parrot_animation.mp4");
        }
    }

    private static void createFrameList() throws IOException {
        System.out.println("Creating frame list for double loop...");

        try (java.io.PrintWriter writer = new java.io.PrintWriter("frame_list.txt")) {
            // Write each frame twice to create the double loop effect
            for (int loop = 0; loop < 2; loop++) {
                for (int frame = 0; frame < TOTAL_FRAMES; frame++) {
                    String framePath = new File("frames/frame_" + String.format("%03d", frame) + ".png").getAbsolutePath();
                    writer.println("file '" + framePath + "'");
                    writer.println("duration 0.083333"); // 1/12 second per frame
                }
            }
            // Add the last frame one more time to complete the duration
            String lastFramePath = new File("frames/frame_" + String.format("%03d", TOTAL_FRAMES - 1) + ".png").getAbsolutePath();
            writer.println("file '" + lastFramePath + "'");
        }

        System.out.println("Frame list created: 240 total frames (120 × 2 loops)");
    }
}