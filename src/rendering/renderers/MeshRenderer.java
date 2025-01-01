package rendering.renderers;

import java.util.List;

import main.Assets;
import main.Game;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import rendering.camera.Camera;
import rendering.Model;
import rendering.ShaderProgram;
import rendering.opengl_objects.Vao;

public class MeshRenderer extends Renderer<Model>{

    public MeshRenderer(String vertexShader,String fragmentShader,Vao vao){
        ShaderProgram SP = new ShaderProgram(Assets.getVertexShaderSrc(vertexShader)
        ,Assets.getFragmentShaderSrc(fragmentShader));
        this.setSP(SP);
        this.setVao(vao);
    }

    public MeshRenderer(String vertexShader,String fragmentShader){
        this(vertexShader,fragmentShader, new Vao(new int[]{3,3,2}));//שלוש מיקום שלוש נורמל שתיים uv
    }
    
    public void draw(String camName,String ObjectGroup) {
        Camera cam = Game.getCurrentScene().getCamera(camName);
        List<Model> models = Game.getCurrentScene().getModels(ObjectGroup);

        ShaderProgram sp = this.getSP();
        Vao vao = this.getVao();

        sp.use();
        cam.bind(sp);

        for (int i = 0; i < models.size(); i++) {
            Model m = models.get(i);
            m.bind(sp);
            vao.bind();

            glDrawArrays(GL_TRIANGLES, 0,Assets.getMesh(m.mesh).getSize());
        }
        sp.detach();
    }
}
