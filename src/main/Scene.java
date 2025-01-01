package main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import main.Assets.ComponentRegistry;
import rendering.Model;
import rendering.camera.Camera;
import rendering.renderers.MeshRenderer;

public class Scene {

    private List<GameObject> gameObjects = new ArrayList<GameObject>();
    
    private Map<String, Camera> cameraMap = new HashMap<>();

    private Map<String, List<Model>> modelMap  = new HashMap<>();

    private Map<String, MeshRenderer> meshRendererMap  = new HashMap<>();

    public Scene(String filePath) {
        try (Reader reader = new FileReader(filePath)) {

            String name = new File(filePath).getName();
            name = name.substring(0, name.indexOf("."));
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            JsonArray gameObjectsJson = json.get("gameObjects").getAsJsonArray();
            for (JsonElement gameObjectElement : gameObjectsJson) {

                JsonObject gameObjectObject = gameObjectElement.getAsJsonObject();
                
                Vector3f position = gameObjectObject.has("position")
                ? getVector3fFromJson(gameObjectObject.getAsJsonObject("position"))
                : new Vector3f(); // Default to (0, 0, 0)
                Vector3f rotation = gameObjectObject.has("rotation")
                ? getVector3fFromJson(gameObjectObject.getAsJsonObject("rotation"))
                : new Vector3f(); // Default to (0, 0, 0)
                Vector3f scale = gameObjectObject.has("scale")
                ? getVector3fFromJson(gameObjectObject.getAsJsonObject("scale"))
                : new Vector3f(1, 1, 1); // Default to (1, 1, 1)

                GameObject gameObject = new GameObject(position, rotation, scale);

                JsonArray componentsJson = gameObjectObject.get("components").getAsJsonArray();
                for (JsonElement componentElement : componentsJson) {
                    JsonObject componentObj = componentElement.getAsJsonObject();
                    String type = componentObj.get("type").getAsString();
                    JsonObject properties = componentObj.getAsJsonObject("properties");

                    Component component = createComponent(type, properties);
                    if (component != null) {
                        gameObject.addComponent(component);
                        component.setGameObject(gameObject);
                    }
                }
                gameObjects.add(gameObject);
            }
            JsonArray renderersJson = json.get("renderers").getAsJsonArray();
            for (JsonElement rendererElement : renderersJson) {
                JsonObject rendererObj = rendererElement.getAsJsonObject();
                createRenderer(rendererObj);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Component createComponent(String type, JsonObject properties) {
        try {
            Class<? extends Component> componentClass = ComponentRegistry.getComponentClass(type);
            if (componentClass == null) {
                System.err.println("Unknown component type: " + type);
                return null;
            }
            
            Component component = componentClass.getDeclaredConstructor().newInstance();
            
            for (String key : properties.keySet()) {
                try {
                    var field = componentClass.getField(key);
                    var value = properties.get(key);
    
                    if (field.getType() == int.class) {
                        field.setInt(component, value.getAsInt());
                    } else if (field.getType() == String.class) {
                        field.set(component, value.getAsString());
                    } else if (field.getType() == float.class) {
                        field.setFloat(component, value.getAsFloat());
                    }else if (field.getType() == Vector3f.class) {
                        Vector3f vector = getVector3fFromJson(value.getAsJsonObject());
                        field.set(component, vector);
                    } else {
                        System.err.println("Unsupported field type: " + field.getType());
                    }
                } catch (NoSuchFieldException e) {
                    System.err.println("Field not found: " + key + " in component: " + type);
                }
            }
                            
            return component;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
         
    private void createRenderer(JsonObject rendererObj){
        String type = rendererObj.get("type").getAsString();
        String name = rendererObj.get("name").getAsString();

        if(type.equals("MeshRenderer")){
            String fragmentShader = rendererObj.get("fragmentShader").getAsString();
            String vertexShader = rendererObj.get("vertexShader").getAsString();
            
            meshRendererMap.put(name, new MeshRenderer(vertexShader, fragmentShader));
        }
    }

    private static Vector3f getVector3fFromJson(JsonObject json) {
        float x = json.has("x") ? json.get("x").getAsFloat() : 0.0f;
        float y = json.has("y") ? json.get("y").getAsFloat() : 0.0f;
        float z = json.has("z") ? json.get("z").getAsFloat() : 0.0f;
        return new Vector3f(x, y, z);
    }
    
    public void addGameObject(GameObject gameObject){
        gameObjects.add(gameObject);
        gameObject.load();
        gameObject.init();
    }

    public void removeGameObject(GameObject gameObject){
        gameObjects.remove(gameObject);
    }
    
    public void load(){
        for (GameObject gameObject : gameObjects) {
            gameObject.load();
        }    }

    public void init(){
        for (GameObject gameObject : gameObjects) {
            gameObject.init();
        }
    }

    public void update(){
        for (GameObject gameObject : gameObjects) {
            gameObject.update();
        }
    }

    public void draw(){
        meshRendererMap.get("default").draw("default", "default");
    }

    public Camera getCamera(String name){
        return cameraMap.get(name);
    }

    public void addCamera(Camera cam,String name){
        cameraMap.put(name, cam);
    }

    public void removeCamera(String name){
        cameraMap.remove(name);
    }

    public List<Model> getModels(String name){
        return modelMap.get(name);
    }

    public void addModel(Model model,String name){
        if(!modelMap.containsKey(name)){
            modelMap.put(name, new ArrayList<>());
        }
        modelMap.get(name).add(model);
    }

    public void removeModel(Model model,String name){
        modelMap.get(name).remove(model);
    }

    
}