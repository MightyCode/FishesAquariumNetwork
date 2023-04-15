package ProjetPoisson.project.scenes;

import ProjetPoisson.mightylib.graphics.renderer._2D.shape.RectangleRenderer;
import ProjetPoisson.mightylib.graphics.shader.ShaderManager;
import ProjetPoisson.mightylib.graphics.text.ETextAlignment;
import ProjetPoisson.mightylib.graphics.text.Text;
import ProjetPoisson.mightylib.inputs.KeyboardManager;
import ProjetPoisson.mightylib.physics.tweenings.ETweeningBehaviour;
import ProjetPoisson.mightylib.physics.tweenings.ETweeningOption;
import ProjetPoisson.mightylib.physics.tweenings.ETweeningType;
import ProjetPoisson.mightylib.physics.tweenings.type.FloatTweening;
import ProjetPoisson.mightylib.resources.Resources;
import ProjetPoisson.mightylib.resources.texture.BasicBindableObject;
import ProjetPoisson.mightylib.resources.texture.Icon;
import ProjetPoisson.mightylib.resources.texture.Texture;
import ProjetPoisson.mightylib.resources.texture.TextureParameters;
import ProjetPoisson.mightylib.scene.Scene;
import ProjetPoisson.mightylib.util.math.Color4f;
import ProjetPoisson.mightylib.util.math.EDirection;
import ProjetPoisson.project.client.Configuration;
import ProjetPoisson.project.command.CommandAnalyser;
import ProjetPoisson.project.command.Terminal;  
import ProjetPoisson.project.display.FishManager;
import ProjetPoisson.project.threads.ServerThread;
import ProjetPoisson.project.threads.ClientThread;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;


import ProjetPoisson.mightylib.util.math.MightyMath;

public class MenuScene extends Scene {
    private Text text;

    private RectangleRenderer renderer;

    private Terminal terminal;

    private CommandAnalyser analyser;

    private FishManager fishManager;
    private Texture displacementMap;
    private FloatTweening displacementMapTweening;

    public void init(String[] args) {
        super.init(args, new BasicBindableObject().setQualityTexture(TextureParameters.REALISTIC_PARAMETERS));

        if (Resources.getInstance().isExistingResource(Icon.class, "Kraken"))
            mainContext.getWindow().setIcon(Resources.getInstance().getResource(Icon.class, "Kraken"));

        /// SCENE INFORMATION ///
        ServerThread serverThread = new ServerThread();
        serverThread.start();
        ClientThread clientThread = new ClientThread();
        clientThread.start();

        main3DCamera.setPos(new Vector3f(0, 0, 0));
        setClearColor(52, 189, 235, 1f);

        /// RENDERERS ///

        Vector2i windowSize = mainContext.getWindow().getInfo().getSizeCopy();

        renderer = new RectangleRenderer("texture2DDisplacement");
        renderer.switchToTextureMode("background");
        renderer.setSizePix(windowSize.x, windowSize.y);

        text = new Text();
        text.setText("Aquarium poisson")
                .setFont("bahnschrift")
                .setAlignment(ETextAlignment.Center)
                .setReference(EDirection.None)
                .setPosition(new Vector2f(windowSize.x * 0.5f, windowSize.y * 0.2f))
                .setColor(new Color4f(1, 1, 1, 1))
                .setFontSize(40);

        terminal = new Terminal(new Vector2f(0,windowSize.y), new Vector2f(windowSize.x * 0.5f,windowSize.y * 0.5f));
        Configuration conf = Resources.getInstance().getResource(Configuration.class, "affichage");
        Configuration configuration = Resources.getInstance().getResource(Configuration.class, "affichage");

        fishManager = new FishManager(mainContext.getWindow().getInfo(), configuration);
        int numberFish = 5;
        float size = MightyMath.mapLog(numberFish, 10, 100, 0.17f, 0.15f);

        for (int i = 0; i < numberFish; ++i)
            fishManager.addFish("Fish" + i, new Vector2f(0.5f, 0.5f), new Vector2f(size, size), "");

        displacementMap = Resources.getInstance().getResource(Texture.class, "displacementMap");
        ShaderManager.getInstance().getShader(renderer.getShape().getShaderId()).glUniform("displacementMap", 1);

        displacementMapTweening = new FloatTweening();
        displacementMapTweening.setTweeningValues(ETweeningType.Linear, ETweeningBehaviour.InOut)
                .setTweeningOption(ETweeningOption.LoopReversed)
                .initTwoValue(15f, 0f, 15f);


        analyser = new CommandAnalyser(fishManager);
    }

    public void update() {
        super.update();

        fishManager.update();

        KeyboardManager manager = mainContext.getKeyboardManager();

        for (char letter = 'a'; letter <= 'z'; ++letter){
            if (manager.keyPressed(GLFW.GLFW_KEY_A + (letter - 'A'))){
                System.out.println(letter);
            }
        }

        terminal.update(mainContext.getInputManager(), mainContext.getSystemInfo());

        if (terminal.shouldProcessCommand()){
            String result = analyser.analyseCommand(terminal.getCommandText());

            if (result != null) {
                if (result.equals("¤¤clear¤¤"))
                     terminal.clearResultText();
                else
                    terminal.addToResultText(result);

                terminal.saveCommand()
                        .clearCommandText();
            }
        }

        displacementMapTweening.update();

        ShaderManager.getInstance().getShader(renderer.getShape().getShaderId()).glUniform("time", displacementMapTweening.value());
    }


    public void display() {
        super.setVirtualScene();
        clear();

        displacementMap.bind(1);
        renderer.display();

        text.display();

        fishManager.display();

        terminal.display();

        super.setAndDisplayRealScene();
    }


    public void unload() {
        super.unload();

        renderer.unload();

        text.unload();
        terminal.unload();

        displacementMap.unload();

        fishManager.unload();
    }
}
