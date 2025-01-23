package rendering.camera;

import org.joml.Matrix4f;

import main.Window;


public class PerspectiveCamera extends Camera{

    private float aspect;
    public float FoV = 90;
    private float zNear = 0.01f;
    private float zFar = 1000.0f;

    public void setProjectionMatrix() {
        Matrix4f matrix  = new Matrix4f();
        matrix.identity();

        matrix.perspective((float)Math.toRadians(FoV),aspect,zNear,zFar);
        setProjectionMatrix(matrix);   
    }

    public void loadSpecific(){
        aspect = Window.width()/Window.height();
    }
    
    public float getzNear() {
        return zNear;
    }


    public void setzNear(float zNear) {
        this.zNear = zNear;
    }

    public float getzFar() {
        return zFar;
    }

    public void setzFar(float zFar) {
        this.zFar = zFar;
    }

    
    public float getAspect() {
        return aspect;
    }


    public void setAspect(float aspect) {
        this.aspect = aspect;
    }

    public float getFoV() {
        return FoV;
    }

    public void setFoV(float foV) {
        FoV = foV;
    }
    
}