package com.raven.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.io.File;
import java.util.*;

import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.GameWindow3D;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.ModelReference;
import com.raven.engine.graphics3d.PlyModelData;
import com.raven.engine.input.Keyboard;
import com.raven.engine.input.Mouse;
import com.raven.engine.worldobject.WorldObject;

public class GameEngine implements Runnable {
	private static GameEngine engine;

	public static GameEngine Launch(Game game) {
		GameEngine engine = new GameEngine(game);

		GameEngine.engine = engine;

		engine.window = new GameWindow3D(engine);

		engine.thread = new Thread(engine);
		engine.thread.start();

		return engine;
	}

	public static GameEngine getEngine() {
		return engine;
	}

	private Game game;
	private Thread thread;
	private GameWindow3D window;
	private List<WorldObject> oldMouseList = new ArrayList<>();
	private GameDatabase gdb;
	private Map<String, ModelData> modelDataMap = new HashMap<>();
	private float deltaTime;
	private long systemTime;
	private boolean breakthread = false;
	private Mouse mouse = new Mouse();
	private Keyboard keyboard = new Keyboard();

	// Accessors
	public Thread getThread() {
		return thread;
	}

	public Game getGame() {
		return game;
	}

	private GameEngine(Game game) {
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

	public GameWindow3D getWindow() {
		return window;
	}

	public GameDatabase getGameDatabase() {
		return gdb;
	}

	public void breakThread() {
		System.out
				.println("Breaking Thread. Was it alive? " + thread.isAlive());
		breakthread = true;
	}

	int frame = 0;
	float framesdt = 0;
	@Override
	public void run() {
		System.out.println("Started Run");

		System.out.println("Starting OpenGL");
		window.setup();

		System.out.println("Loading Assets");
		loadDatabase();

		game.setup();

		game.transitionScene(game.loadInitialScene());

		ModelReference.compileBuffer();
		window.printErrors("Compile Buffer Error: ");

		// game.getCurrentScene().enterScene();

		while (game.isRunning()
				|| !glfwWindowShouldClose(window.getWindowHandler())) {

			long start = System.nanoTime();

			draw();
			input();
			game.update(deltaTime);

			if (frame % 60 == 0) {
                System.out.println("FPS: " + 1000f / (framesdt / 60f));
                framesdt = 0;
            }


			glfwSwapBuffers(window.getWindowHandler()); // swap the color buffers

            long currentTime = System.nanoTime();
			deltaTime = (currentTime - start) / 1000000.0f;
			systemTime = currentTime / 1000000L;

            framesdt += deltaTime;
            frame++;

            window.printErrors("Errors: ");
		}

		System.out.println("End Run");

		game.breakdown();

		System.out.println("Exit");

		System.exit(0);
	}

	private void draw() {
		game.draw3d();

		// window.getBloomShader().useProgram();
		// window.drawFBO();

		window.getCombinationShader().useProgram();
		window.getCombinationShader().setProjectionMatrix(game.getCurrentScene().getCamera().getProjectionMatrix());
		window.drawFBO();
    }

	private void input() {
        glfwPollEvents();

		int id = window.getWorldShader().getWorldObjectID();
		if (id != 0 && false) {
			System.out.println("id: " + id);

			WorldObject clicked = WorldObject.getWorldObjectFromID(id);

			List<WorldObject> newList = clicked.getParentWorldObjectList();
			newList.add(clicked);
			for (WorldObject o : oldMouseList) {
				if (newList.contains(o))
					o.checkMouseMovement(true);
				else {
					o.checkMouseMovement(false);
				}
			}

			oldMouseList = newList;
		} else {
			for (WorldObject o : oldMouseList) {
				o.checkMouseMovement(false);
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
				modelDataMap.put(f.getPath(), new PlyModelData(f.getPath()));
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
        if (mouse.isMiddleButtonDown())
            this.getGame().getCurrentScene().getCamera().rotate(xpos - mouse.getX(), ypos - mouse.getY());

        if (mouse.isRightButtonDown()) {
            this.getGame().getCurrentScene().getCamera().move(xpos - mouse.getX(), ypos - mouse.getY());
        }

        mouse.setPos(xpos, ypos);
    }

    public void inputKey(int key, int action, int mods) {

    }

    public void inputScroll(double xoffset, double yoffset) {
        this.getGame().getCurrentScene().getCamera().zoom(yoffset);

    }
}
