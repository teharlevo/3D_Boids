package main;

import rendering.Model;
import rendering.camera.Camera;
import specific.MovingCamera;

public class App {
    public static void main(String[] args) {// cmd /C ""C:\Program Files\Java\jdk-17\bin\java.exe" @C:\Users\tehar\AppData\Local\Temp\cp_8bhd8oh98bij35zn4yvm3jvt6.argfile main.App "
    
        trigerComponents();

        Game.init("hi", 500, 500,"g1");
    }

    public static void trigerComponents(){
        
        new Model();
        new MovingCamera();
        new Camera() {
            
            public void setProjectionMatrix() {
                
            }
            
            public void loadSpecific() {
                
            }
            
        };
    }
}
