package rendering;

import main.Assets.ComponentRegistry;
import main.Assets;
import main.Component;
import main.Game;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Model extends Component{

    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public String texture;

    public String mesh;

    private Matrix4f matrix;

    public void load(){
        Game.getCurrentScene().addModel(this, "default");
    }

    ShaderProgram sp;

    public void init() {
        
        sp = new ShaderProgram(Assets.getVertexShaderSrc("default"),Assets.getFragmentShaderSrc("default"));
    }

    public void update() {
        makeMatrix();
    }

    public void bind(ShaderProgram sp){
        sp.uploadMat4f("uModel", matrix);
        Assets.getMesh(mesh).bind();
        Assets.getTexture(texture).bind(0);
    }

    private void makeMatrix(){
        Matrix4f matrix = new Matrix4f();
		matrix.identity();

        Vector3f transform = new Vector3f();
        getGameObject().getTransform().position.add(position,transform);

        Vector3f rot = new Vector3f();
        getGameObject().getTransform().rotation.add(rotation,rot);

        Vector3f objectScale = new Vector3f();
        getGameObject().getTransform().scale.mul(scale,objectScale);
        
        matrix =   matrix.translate(transform, matrix);
        matrix = matrix.rotate((float) Math.toRadians(rot.x)
        , new Vector3f(1,0,0), matrix);

        matrix = matrix.rotate((float) Math.toRadians(rot.y)
        , new Vector3f(0,1,0),matrix);

        matrix = matrix.rotate((float) Math.toRadians(rot.z)
        , new Vector3f(0,0,1),matrix);

        matrix = matrix.scale(objectScale, matrix);
		this.matrix = matrix;
    }

    static {
        ComponentRegistry.registerComponent("Model", Model.class);
    }
}