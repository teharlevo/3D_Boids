package rendering.opengl_objects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Vao {
    
    private int ID;
    private int[] vaoStractereArray;
    private int vertexSizeBytes;

    public Vao(int[] vaoStractereArray){
        this.vaoStractereArray = vaoStractereArray;
        ID = glGenVertexArrays();
        glBindVertexArray(ID);

        vertexSizeBytes = 0;
        for (int i = 0; i < vaoStractereArray.length; i++) {
            vertexSizeBytes += vaoStractereArray[i] * 4;
        }

        int pointer = 0;
        for (int i = 0; i < vaoStractereArray.length; i++) {
            glVertexAttribPointer(i, vaoStractereArray[i],GL_FLOAT, false, vertexSizeBytes, pointer);
            glEnableVertexAttribArray(i);
            pointer += vaoStractereArray[i] * 4;
        }
    }

    public int getID() {
        return ID;
    }

    public void bind(){
        glBindVertexArray(ID);
        int pointer = 0;
        for (int i = 0; i < vaoStractereArray.length; i++) {
            glVertexAttribPointer(i, vaoStractereArray[i],GL_FLOAT, false, vertexSizeBytes, pointer);
            glEnableVertexAttribArray(i);
            pointer += vaoStractereArray[i] * 4;
        }

    }

    public void unbind(){
        for (int i = 0; i < vaoStractereArray.length; i++) {
            glDisableVertexAttribArray(i);
        }
        glBindVertexArray(0);
    }

    public void delete(){
        glDeleteVertexArrays(ID);
    }
}
