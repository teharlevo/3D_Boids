package rendering.camera;

import org.joml.Matrix4f;

public class OrthographicCamera extends Camera {

    public float width;
    public float height;

    private float zFar = 1000.0f;

    public void setProjectionMatrix() {
        Matrix4f matrix  = new Matrix4f();
        matrix.identity();

        matrix.ortho(width/-2, width/2, height/-2, height/2, 0.0f, zFar);
        setProjectionMatrix(matrix);  
    }

    public void loadSpecific(){
        //כלופ
    }

    public float getZFar() {
        return zFar;
    }

    public void setZFar(float zFar) {
        this.zFar = zFar;
    }

}
