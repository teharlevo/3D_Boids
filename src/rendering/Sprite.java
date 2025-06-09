package rendering;

import main.Assets.ComponentRegistry;
import main.Assets;
import main.Component;
import main.Game;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Sprite extends Component{

    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public String texture;

    public String renderGroup;

    private Matrix4f matrix;

    public void load(){
        Game.getCurrentScene().addSprite(this, renderGroup);
    }

    public void init() {
    }

    public void update() {
        makeMatrix();
    }

    public void bind(ShaderProgram sp){
        sp.uploadMat4f("uModel", matrix);
        Assets.getMesh("box").bind();
        sp.uploadTexture("uTexture", Assets.getTexture(texture));
    }

    private void makeMatrix(){
        Texture tex = Assets.getTexture(texture);
        float nor = (float)Math.sqrt(tex.width()*tex.width() + tex.height()*tex.height());

        Matrix4f matrix = new Matrix4f();
		matrix.identity();

        Vector3f transform = new Vector3f();
        getGameObject().getTransform().position.add(position,transform);

        Vector3f rot = new Vector3f();
        getGameObject().getTransform().rotation.add(rotation,rot);

        Vector3f objectScale = new Vector3f();
        getGameObject().getTransform().scale.mul(scale,objectScale);
        
        objectScale = new Vector3f();
        getGameObject().getTransform().scale.mul(new Vector3f(
            tex.width()/(nor)
            ,tex.height()/(nor),1),objectScale);
        
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
        ComponentRegistry.registerComponent("Sprite", Sprite.class);
    }
}