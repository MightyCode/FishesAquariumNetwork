package ProjetPoisson.mightylib.graphics.renderer._3D;

import ProjetPoisson.mightylib.graphics.renderer.Renderer;
import ProjetPoisson.mightylib.util.math.Color4f;

public class ModelRenderer extends Renderer {
    public ModelRenderer(String shaderName, String modelPath, String texture) {
        super(shaderName, true, false);
        shape = OBJLoader.loadObjTexturedModel(modelPath);
        switchToTextureMode(texture);
    }

    public ModelRenderer(String shaderName, String modelPath, Color4f color) {
        super(shaderName, true, false);
        shape = OBJLoader.loadObjColoredModel(modelPath);
        switchToColorMode(color);
    }
}
