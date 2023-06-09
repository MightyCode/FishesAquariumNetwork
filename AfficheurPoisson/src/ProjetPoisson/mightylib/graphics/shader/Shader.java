package ProjetPoisson.mightylib.graphics.shader;

import ProjetPoisson.mightylib.resources.FileMethods;
import ProjetPoisson.mightylib.util.ObjectId;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

public class Shader extends ObjectId{
    public static String PATH = "resources/shaders/";

    private final String fragmentSource, vertexSource;
    private int shaderProgram;

    private final HashMap<String, Integer> valuesLink;
    public final ArrayList<Integer> properties;

    public Shader(String vertexSource, String fragmentSource){
        // Base value
        shaderProgram = 0;

        this.fragmentSource = fragmentSource;
        this.vertexSource = vertexSource;
        valuesLink = new HashMap<>();
        properties = new ArrayList<>();
    }

    public void load(){
        // Creation du shader programme
        int vShader = glCreateShader(GL_VERTEX_SHADER);
        String bf = "error";
        try {
            bf = FileMethods.readFileAsString(PATH + vertexSource);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.print("Error path :" + PATH + vertexSource + " not found");
        }

        glShaderSource(vShader, bf);
        glCompileShader(vShader);
        if(!glGetShaderInfoLog(vShader).equals("")){
            System.err.println("Shader path " + PATH + vertexSource);
            System.err.println("Error from Vertex:\n" + glGetShaderInfoLog(vShader));
        }

        int fShader = glCreateShader(GL_FRAGMENT_SHADER);
        try {
            bf = FileMethods.readFileAsString(PATH + fragmentSource);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.print("Error path :" + PATH + fragmentSource + " not found");
        }

        glShaderSource(fShader,bf);
        glCompileShader(fShader);
        if(!glGetShaderInfoLog(fShader).equals("")){
            System.err.println("Shader path " + PATH + fragmentSource);
            System.err.println("Error from Fragment:\n" + glGetShaderInfoLog(fShader));
        }

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vShader);
        glAttachShader(shaderProgram,fShader);
        glLinkProgram(shaderProgram);
        if(!glGetShaderInfoLog(shaderProgram).equals("")) System.err.println("Error from Shader Program:\n" + glGetShaderInfoLog(shaderProgram));

        glDeleteShader(vShader);
        glDeleteShader(fShader);
    }

    public void use(){
        glUseProgram(shaderProgram);
    }

    public void addLink(String valueName){
        use();
        valuesLink.put(valueName, glGetUniformLocation(shaderProgram, valueName));
    }

    public int getLink(String valueName){
        return valuesLink.getOrDefault(valueName, -1);
    }

    public int getShaderId(){
        return shaderProgram;
    }

    public void unload(){
        glDeleteShader(shaderProgram);
    }

    public void glUniform(String valueName, float value1){
        this.use();
        glUniform1f(this.getLink(valueName), value1);
    }

    public void glUniform(String valueName, float value1, float value2){
        this.use();
        glUniform2f(getLink(valueName), value1, value2);
    }

    public void glUniform(String valueName, float value1, float value2, float value3){
        this.use();
        glUniform3f(getLink(valueName), value1, value2, value3);
    }

    public void glUniform(String valueName, float value1, float value2, float value3, float value4){
        this.use();
        glUniform4f(getLink(valueName), value1, value2, value3, value4);
    }

    public void glUniform(String valueName, float[] value1){
        this.use();
        glUniformMatrix4fv(getLink(valueName), false, value1);
    }

    public void glUniform(String valueName, FloatBuffer value1){
        this.use();
        glUniformMatrix4fv(getLink(valueName), false, value1);
    }

    public void glUniform(String valueName, int value1){
        this.use();
        glUniform1i(getLink(valueName), value1);
    }
}
