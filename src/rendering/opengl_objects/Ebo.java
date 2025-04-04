package rendering.opengl_objects;
import static org.lwjgl.opengl.GL15.*;

public class Ebo {
    private int ID;

    public Ebo(int[] indexArray) {
        
        ID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexArray, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); 
    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ID);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int getID() {
        return ID;
    }

    public void updateData(int[] newIndexArray) {
        bind();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, newIndexArray, GL_STATIC_DRAW);
        unbind();
    }

    public void delete() {
        glDeleteBuffers(ID);
    }
}
