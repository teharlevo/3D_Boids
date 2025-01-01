package rendering;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL43.*;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import static org.lwjgl.stb.STBImage.*;

import org.lwjgl.BufferUtils;


public class Texture {
    private int texID;
    private int width = 0;
    private int height = 0;

    private int channels = 3;

    public Texture(String path){

        texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_R,GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,GL_NEAREST);

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer imge = stbi_load(path, w, h,c,0);
        
        
        if(imge != null){
            if (c.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(0), h.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, imge);
            } else if (c.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(0), h.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, imge);
            } else {
                assert false : "Error: (Texture) Unknown number of channesl '" + c.get(0) + "'";
            }
        
        }else{
            System.out.println("dont work imge " + path);
        }

        stbi_image_free(imge);

        width  = w.get(0);
        height = h.get(0);
        channels = c.get(0);
        unbind();
    }

    public Texture(int width, int height, int internalFormat, int format) {
        texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_FLOAT, (ByteBuffer) null);

        this.width  = width;
        this.height = height;
        channels = 1;
        unbind();
    }

    public Texture(int width, int height) {
        texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        this.width = width;
        this.height = height;

        unbind();
        
    }

    public void bind(int place){
        glActiveTexture(GL_TEXTURE0 + place);
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getID(){
        return texID;
    }

    public int width(){
        return width;
    }

    public int height(){
        return height;
    }


    public ByteBuffer getTextureData() {
        int bufferSize = width * height * channels;

        // Create a ByteBuffer to hold the texture data
        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferSize);

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texID);

        // Get the texture data
        if(channels == 3){
            glGetTexImage(GL_TEXTURE_2D, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
        }
        else{
            glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        }

        // Unbind the texture
        glBindTexture(GL_TEXTURE_2D, 0);

        return buffer;
    }
    
    public void saveImage(String name,String formet) {// איטי רצח יש לשפר

        //יצרת באיטבאפר מהאקסטאראידי בלבד

        ByteBuffer buffer = getTextureData();
        //לקיחת באיטבאפרולשמור אותו לקובץ
        File file = new File(name + "." + formet); // The file to save to.
        BufferedImage image;
        if(channels == 3){
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        else{
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        
        byte[] data = new byte[width * height * channels];
        buffer.get(data);
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                int i = (x + (width * y)) * channels;
                int r = data[i] & 0xFF;
                int g = data[i + 1] & 0xFF;
                int b = data[i + 2] & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
           
        try {
            ImageIO.write(image, formet, file);
        } catch (IOException e) { e.printStackTrace(); }
    }


    public int getTexID() {
        return texID;
    }


    public void setTexID(int texID) {
        this.texID = texID;
    }


    public int getWidth() {
        return width;
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }


    public void setHeight(int height) {
        this.height = height;
    }

    public int getChannels() {
        return channels;
    }


    public void setChannels(int channels) {
        this.channels = channels;
    }
}