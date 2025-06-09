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
import rendering.ShaderProgram;
import rendering.Sprite;
import rendering.Texture;
import rendering.camera.Camera;
import rendering.opengl_objects.FrameBuffer;
import rendering.renderers.Effect;
import rendering.renderers.MeshRenderer;
import rendering.renderers.SpriteRenderer;

public class Scene {

    private List<GameObject> gameObjects = new ArrayList<GameObject>();
    
    private Map<String, Camera> cameraMap = new HashMap<>();

    private Map<String, List<Model>> modelMap  = new HashMap<>();
    private Map<String, List<Sprite>> spriteMap  = new HashMap<>();

    private Map<String, FrameBuffer> frameBufferMap  = new HashMap<>();

    private Map<String, MeshRenderer> meshRendererMap  = new HashMap<>();
    private Map<String, SpriteRenderer> spriteRendererMap  = new HashMap<>();
    private Map<String, Effect> effectMap  = new HashMap<>();

    private String renderPipelineJson;

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

            JsonArray frameBuffersJson = json.get("frameBuffers").getAsJsonArray();
            for (JsonElement frameBufferElement : frameBuffersJson) {
                JsonObject frameBufferObj = frameBufferElement.getAsJsonObject();
                createFrameBuffer(frameBufferObj);
            }

            renderPipelineJson = json.get("pipelineActions").toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
         
    private void createRenderer(JsonObject rendererObj){
        String type = rendererObj.get("type").getAsString();
        String name = rendererObj.get("name").getAsString();
        System.out.println(type);

        if(type.equals("mesh")){
            String fragmentShader = rendererObj.get("fragmentShader").getAsString();
            String vertexShader = rendererObj.get("vertexShader").getAsString();
            
            meshRendererMap.put(name, new MeshRenderer(vertexShader, fragmentShader));
        }
        else if(type.equals("sprite")){
            String fragmentShader = rendererObj.get("fragmentShader").getAsString();
            String vertexShader = rendererObj.get("vertexShader").getAsString();

            spriteRendererMap.put(name, new SpriteRenderer(vertexShader, fragmentShader));
        }
        else if(type.equals("effect")){
            String fragmentShader = rendererObj.get("fragmentShader").getAsString();
            effectMap.put(name, new Effect(fragmentShader));
        }
    }

    private void createFrameBuffer(JsonObject rendererObj){
        String name = rendererObj.get("name").getAsString();
        int width = rendererObj.get("width").getAsInt();
        int height = rendererObj.get("height").getAsInt();

        frameBufferMap.put(name, new FrameBuffer(width, height));
    }

    private static Component createComponent(String type, JsonObject properties) {
        try {
            Class<? extends Component> componentClass = ComponentRegistry.getComponentClass(type);
            if (componentClass == null) {
                System.err.println("Unknown component type: " + type);
                return null;
            }
            
            Component component = componentClass.getDeclaredConstructor().newInstance();
            
            if(properties == null){return component;}
            
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

        JsonArray renderPipeline = JsonParser.parseString(renderPipelineJson).getAsJsonArray();
        for (JsonElement actionElement : renderPipeline) {
            JsonObject actionObj = actionElement.getAsJsonObject();
            drawAction(actionObj);
        }
    }

    public void drawAction(JsonObject actionObj){
        String type = actionObj.get("type").getAsString();
        switch (type){
            case "draw":{
                String rendererType = actionObj.get("rendererType").getAsString();
                String rendererName = actionObj.get("rendererName").getAsString();
                String renderGroup = actionObj.get("renderGroup").getAsString();
                String cam = actionObj.get("cam").getAsString();

                switch (rendererType) {
                    case "mesh":
                        meshRendererMap.get(rendererName).draw(cam, renderGroup);
                        break;
                    case "spirte":
                        System.out.println("W");
                        spriteRendererMap.get(rendererName).draw(cam, renderGroup);
                        break;
                    default:
                        break;
                }
                break;
            }

            case "setResolution":{
                Window.setResolution(
                    actionObj.get("width").getAsInt()
                    ,actionObj.get("height").getAsInt());
                break;
            }
            case "drawEffect":{
                String name = actionObj.get("name").getAsString();
                boolean clearColor = actionObj.get("clearColor").getAsBoolean();
                boolean clearDepth = actionObj.get("clearDepth").getAsBoolean();
                
                effectMap.get(name).draw(clearColor, clearDepth);
                break;
            }

            case "bind":{
                String frameBufferName = actionObj.get("frameBufferName").getAsString();
                frameBufferMap.get(frameBufferName).bind();
                break;
            }

            case "unbind":{
                String frameBufferName = actionObj.get("frameBufferName").getAsString();
                frameBufferMap.get(frameBufferName).unbind();
                break;
            }
            
            case "upload":{
                String uploadType = actionObj.get("uploadType").getAsString();
                String uploadName = actionObj.get("uploadName").getAsString();
                String value = actionObj.get("value").getAsString();

                String loaderType = actionObj.get("loaderType").getAsString();
                String loaderName = actionObj.get("loaderName").getAsString();
                ShaderProgram sp = meshRendererMap.get("default").getSP();

                switch (loaderType) {
                    case "effect":
                        sp = effectMap.get(loaderName).getSP();
                        break;
                    
                    case "mesh":
                        sp = meshRendererMap.get(loaderName).getSP();
                        break;
                    case "sprite":
                        sp = spriteRendererMap.get(loaderName).getSP();
                        break;
                
                    default:
                        break;
                }


                switch (uploadType) {
                    case "texture":{
                        sp.uploadTexture(uploadName, getTextureFromScene(value));
                        break;
                    }

                    case "int":{
                        sp.uploadInt(uploadName, Integer.parseInt(value));
                        break;
                    }

                    case "float":{
                        sp.uploadInt(uploadName, Integer.parseInt(value));
                        break;
                    }

                    default:
                        break;
                }
                break;
            }
        }

    }

    private Texture getTextureFromScene(String name){
        if(name.indexOf(".texture") != -1){
            return frameBufferMap.get(name.substring(0,name.indexOf("."))).getTexture();
        }
        return Assets.getTexture(name);
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

    public List<Sprite> getSprites(String name){
        return spriteMap.get(name);
    }

    public void addSprite(Sprite sprite,String name){
        if(!spriteMap.containsKey(name)){
            spriteMap.put(name, new ArrayList<>());
        }
        spriteMap.get(name).add(sprite);
    }

    public void removeSprite(Sprite sprite,String name){
        spriteMap.get(name).remove(sprite);
    }

    public String getRenderPipelineJson() {
        return renderPipelineJson;
    }

    public void setRenderPipelineJson(String renderPipelineJson) {
        this.renderPipelineJson = renderPipelineJson;
    }
}