package rendering;

import static org.lwjgl.opengl.GL43.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class ShaderProgram {
    
    private int ID;

    private boolean isUse = false;
    

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
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }
    
    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(ID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadfloat(String varName, float f) {
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform1f(varLocation, f);
    }

    public void uploadInt(String varName, int i) {
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform1i(varLocation, i);
    }

    public void uploadIntArray(String varName, int[] array) {
        int varLocation = glGetUniformLocation(ID, varName);
        glUniform1iv(varLocation, array);
    }
}