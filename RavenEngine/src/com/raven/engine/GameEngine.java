package com.raven.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.io.File;
import java.util.*;

import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.*;
import com.raven.engine.graphics3d.model.ModelData;
import com.raven.engine.graphics3d.model.ModelReference;
import com.raven.engine.graphics3d.model.PlyModelData;
import com.raven.engine.graphics3d.model.RavModelData;
import com.raven.engine.input.Keyboard;
import com.raven.engine.input.Mouse;
import com.raven.engine.scene.Camera;
import com.raven.engine.worldobject.GameObject;

public class GameEngine<G extends Game> implements Runnable {
    private static GameEngine engine;

    public static <G extends Game> GameEngine Launch(G game) {
        GameEngine<G> engine = new GameEngine<>(game);

        GameEngine.engine = engine;

        engine.window = new GameWindow(engine);

        engine.thread = new Thread(engine);
        engine.thread.start();

        return engine;
    }

    public static GameEngine getEngine() {
        return engine;
    }

    private G game;
    private Thread thread;
    private GameWindow window;
    private List<GameObject> oldMouseList = new ArrayList<>();
    private GameDatabase gdb;
    private Map<String, ModelData> modelDataMap = new HashMap<>();
    private float deltaTime;
    private long systemTime;
    private Mouse mouse = new Mouse();
    private Keyboard keyboard = new Keyboard();

    // Accessors
    public Thread getThread() {
        return thread;
    }

    public G getGame() {
        return game;
    }

    private GameEngine(G game) {
        this.game = game;

        game.setEngine(this);

        systemTime = System.nanoTime() / 1000000L;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public GameWindow getWindow() {
        return window;
    }

    public GameDatabase getGameDatabase() {
        return gdb;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void breakThread() {
        System.out
                .println("Breaking Thread. Was it alive? " + thread.isAlive());

        game.breakdown();
    }

    int frame = 0;
    float framesdt = 0;

    @Override
    public void run() {
        System.out.println("Started Run");

        System.out.println("Starting OpenGL");
        window.create();
        window.printErrors("Create Error: ");

        System.out.println("Loading Assets");
        loadDatabase();

        game.setup();

        game.transitionScene(game.loadInitialScene());

        ModelReference.compileBuffer();
        window.printErrors("Compile Buffer Error: ");

        while (game.isRunning()
                && !glfwWindowShouldClose(window.getWindowHandler())) {

            long start = System.nanoTime();


            if (GameProperties.supportsOpenGL4()) {
                input(deltaTime);
                window.printErrors("Input MS Error: ");

                if (GameProperties.getMultisampleCount() != 0) {
                    draw4fw();
                    window.printErrors("Draw MSAA Error: ");
                } else {
                    draw4();
                    window.printErrors("Draw FXAA Error: ");
                }
            } else {
                input(deltaTime);
                draw2();
                window.printErrors("Draw Basic Error: ");
            }

            game.update(deltaTime);
            window.printErrors("Update Error: ");

//            if (frame % 60 == 0) {
//                System.out.println("FPS: " + 1000f / (framesdt / 60f) + " MPF: " + framesdt / 60f);
//                framesdt = 0;
//            }

            glfwSwapBuffers(window.getWindowHandler()); // swap the color buffers

            window.printErrors("Swap Error: ");

            long currentTime = System.nanoTime();
            deltaTime = (currentTime - start) / 1000000.0f;
            systemTime = currentTime / 1000000L;

            framesdt += deltaTime;
            frame++;

            window.printErrors("Errors: ");
        }

        System.out.println("End Run");

        game.breakdown();

        window.destroy();

        System.out.println("Exit");

        System.exit(0);
    }

    private void draw4fw() {
        game.renderWorld4fw(window);

        window.getIDShader().useProgram();
        window.drawQuad();
    }

    private void draw4() {
        game.renderWorld4(window);

//        window.getIDShader().useProgram();
//        window.drawQuad();
    }

    private void draw2() {
        game.renderWorld2(window);

        // render world as input
    }

    private List<GameObject> newList = new ArrayList();
    private void input(float delta) {
        glfwPollEvents();

        int id = 0;

        if (GameProperties.getMultisampleCount() == 0) {
            id = window.getWorldShader().getWorldObjectID();
        } else {
            id = window.getIDShader().getWorldObjectID();
        }

        if (id != 0) {
            GameObject hover = GameObject.getGameObjectFromID(id);

            newList.clear();
            newList.addAll(hover.getParentGameObjectList());
            newList.add(hover);

            // clicks - might cause a problem with the order of enter and leave
            if (mouse.isLeftButtonClick()) {
                for (GameObject o : oldMouseList) {
                    if (newList.contains(o))
                        o.onMouseClick();
                }
                
                mouse.setLeftButtonClick(false);
            }

            // hover
            for (GameObject o : oldMouseList) {
                if (newList.contains(o))
                    o.checkMouseMovement(true, delta);
                else {
                    o.checkMouseMovement(false, delta);
                }
            }

            for (GameObject o : newList) {
                if (!oldMouseList.contains(o)) {
                    o.checkMouseMovement(true, delta);
                }
            }

            oldMouseList.clear();
            oldMouseList.addAll(newList);
        } else {
            for (GameObject o : oldMouseList) {
                o.checkMouseMovement(false, delta);
            }

            oldMouseList.clear();
        }
    }

    private void loadDatabase() {
        // load models

        File modelDirectory = new File(game.getMainDirectory() + File.separator + "models");
        loadModels(modelDirectory);

        // load database
        gdb = new GameDatabase();
        gdb.load();
    }

    private void loadModels(File base) {
        for (File f : base.listFiles()) {
            if (f.isFile()) {
                System.out.println(f.getPath());

                String[] parts = f.getPath().split("\\.");
                String ext = parts[parts.length - 1];

                switch (ext) {
                    case "ply":
                        modelDataMap.put(f.getPath(), new PlyModelData(f.getPath()));
                        break;
                    case "rav":
                        modelDataMap.put(f.getPath(), new RavModelData(f.getPath()));
                        break;
                }

            } else if (f.isDirectory()) {
                loadModels(f);
            }
        }
    }

    public ModelData getModelData(String modelsrc) {
        return modelDataMap.get(game.getMainDirectory() + File.separator + modelsrc);
    }

    // input
    public void inputMouseButton(int button, int action, int mods) {
        mouse.buttonAction(button, action);
    }

    public void inputMouseMove(double xpos, double ypos) {
        Camera camera = this.getGame().getCurrentScene().getCamera();

        if (mouse.isMiddleButtonDown() && camera.isInteractable()) {
            camera.rotate(xpos - mouse.getX(), ypos - mouse.getY());
        }

        if (mouse.isRightButtonDown() && camera.isInteractable()) {
            camera.move(xpos - mouse.getX(), ypos - mouse.getY());
        }

        mouse.setPos(xpos, ypos);
    }

    public void inputKey(int key, int action, int mods) {
        game.getCurrentScene().inputKey(key, action, mods);
    }

    public void inputScroll(double xoffset, double yoffset) {
        Camera camera = this.getGame().getCurrentScene().getCamera();

        if (camera.isInteractable())
            camera.zoom(yoffset);
    }
}
