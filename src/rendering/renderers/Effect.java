package rendering.renderers;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import main.Assets;
import rendering.ShaderProgram;
import rendering.opengl_objects.Vao;

public class Effect {
    private Vao vao = new Vao(new int[]{3,3,2});
    private ShaderProgram SP;

    public Effect(String fragmentShader){
        SP = new ShaderProgram(Assets.getVertexShaderSrc("effect")
        ,Assets.getFragmentShaderSrc(fragmentShader));
    }

    public void draw(boolean clearColor,boolean clearDepth){
        if(clearDepth){glClear(GL_DEPTH_BUFFER_BIT);}
        if(clearColor){glClear(GL_COLOR_BUFFER_BIT);}

        SP.use();
        Assets.getMesh("box").getVbo().bind();
        vao.bind();
        glDrawArrays(GL_TRIANGLES, 0,6);  
        SP.detach();
    }

    public ShaderProgram getSP() {
        return SP;
    }

    public void setSP(ShaderProgram shader) {
        this.SP = shader;
    }
    
}
