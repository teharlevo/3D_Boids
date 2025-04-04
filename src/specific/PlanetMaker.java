package specific;

import main.Assets.ComponentRegistry;
import rendering.Mesh;
import rendering.Texture;
import rendering.renderers.Renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

import main.Assets;
import main.Component;

public class PlanetMaker extends Component{

    public int resolution;

    private int trangCount = 0;
    
    private float[] vertexArray;
    private int[] indexArray;

    public void load() {
        makePlanet();
    }
    
    public void init() {
        
    }

    public void update() {
        
    }

    public void makePlanet(){
        makeHightMap();
        Assets.importMesh(makeMesh(), "planet");
    }

    private Texture makeHightMap(){

        return null;
    }

    private Mesh makeMesh(){
        vertexArray = new float[resolution * resolution * Renderer.vertexSize * 6 * 4];
        indexArray = new int[(resolution - 1) * (resolution - 1) * 6 * 6];
        makeSphere();
        return new Mesh(indexArray,vertexArray);
    }

    private void makeSphere(){
       makeSphereFace(new Vector3f(0, 0,1));
       makeSphereFace(new Vector3f(0, 0,-1));
       makeSphereFace(new Vector3f(0, 1,0));
       makeSphereFace(new Vector3f(0, -1,0));
       makeSphereFace(new Vector3f(1, 0,0));
       makeSphereFace(new Vector3f(-1, 0,0));
    }

    private void makeSphereFace(Vector3f localUp){
        
        int i = (int)(trangCount/1.5f) * Renderer.vertexSize;
        for (int x = 0; x < resolution; x++) {
            for (int y = 0; y < resolution; y++) {
                
                Vector3f axisA = new Vector3f(localUp.y(), localUp.z(), localUp.x());
                
                Vector3f axisB = new Vector3f();
                localUp.cross(axisA,axisB);
                axisB.normalize(axisB);
                Vector2f percent = new Vector2f(x, y).div(resolution - 1.0f);
                
                Vector3f pointA = axisA.mul((percent.x() - 0.5f) * 2.0f);
                Vector3f pointB = axisB.mul((percent.y() - 0.5f) * 2.0f);
                
                Vector3f pointOnUnitCube = new Vector3f();
                localUp.add(pointA,pointOnUnitCube);
                pointOnUnitCube.add(pointB,pointOnUnitCube);
                
                Vector3f pointOnSphere = pointOnUnitCube.normalize();


                vertexArray[i] =   pointOnSphere.x;//x
                vertexArray[i+1] = pointOnSphere.y;//y
                vertexArray[i+2] = pointOnSphere.z;//z
                vertexArray[i+3] = pointOnSphere.x;//xנורמל
                vertexArray[i+4] = pointOnSphere.y;//yנורמל
                vertexArray[i+5] = pointOnSphere.z;//zנורמל
                vertexArray[i+6] = percent.x;
                vertexArray[i+7] = percent.y;

                i = i + Renderer.vertexSize;
            }
        }
        int offset = (int)(trangCount/1.5f);
        i = offset;
        for (int x = 0; x < resolution - 1; x++) {
            for (int y = 0; y < resolution - 1; y++) {

                int indexTrangle = x + y * resolution;

                indexArray[trangCount] = indexTrangle + offset;
                indexArray[trangCount + 1] = indexTrangle + resolution + 1 + offset;
                indexArray[trangCount + 2] = indexTrangle + resolution + offset;

                indexArray[trangCount + 3] = indexTrangle + offset;
                indexArray[trangCount + 4] = indexTrangle + 1 + offset;
                indexArray[trangCount + 5] = indexTrangle + resolution + 1 + offset;


                trangCount += 6;
            }
        }
    }

    static {
        ComponentRegistry.registerComponent("PlanetMaker", PlanetMaker.class);
    }
    
}
