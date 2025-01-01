package main;


 
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.openal.ALC10.*;

public class Window {

    private static long window;

    private static long audioContext;
    private static long audioDevice;

    private static int width = 300;
    private static int height = 500; 
    private static int resolutionWidth = 300;
    private static int resolutionHeight = 500;
    private static String title;

    private static float time = 0;

    public static void init(int _width,int _height,String _title){
        width = _width;
        height = _height;
        title = _title;
        GLFWErrorCallback.createPrint(System.err).set();
        
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }
        
        //glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(width, height, title,   NULL, NULL);


        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }
        
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        //ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        //ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

    }
    
    public static void setResolution(int w,int h){
        if(w == resolutionWidth && h == resolutionHeight){return;}

        resolutionWidth = w;
        resolutionHeight = h;

        GL11.glViewport(0, 0, w,h);
    }

    
    public static int getResolutionWidth() {
        return resolutionWidth;
    }

    public static int getResolutionHeight() {
        return resolutionHeight;
    }


    //private static void freeMemory(){
    //    glfwFreeCallbacks(window);
    //    glfwDestroyWindow(window);
    //    
    //    glfwTerminate();
    //    glfwSetErrorCallback(null).free();
//
    //    glfwTerminate();
    //}
    

    public static int width(){
        return width;
    }

    public static int height(){
        return height;
    }

    public static float time(){
        return time;
    }

    public static long getWindow(){
        return window;
    }

    public static void setFullScreen(boolean fullScreen){
        glfwWindowHint(GLFW_MAXIMIZED, fullScreen ? 1 : 0);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, fullScreen ? 1 : 0);
        if (fullScreen) {
            
            long monitor = glfwGetPrimaryMonitor();
            
            GLFWVidMode videoMode = glfwGetVideoMode(monitor);
            
            glfwSetWindowMonitor(window, monitor, 0, 0, width, height, videoMode.refreshRate());
        } else {
            
            glfwSetWindowMonitor(window, NULL, 100, 100, width, height, 0);
        }
    }
    
}