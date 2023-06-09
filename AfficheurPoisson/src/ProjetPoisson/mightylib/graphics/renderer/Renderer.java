package ProjetPoisson.mightylib.graphics.renderer;

import ProjetPoisson.mightylib.resources.texture.Texture;
import ProjetPoisson.mightylib.graphics.shader.ShaderManager;
import ProjetPoisson.mightylib.resources.Resources;
import ProjetPoisson.mightylib.util.math.Color4f;
import ProjetPoisson.mightylib.util.math.ColorList;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class Renderer{
    public static final int NOTHING = 0;
    public static final int COLOR = 1;
    public static final int TEXTURE = 2;

    protected final Vector3f position;
    protected final Vector3f scale;
    protected final Vector3f rotation;
    protected float angle;

    protected boolean shouldGlUniformModel;
    protected Matrix4f model;
    protected boolean display;
    protected FloatBuffer modelBuffer;
    protected Shape shape;
    protected ShaderManager shadManager;

    protected int displayMode;

    // Textured
    protected Texture texture;

    // Colored
    public Color4f color;

    public Renderer(String shaderName, boolean useEbo, boolean in2D){
        shadManager = ShaderManager.getInstance();
        shape = new Shape(shaderName, useEbo, in2D);
        model = new Matrix4f().identity();

        display = true;

        // Display mode
        texture = null;
        displayMode = NOTHING;
        color = ColorList.BLACK;

        position = new Vector3f();
        scale = new Vector3f(1f);
        rotation = new Vector3f();

        modelBuffer = BufferUtils.createFloatBuffer(16);

        shouldGlUniformModel = ShaderManager.getInstance().getShader(shape.getShaderId()).getLink("model") != -1;

        applyModel();
    }


    public void display(){
        if(display){
            updateShader();
            draw();
        }
    }


    public void updateShader(){
        // Apply model matrix
        if (shouldGlUniformModel){
            shadManager.getShader(shape.getShaderId()).glUniform("model", modelBuffer);
        }

        if (displayMode == COLOR) {
            shadManager.getShader(
                    shape.getShaderId()).glUniform("color", color.getR(), color.getG(), color.getB(), color.getA()
            );
        } else if (displayMode == TEXTURE){
            texture.bind(0);
        }
    }


    public void draw(){
        shape.display();
    }


    public void setPosition(Vector3f position){
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;

        applyModel();
    }

    public void setX(float x){
        this.position.x = x;

        applyModel();
    }

    public void setY(float y){
        this.position.y = y;

        applyModel();
    }

    public void setZ(float z){
        this.position.z = z;

        applyModel();
    }

    public void setScale(Vector3f scale){
        this.scale.x = scale.x;
        this.scale.y = scale.y;
        this.scale.z = scale.z;

        applyModel();
    }

    public void setRotation(float angle, Vector3f rotation){
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
        this.rotation.z = rotation.z;

        this.angle = angle;

        applyModel();
    }


    public void applyModel(){
        this.model.identity();

        this.model.translate(this.position);
        this.model.rotate(angle, this.rotation);
        this.model.scale(this.scale);

        this.model.get(modelBuffer);
    }

    public void hide(boolean state) {
        display = state;
    }


    public void invertDisplayState() {
        display = !display;
    }


    public void switchToTextureMode(String name) {
        switchToTextureMode(Resources.getInstance().getResource(Texture.class, name));
    }

    public void switchToTextureMode(Texture texture){
        displayMode = TEXTURE;
        this.texture = texture;
        shape.enableVbo(1);
    }


    public void switchToColorMode(Color4f color){
        displayMode = COLOR;
        this.color = color.copy();
        shape.disableVbo(1);
    }


    public Shape getShape(){
        return shape;
    }

    public void setShape(Shape shape){
        this.shape = shape;
    }

    public Vector3f position(){
        return position;
    }

    public Vector3f scale() { return scale; }

    public Vector3f rotationCoef() { return rotation; }

    public float getRotationAngle() { return angle; }

    public void unload(){
        shape.unload();
        modelBuffer.clear();
    }
}
