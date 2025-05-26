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
    // Total number of animation frames to render
    private static final int TOTAL_FRAMES = 120;

    // Radius of the circular path that the lights will move along
    private static final double LIGHT_RADIUS = 15.0;

    // Height at which the lights move in the Y-axis (vertical axis)
    private static final double LIGHT_HEIGHT = 13.0;

    public static void main(String[] args) {
        // Format timestamps for logging
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Starting animation render at: " + LocalDateTime.now().format(formatter));
        LocalDateTime startTime = LocalDateTime.now();

        // Create an output directory named "frames" if it doesn't exist,
        // to store individual frame images for the animation
        File framesDir = new File("frames");
        if (!framesDir.exists()) {
            framesDir.mkdir();
        }

        try {
            // Load the 3D parrot model once before rendering frames for efficiency
            System.out.println("Loading parrot object...");
            LocalDateTime loadObjStart = LocalDateTime.now();

            // Path to the parrot OBJ file
            String pathObj = "ObjFiles/Objs/heavyParrot.obj";

            // Color, rotation, position, and scale for the parrot model
            Vector3D color = new Vector3D(255, 255, 255);      // White color tint
            Vector3D rotation = new Vector3D(0, 180, 0);       // Rotate 180 degrees around Y axis
            Vector3D position = new Vector3D(0, -5, -11);      // Position in 3D space
            Vector3D scale = new Vector3D(3, 3, 3);             // Scale uniformly by 3x

            // Load the texture image for the parrot
            Texture texture = new Texture("Textures/heavyParrot.png");

            // Log loading time (though actual loading of OBJ is inside loop when creating ObjObject)
            LocalDateTime loadObjEnd = LocalDateTime.now();
            Duration loadObjDuration = Duration.between(loadObjStart, loadObjEnd);
            System.out.println("Time to load OBJ object: " + loadObjDuration.toMillis() + " milliseconds");

            // Loop over all frames to render animation
            for (int frame = 0; frame < TOTAL_FRAMES; frame++) {
                System.out.println("Rendering frame " + (frame + 1) + "/" + TOTAL_FRAMES);
                LocalDateTime frameStart = LocalDateTime.now();

                // --- Camera setup ---
                Camera camera = new Camera(
                        new Vector3D(0, 0, 0),   // Camera position at origin
                        new Vector3D(0, 0, 0),   // No rotation
                        1.5,                     // Near clipping plane
                        600,                     // Far clipping plane
                        4096,                    // Output image width
                        2160,                    // Output image height
                        60                       // Field of view in degrees
                );
                camera.setBounces(2);           // Set ray bounces (reflection/refraction depth)

                // --- Scene setup ---
                Scene scene = new Scene(camera);

                // Create the parrot object for the current frame with set transformations
                ObjObject parrot = new ObjObject(TipicalMaterials.WOOD, color, rotation, position, scale, pathObj);
                parrot.setTexture(texture);      // Apply texture to the object
                scene.addObject(parrot);          // Add the parrot to the scene

                // --- Animate lights ---

                // Calculate current angle in radians for this frame (full circle over total frames)
                double angle1 = (2.0 * Math.PI * frame) / TOTAL_FRAMES;

                // Second light moves opposite (180 degrees offset)
                double angle2 = angle1 + Math.PI;

                // First light position - moves clockwise on a circle at LIGHT_HEIGHT Y
                Vector3D lightPos1 = new Vector3D(
                        LIGHT_RADIUS * Math.cos(angle1),   // X coordinate on circle
                        LIGHT_HEIGHT,                      // Y coordinate fixed height
                        -2 + LIGHT_RADIUS * Math.sin(angle1) // Z coordinate offset by -2
                );
                Vector3D lightColor1 = new Vector3D(255, 255, 255); // White light
                scene.addLight(new PointLight(lightPos1, lightColor1, 30)); // Add light to scene with intensity 30

                // Second light position - moves counter-clockwise on opposite circle at -LIGHT_HEIGHT Y
                Vector3D lightPos2 = new Vector3D(
                        LIGHT_RADIUS * Math.cos(-angle2),  // X coordinate (negated for CCW)
                        -LIGHT_HEIGHT,                     // Y coordinate negative height
                        -2 + LIGHT_RADIUS * Math.sin(-angle2) // Z coordinate offset by -2
                );
                Vector3D lightColor2 = new Vector3D(180, 180, 255); // Blue-ish light
                scene.addLight(new PointLight(lightPos2, lightColor2, 15)); // Add light with lower intensity

                // --- Render the scene ---
                BufferedImage image = scene.render();

                // Apply Gaussian blur denoiser to reduce noise/artifacts
                image = Denoisser.gaussianBlur(image);

                // Save the rendered frame image with zero-padded filename
                String filename = String.format("frames/frame_%03d.png", frame);
                ImageIO.write(image, "PNG", new File(filename));

                // Log how long this frame took to render
                LocalDateTime frameEnd = LocalDateTime.now();
                Duration frameDuration = Duration.between(frameStart, frameEnd);
                System.out.println("Frame " + (frame + 1) + " completed in " + frameDuration.toMillis() + " milliseconds");
            }

            System.out.println("All frames rendered successfully!");

            // After rendering all frames, create a video using FFmpeg
            createVideo();

        } catch (Exception e) {
            // Print any errors encountered during rendering or video creation
            System.err.println("Error during rendering: " + e.getMessage());
            e.printStackTrace();
        }

        // Log total time taken for entire animation process
        Duration totalDuration = Duration.between(startTime, LocalDateTime.now());
        System.out.println("Total animation duration: " + totalDuration.toMillis() + " milliseconds");
        System.out.println("Animation completed at: " + LocalDateTime.now().format(formatter));
    }

    /**
     * Create a video file from the rendered frames using FFmpeg.
     * This method creates a text file listing the frames twice for looping,
     * then runs an FFmpeg process to stitch them into an mp4 video.
     */
    private static void createVideo() {
        try {
            System.out.println("Creating video from frames...");

            // Create a frame list file that repeats the frames twice for looping effect
            createFrameList();

            // FFmpeg command for creating a video from the frames listed in the text file
            String[] command = {
                    "ffmpeg",
                    "-y",                 // Overwrite existing output file if any
                    "-f", "concat",       // Use concat demuxer to read multiple inputs
                    "-safe", "0",         // Allow unsafe file paths in the list file
                    "-i", "frame_list.txt", // Input file list
                    "-framerate", "12",   // Output video frame rate (12 FPS)
                    "-c:v", "libx264",    // Use H.264 codec
                    "-pix_fmt", "yuv420p",// Pixel format for compatibility with players
                    "-crf", "18",         // Constant Rate Factor - quality (lower is better)
                    "parrot_animation.mp4" // Output video filename
            };

            // Run the FFmpeg process and inherit IO so we see its output
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            Process process = pb.start();

            // Wait for FFmpeg to complete
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Video created successfully: parrot_animation.mp4");
                System.out.println("Video duration: 20 seconds (120 frames × 2 loops ÷ 12fps)");
                // Delete the temporary frame list file after success
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

    /**
     * Creates a text file "frame_list.txt" listing each rendered frame twice.
     * This allows FFmpeg to create a video that loops the animation twice smoothly.
     * Each entry contains the file path and duration (1/12 seconds per frame).
     */
    private static void createFrameList() throws IOException {
        System.out.println("Creating frame list for double loop...");

        try (java.io.PrintWriter writer = new java.io.PrintWriter("frame_list.txt")) {
            // Loop twice to repeat the animation frames twice
            for (int loop = 0; loop < 2; loop++) {
                for (int frame = 0; frame < TOTAL_FRAMES; frame++) {
                    // Absolute path of the frame file
                    String framePath = new File("frames/frame_" + String.format("%03d", frame) + ".png").getAbsolutePath();

                    // Write file path line (required by ffmpeg concat)
                    writer.println("file '" + framePath + "'");
                    // Write duration line for frame display time (1/12th second)
                    writer.println("duration 0.083333");
                }
            }
            // Add the last frame one more time without duration for correct ending
            String lastFramePath = new File("frames/frame_" + String.format("%03d", TOTAL_FRAMES - 1) + ".png").getAbsolutePath();
            writer.println("file '" + lastFramePath + "'");
        }

        System.out.println("Frame list created: 240 total frames (120 × 2 loops)");
    }
}
