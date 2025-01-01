package rendering;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import rendering.opengl_objects.Vbo;
import rendering.renderers.Renderer;

public class Mesh {
    private float[] vertices;

    private Vbo vbo;

    public Mesh(float[] MeshVertices) {
        vbo = new Vbo(MeshVertices);
        vertices = MeshVertices; 
    }

    public Mesh(String path) {

        AIScene scene = Assimp.aiImportFile(path, Assimp.aiProcess_Triangulate);
        loadMeshs(scene);
        vbo = new Vbo(vertices);
    }

    private void loadMeshs(AIScene scene) {
        
        PointerBuffer bufferMeshes = scene.mMeshes();
        int numMeshes = Math.min(scene.mNumMeshes(), bufferMeshes.remaining());
        int totalVertices = 0;
        

        for (int i = 0; i < numMeshes; i++) {
            AIMesh mesh = AIMesh.create(bufferMeshes.get(i));
            totalVertices += mesh.mNumVertices();
        }
    
        vertices = new float[totalVertices * Renderer.vertexSize];

        int currentVertexIndex = 0;

        for (int i = 0; i < numMeshes; i++) {
            AIMesh mesh = AIMesh.create(bufferMeshes.get(i));
            
            float[] meshVertices = processMeshVertices(mesh);

            mesh.close();

            System.arraycopy(meshVertices, 0, vertices, currentVertexIndex * Renderer.vertexSize, meshVertices.length);
            currentVertexIndex += meshVertices.length/Renderer.vertexSize;
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

    public Vbo getVbo() {
        return vbo;
    }

    public void bind(){
        vbo.bind();
    }

    public int getSize() {
        return vertices.length/Renderer.vertexSize;
    }
}