package rendering.renderers;

import rendering.ShaderProgram;
import rendering.opengl_objects.Vao;

public abstract class Renderer<T>{
    public final static int vertexSize = 8;//שלוש מיקום שלוש נורמל שתיים uv

    private ShaderProgram SP;
    private Vao vao;

    public abstract void draw(String camName,String ObjectGroup);

    public ShaderProgram getSP() {
        return SP;
    }
    public void setSP(ShaderProgram SP) {
        this.SP = SP;
    }

    public Vao getVao() {
        return vao;
    }

    public void setVao(Vao vao) {
        this.vao = vao;
    }
}