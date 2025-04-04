package main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

public class Game {

    private static Scene currentScene;

    private static double time;
    private static int FPSPublic = 60;
    private static double deltaTime;
    private static double startFrameTime;
    private static double startTime;

    public static void changeSence(String sceneName){
        currentScene = Assets.getScene(sceneName);
        currentScene.load();
        currentScene.init();
    }

    public static void init(String name,int width,int height,Boolean fullString, String sceneName) {

        Input.init();
        
        if(fullString){
            Window.init(1920,1080, name);
            Window.setFullScreen(true);
        }
        else{
            Window.init(width, height, name);
        }
        Assets.init();
        

        int FPS = 0;
        double FPS_timer = System.nanoTime();
        startTime = System.nanoTime();
        startFrameTime = System.nanoTime();
        time = 0;
        deltaTime = 0;

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND); 

        currentScene = Assets.getScene(sceneName);
        currentScene.load();
        currentScene.init();

        while (!Input.getKeyPress("q")) {
            double currentFrameTime = System.nanoTime();
            
            deltaTime = (currentFrameTime - startFrameTime) / 1000000000.0;
            
            time = (currentFrameTime - startTime) / 1000000000.0;

            startFrameTime = System.nanoTime();
            
            Input.update();
            glfwPollEvents();

            glClearColor(0, 0, 0, 0);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            currentScene.update();
            currentScene.draw();

            FPS++;
            if (System.nanoTime() - FPS_timer > 1000000000.0) {
                FPS_timer = System.nanoTime();
                FPSPublic = FPS;
                FPS = 0;
            }

            glfwSwapBuffers(Window.getWindow());
        }
    }

    public static double getTime() {
        return time;
    }
    
    public static double getDeltaTime() {
        return deltaTime;
    }

    public static int getFPS() {
        return FPSPublic;
    }

    public static Scene getCurrentScene(){
        return currentScene;
    }
}