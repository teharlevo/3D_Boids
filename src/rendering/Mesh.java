package rendering;

import java.nio.IntBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import rendering.opengl_objects.Ebo;
import rendering.opengl_objects.Vbo;
import rendering.renderers.Renderer;

public class Mesh {
    private float[] vertices;
    private int[] indexArray;

    private Vbo vbo;
    private Ebo ebo;

    public Mesh(int[] meshIndexArray,float[] meshVertices) {
        vbo = new Vbo(meshVertices);
        vertices = meshVertices; 

        ebo = new Ebo(meshIndexArray);
        indexArray = meshIndexArray;
    }

    public Mesh(String path) {

        AIScene scene = Assimp.aiImportFile(path, Assimp.aiProcess_Triangulate);
        loadMeshs(scene);
        vbo = new Vbo(vertices);
        ebo = new Ebo(indexArray);
    }

    private void loadMeshs(AIScene scene) {
        
        PointerBuffer bufferMeshes = scene.mMeshes();
        int numMeshes = Math.min(scene.mNumMeshes(), bufferMeshes.remaining());
        int totalVertices = 0;
        int totalElements = 0;
        

        for (int i = 0; i < numMeshes; i++) {
            AIMesh mesh = AIMesh.create(bufferMeshes.get(i));
            totalVertices += mesh.mNumVertices();
            totalElements += mesh.mNumFaces();
        }
    
        vertices = new float[totalVertices * Renderer.vertexSize];
        indexArray = new int[totalElements * 3];

        int currentVertexIndex = 0;
        int currentIndexIndex = 0;

        for (int i = 0; i < numMeshes; i++) {
            AIMesh mesh = AIMesh.create(bufferMeshes.get(i));
            
            float[] meshVertices = processMeshVertices(mesh);


            int[] meshIndexArray = new int[mesh.mNumFaces() * 3] ;

            int faceCount = mesh.mNumFaces();
            AIFace.Buffer faces = mesh.mFaces();
            for (int j = 0; j < faceCount; j++) {
                AIFace face = faces.get(j);
                
                IntBuffer faceIndices = face.mIndices();

                meshIndexArray[j * 3] = faceIndices.get() + currentIndexIndex;
                meshIndexArray[j * 3 + 1] = faceIndices.get() + currentIndexIndex;
                meshIndexArray[j * 3 + 2] = faceIndices.get() + currentIndexIndex;
            }

            mesh.close();

            System.arraycopy(meshVertices, 0, vertices, currentVertexIndex * Renderer.vertexSize, meshVertices.length);
            System.arraycopy(meshIndexArray, 0, indexArray, currentIndexIndex, meshIndexArray.length);
            currentVertexIndex += meshVertices.length/Renderer.vertexSize;
            currentIndexIndex += meshIndexArray.length;
        }
    }

    private static float[] processMeshVertices(AIMesh Mesh){
        AIVector3D.Buffer vectors = Mesh.mVertices();
        AIVector3D.Buffer cordes = Mesh.mTextureCoords(0);
        AIVector3D.Buffer Normals = Mesh.mNormals();

        float[] vertices = new float[vectors.limit() * Renderer.vertexSize];


        for (int i = 0; i < vectors.limit(); i++) {
            int offset = i * Renderer.vertexSize;

            AIVector3D vector = vectors.get(i);
            AIVector3D corde  = cordes.get(i);
            AIVector3D normal  = Normals.get(i);

            vertices[offset]    = vector.x();
            vertices[offset+1]  = vector.y(); 
            vertices[offset+2]  = vector.z(); 
            vertices[offset+3]  = normal.x(); 
            vertices[offset+4]  = normal.y(); 
            vertices[offset+5]  = normal.z(); 
            vertices[offset+6]  = corde.x();   
            vertices[offset+7]  = corde.y();  
        }
        
        return vertices;
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndexArray() {
        return indexArray;
    }

    public Vbo getVbo() {
        return vbo;
    }

    public Ebo getEbo() {
        return ebo;
    }

    public void bind(){
        vbo.bind();
        ebo.bind();
    }

    public int getSize() {
        return indexArray.length;
    }
}