package main;

import java.io.File;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import rendering.Mesh;
import rendering.Texture;

public class Assets {
    private static Map<String, Scene> sceneMap = new HashMap<>();

    private static Map<String, Texture> textureMap = new HashMap<>();

    private static Map<String, Mesh> meshMap = new HashMap<>();

    private static Map<String, String>   vertexShaderMap = new HashMap<>();
    private static Map<String, String> fragmentShaderMap = new HashMap<>();

    private static void importScene(String path) {
        String name = new File(path).getName();
        name = name.substring(0, name.indexOf("."));
        if(sceneMap.get(name) == null){
            Scene scene = new Scene(path);
            sceneMap.put(name, scene);
        }
    }

    public static void importTexture(String path){
        String name = new File(path).getName();
        name = name.substring(0, name.indexOf("."));
        if(textureMap.get(name) == null){
            Texture texture = new Texture(path);
            textureMap.put(name, texture);
        }
    }
    
    public static void importTexture(Texture texture,String name){
        textureMap.put(name, texture);
    }

    public static void importMesh(String path){
        String name = new File(path).getName();
        name = name.substring(0, name.indexOf("."));
        if(meshMap.get(name) == null){
            Mesh mesh = new Mesh(path);
            meshMap.put(name, mesh);
        }
    }

    public static void importMesh(Mesh mesh,String name){
        meshMap.put(name, mesh);
    }
    
    public static void importShaderSrc(String path){
        File file = new File(path);
        String name = file.getName();
        String data;
        try {
            data = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            return;
        }

        String key = name.substring(name.indexOf(".") + 1);
        name = name.substring(0, name.indexOf("."));
        if(key.equals("fs")){
            fragmentShaderMap.put(name,data);
        }
        else if(key.equals("vs")){
            vertexShaderMap.put(name,data);
        }
    }

    public static void importFolderTextures(String path){
        File folder = new File(path);
        if(folder.listFiles() == null){
            return;
        }
        for (File fileEntry : folder.listFiles()) {
            if(!fileEntry.isDirectory()){
                importTexture(fileEntry.getAbsolutePath());
            }
            else{
                importFolderTextures(fileEntry.getAbsolutePath());
            }
        }
    }

    public static void importFolderMeshs(String path){
        File folder = new File(path);
        if(folder.listFiles() == null){
            return;
        }
        for (File fileEntry : folder.listFiles()) {
            if(!fileEntry.isDirectory()){
                importMesh(fileEntry.getAbsolutePath());
            }
            else{
                importFolderMeshs(fileEntry.getAbsolutePath());
            }
        }
    }

    public static void importFolderScene(String path){
        File folder = new File(path);
        if(folder.listFiles() == null){
            return;
        }
        for (File fileEntry : folder.listFiles()) {
            if(!fileEntry.isDirectory()){
                importScene(fileEntry.getAbsolutePath());
            }
            else{
                importFolderScene(fileEntry.getAbsolutePath());
            }
        }
    }

    public static void importFolderShadersSrc(String path){
        File folder = new File(path);
        if(folder.listFiles() == null){
            return;
        }
        for (File fileEntry : folder.listFiles()) {
            if(!fileEntry.isDirectory()){
                importShaderSrc(fileEntry.getAbsolutePath());
            }
            else{
                importFolderShadersSrc(fileEntry.getAbsolutePath());
            }
        }
    }

    public static Scene getScene(String name){
        return sceneMap.get(name);
    }

    public static Texture getTexture(String name){
        return textureMap.get(name);
    }

    public static Mesh getMesh(String name){
        return meshMap.get(name);
    }

    public static String getFragmentShaderSrc(String name){
        return fragmentShaderMap.get(name);
    }

    public static String getVertexShaderSrc(String name){
        return vertexShaderMap.get(name);
    }

    public static void init(){
        Mesh m = new Mesh(indicesArrayBox,vertexArrayBox);
        meshMap.put("box",m);


        importFolderShadersSrc("assets\\shaders");
        importFolderTextures("assets\\textures");
        importFolderMeshs("assets\\meshs");
        importFolderScene("assets\\scenes");

    }

    public class ComponentRegistry {
        private static final Map<String, Class<? extends Component>> registry = new HashMap<>();
    
        public static void registerComponent(String type, Class<? extends Component> componentClass) {
            registry.put(type, componentClass);
        }
    
        public static Class<? extends Component> getComponentClass(String type) {
            return registry.get(type);
        }
    }

    private static float[] vertexArrayBox = {
        1.0f, 1.0f, 0.0f,   0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
        1.0f, -1.0f, 0.0f,  0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,  0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        -1.0f,  1.0f, 0.0f,  0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
    };

    private static int[] indicesArrayBox = { 
        0, 1, 3,  
        1, 2, 3,
    };
}