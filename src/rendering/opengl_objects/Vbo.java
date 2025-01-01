package rendering.opengl_objects;

import static org.lwjgl.opengl.GL15.*;

public class Vbo {
    private int ID;

    public Vbo(float[] vertexArray) {
        ID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, ID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, ID);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int getID() {
        return ID;
    }

    public void updateData(float[] newVertexArray) {
        bind();
        glBufferData(GL_ARRAY_BUFFER, newVertexArray, GL_STATIC_DRAW);
        unbind();
    }
}
