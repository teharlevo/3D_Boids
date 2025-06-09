package rendering.renderers;


import java.util.List;

import main.Assets;
import main.Game;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

import rendering.camera.Camera;
import rendering.ShaderProgram;
import rendering.Sprite;
import rendering.opengl_objects.Vao;

public class SpriteRenderer extends Renderer<Sprite>{

    public SpriteRenderer(String vertexShader,String fragmentShader,Vao vao){
        ShaderProgram SP = new ShaderProgram(Assets.getVertexShaderSrc(vertexShader)
        ,Assets.getFragmentShaderSrc(fragmentShader));
        this.setSP(SP);
        this.setVao(vao);
    }

    public SpriteRenderer(String vertexShader,String fragmentShader){
        this(vertexShader,fragmentShader, new Vao(new int[]{3,3,2}));//שלוש מיקום שלוש נורמל שתיים uv
        System.out.println("f");
    }
    
    public void draw(String camName,String ObjectGroup) {
        System.out.println("k");
        Camera cam = Game.getCurrentScene().getCamera(camName);
        List<Sprite> sprites = Game.getCurrentScene().getSprites(ObjectGroup);

        ShaderProgram sp = this.getSP();
        Vao vao = this.getVao();

        sp.use();
        cam.bind(sp);

        for (int i = 0; i < sprites.size(); i++) {
            Sprite m = sprites.get(i);
            m.bind(sp);
            vao.bind();

            glDrawElements(GL_TRIANGLES, 9, GL_UNSIGNED_INT, 0);

        }
        sp.detach();
    }
}

