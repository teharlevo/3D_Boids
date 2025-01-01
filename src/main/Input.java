package main;
import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;

public class Input{

    private static int[] keyNums = new int[]{32,39,44,45,46,47,48,49,50,51,52,53,54,55,56,57,59,61,65,66,67,68,69
        ,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,96,256,257,258,259,260,261,262,263
        ,264,265,290,291,292,293,294
        ,295,296,297,298,299,300,314,320,321,322,323,324,325,326,327,328,329};
    private static String[] keyNames = new String[]{"space","'",",","-",".","/","0","1","2","3","4","5","6","7","8","9",";","="
    ,"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","left_bracket","left_bracket"
    ,"right_bracket","rave_accent","escape",new String(new char[]{'\n'}),"tab","backspace","insert","delete","right","left","down","up"
    ,"f1","f2","f3","f4","f5","f6","f7","f8","f9","f10","f11","f12","kp0","kp1","kp2","kp3","kp4","kp5","kp6","kp7","kp8","kp9"};

    private static float posX,posY;
    private static boolean keyPressed[] = new boolean[350];
    private static boolean keyPressedNow[] = new boolean[350];
    private static List<String> keyPrassList = new ArrayList<>();
    private static List<String> keyPrassNowList = new ArrayList<>();
    private static Map<String, Integer> keyByName = new HashMap<>();
    private static Map<Integer, String> keyByNum = new HashMap<>();
    private static boolean mouseButtonPressed[] = new boolean[3];
    private static boolean mouseButtonPressedNow[] = new boolean[3];

    private static int lastKeyToPress;

    public static void init(){
        for (int i = 0; i < keyNums.length; i++) {
            keyByName.put(keyNames[i], keyNums[i]);
            keyByNum.put(keyNums[i],keyNames[i]);
        }
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        posX = (float)xPos;
        posY = (float)yPos;
    }

    public static void update(){
        keyPrassNowList = new ArrayList<>();
        keyPressedNow = new boolean[350];
        mouseButtonPressedNow = new boolean[3];

        glfwSetCursorPosCallback(Window.getWindow(), Input::mousePosCallback);
        glfwSetKeyCallback(Window.getWindow(), Input::key_callback);
        glfwSetMouseButtonCallback(Window.getWindow(), Input::mouseButtonCallback);
    }

    public static void key_callback(long window, int key, int scancode, int action, int mods)
    {
        if (action == GLFW_PRESS) {
            keyPressed[key] = true;
            keyPressedNow[key] = true;

            lastKeyToPress = key;
            keyPrassNowList.add(keyByNum.get(key));
            keyPrassList.add(keyByNum.get(key));
        } 
        else if (action == GLFW_RELEASE) {
            keyPressed[key] = false;
            keyPrassList.remove(keyByNum.get(key));
        }
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            mouseButtonPressed[button] = true;
            mouseButtonPressedNow[button] = true;
        }
        else if (action == GLFW_RELEASE) {
            mouseButtonPressed[button] = false;
        }
    }

    public static float getMousePosX(){
        return posX;
    }

    public static float getMousePosY(){
        return posY;
    }

    public static Vector2f mouseToCenterOFscreen(){
        Vector2f mos = new Vector2f(getMousePosX() - Window.width()/2,getMousePosY() - Window.height()/2);
        glfwSetCursorPos(Window.getWindow(),Window.width()/2,Window.height()/2);
        return mos;
    }

    public static boolean getKeyPress(int key){
        return keyPressed[key];
    }

    public static boolean getKeyPressNow(int key){
        return keyPressedNow[key];
    }

    public static boolean getKeyPress(String keyName){
        int key = keyByName.get(keyName);
        return keyPressed[key];
    }

    public static boolean getKeyPressNow(String keyName){
        int key = keyByName.get(keyName);
        return keyPressedNow[key];
    }

    public static String[] getKeysPress(){
        String keyPrassArray[] = new String[keyPrassList.size()];
        keyPrassArray = keyPrassList.toArray(keyPrassArray);
        return keyPrassArray;
    }

    public static String[] getKeysPressNow(){
        String keyPrassNowArray[] = new String[keyPrassNowList.size()];
        keyPrassNowArray = keyPrassNowList.toArray(keyPrassNowArray);
        return keyPrassNowArray;
    }

    public static int getLastKeyToPress(){
        return lastKeyToPress;
    }

    public static String getLastKeyToPressName(){
        if(lastKeyToPress != 0){
            return keyByNum.get(lastKeyToPress);
        }
        return "";
    }

    public static boolean getMouseButtonPress(int button){
        return mouseButtonPressed[button];
    }

    public static boolean getMouseButtonPressNow(int button){
        return mouseButtonPressedNow[button];
    }
}