package main;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;


public class GameObject {

    private Transform transform;

    private List<Component> components = new ArrayList<Component>();

    public GameObject(Vector3f pos,Vector3f rotation,Vector3f scale){
        transform = new Transform(pos, rotation, scale); 
    }

    public void addComponent(Component component){
        components.add(component);
    }

    public void removeComponent(Component component){
        components.remove(component);
    }
    
    public void load(){
        for (Component component : components) {
            component.load();
        }
    }

    public void init(){
        for (Component component : components) {
            component.init();
        }
    }

    public void update(){
        for (Component component : components) {
            component.update();
        }
    }

    public Transform getTransform() {
        return transform;
    }
}