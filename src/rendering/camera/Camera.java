package rendering.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import main.Assets.ComponentRegistry;
import main.Component;
import main.Game;
import rendering.ShaderProgram;

public abstract class Camera extends Component{
    public String name;

    public Vector3f position;
    public Vector3f rotation;
    private Vector3f orientation = new Vector3f(0,  1, 0);

    private Matrix4f projectionMatrix = new Matrix4f();

    private Matrix4f viewMatrix = new Matrix4f();

    public abstract void setProjectionMatrix();

    private void setViewMatrix(){
        Vector3f transform = new Vector3f();
        getGameObject().getTransform().position.add(position,transform);

        Vector3f direction = new Vector3f(0,0,-1);

        direction.rotateX((float) Math.toRadians(getGameObject().getTransform().rotation.x() + rotation.x));
        direction.rotateY((float) Math.toRadians(getGameObject().getTransform().rotation.y() + rotation.y));
        direction.rotateZ((float) Math.toRadians(getGameObject().getTransform().rotation.z() + rotation.z));



        
        Vector3f lookPoaint = new Vector3f(new Vector3f(
            transform.x - direction.x,transform.y - direction.y,transform.z - direction.z
        )); 

        Matrix4f matrix  = new Matrix4f();
        matrix.identity();

        matrix.lookAt(transform,lookPoaint,orientation,matrix);
        viewMatrix = matrix;
    }

    public void bind(ShaderProgram s){
        s.uploadMat4f("uProjection", projectionMatrix);
        s.uploadMat4f("uView", viewMatrix);
    }

    public Vector3f getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector3f orientation) {
        this.orientation = orientation;
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public abstract void loadSpecific();//לעשות שיט שצריך לעשות בטעינה שספציפים לסוגגג המצלמה

    public void load() {
        loadSpecific();
        Game.getCurrentScene().addCamera(this, name);

    }

    public void init() {   
        
    }

    public void update() {
        setProjectionMatrix();
        setViewMatrix();
    }

    static {
        ComponentRegistry.registerComponent("PerspectiveCamera", PerspectiveCamera.class);
        ComponentRegistry.registerComponent("OrthographicCamera", OrthographicCamera.class);

    }
}