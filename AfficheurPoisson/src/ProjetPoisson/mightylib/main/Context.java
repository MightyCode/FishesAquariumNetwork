package ProjetPoisson.mightylib.main;

import ProjetPoisson.mightylib.inputs.InputManager;
import ProjetPoisson.mightylib.inputs.KeyboardManager;
import ProjetPoisson.mightylib.inputs.MouseManager;
import ProjetPoisson.mightylib.scene.Camera3D;
import ProjetPoisson.mightylib.scene.Camera3DCreationInfo;
import org.joml.Vector3f;

public class Context {
    private final Window window;
    private final InputManager inputManager;
    private final KeyboardManager keyboardManager;
    private final MouseManager mouseManager;

    private final SystemInfo systemInfo;


    Context(Window window,
            InputManager inputManager,
            KeyboardManager keyboardManager,
            MouseManager mouseManager){

        this.window = window;
        this.inputManager = inputManager;
        this.keyboardManager = keyboardManager;
        this.mouseManager = mouseManager;

        this.systemInfo = new SystemInfo(this.window.getInfo());
    }

    public Window getWindow() { return window; }

    public InputManager getInputManager() { return inputManager; }
    public KeyboardManager getKeyboardManager() { return keyboardManager; }
    public MouseManager getMouseManager() { return mouseManager; }

    public SystemInfo getSystemInfo() { return systemInfo; }


    public Camera3D createCamera(Camera3DCreationInfo info){
        return new Camera3D(window.getInfo(), mouseManager, info.fov,info.initialPosition);
    }


    public void dispose(){
        inputManager.dispose();
    }


    public void unload(){
        window.destroyWindow();
    }
}
