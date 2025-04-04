package rendering;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform1iv;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class ShaderProgram {
    
    private int ID;

    private boolean isUse = false;

    private List<String> texturesNames = new ArrayList<>();
    

    public  ShaderProgram(String _vertexName,String _frgmntName){
        compile(_vertexName,_frgmntName);
    }

    public void compile(String vertexsrc,String frgmntsrc){
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        
        glShaderSource(vertexID, vertexsrc);
        glCompileShader(vertexID);

        // בודק בעיות
        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);

        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }
        

        int frgmntID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(frgmntID, frgmntsrc);
        glCompileShader(frgmntID);

        // בודק בעיות
        success = glGetShaderi(frgmntID,GL_COMPILE_STATUS);

        if(success == GL_FALSE){
            int len = glGetShaderi(frgmntID,GL_INFO_LOG_LENGTH);
            System.out.println("Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(frgmntID, len));
            assert false : "";
        }

        //לחבר בין שתי השידרים
        ID = glCreateProgram();
        glAttachShader(ID, vertexID);
        glAttachShader(ID, frgmntID);
        glLinkProgram(ID);
    
        success = glGetProgrami(ID, GL_LINK_STATUS);

        if(success == GL_FALSE){
            int len = glGetProgrami(ID,GL_INFO_LOG_LENGTH);
            System.out.println("Linking of shaders failed.");
            System.out.println(glGetProgramInfoLog(ID, len));
            assert false : "";
        }
    }

    public void use() {
        if(!isUse){
            glUseProgram(ID);
            isUse = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        isUse = false;
        texturesNames = new ArrayList<>();
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        use();
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        use();
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        use();
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }
    
    public void uploadMat4f(String varName, Matrix4f mat4) {
        use();
        int varLocation = glGetUniformLocation(ID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadfloat(String varName, float f) {
        use();
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform1f(varLocation, f);
    }

    public void uploadInt(String varName, int i) {
        use();
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform1i(varLocation, i);
    }

    public void uploadIntArray(String varName, int[] array) {
        use();
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform1iv(varLocation, array);
    }

    public void uploadTexture(String uniformName, Texture texture) {
        if(texturesNames.indexOf(uniformName) == -1){
            texturesNames.add(uniformName);
        }

        uploadInt(uniformName, texturesNames.indexOf(uniformName));

        glActiveTexture(GL_TEXTURE0 + texturesNames.indexOf(uniformName));
        glBindTexture(GL_TEXTURE_2D, texture.getID());
        
    }
}