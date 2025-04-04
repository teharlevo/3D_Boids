package specific;

import main.Assets.ComponentRegistry;
import main.Input;
import main.Component;

public class MovingCamera extends Component{
    public float speed;
    public float rotSpeed;

    
    static {
        ComponentRegistry.registerComponent("MovingCamera", MovingCamera.class);
    }
    
    public void init() {
        
    }
    public void load() {
        
    }
    public void update() {

        if(Input.getKeyPress("up")){
            getGameObject().getTransform().position.z += speed;
        }
        if(Input.getKeyPress("down")){
            getGameObject().getTransform().position.z -= speed;
        }
        if(Input.getKeyPress("left")){
            getGameObject().getTransform().position.x += speed;
        }
        if(Input.getKeyPress("right")){
            getGameObject().getTransform().position.x -= speed;
        }
        
        if(Input.getKeyPress("w")){
            getGameObject().getTransform().rotation.x += rotSpeed;
        }
        if(Input.getKeyPress("s")){
            getGameObject().getTransform().rotation.x -= rotSpeed;
        }
        if(Input.getKeyPress("a")){
            getGameObject().getTransform().rotation.y += rotSpeed;
        }
        if(Input.getKeyPress("d")){
            getGameObject().getTransform().rotation.y -= rotSpeed;
        }
    }
}
