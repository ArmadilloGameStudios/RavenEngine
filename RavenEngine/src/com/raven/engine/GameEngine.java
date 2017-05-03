package com.raven.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.GameWindow3D;
import com.raven.engine.graphics3d.ModelFrames;
import com.raven.engine.graphics3d.ModelReference;
import com.raven.engine.input.Input;
import com.raven.engine.input.MouseMovementInput;
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
	private Map<String, ModelFrames> modelAssets = new ConcurrentHashMap<String, ModelFrames>();
	// private Map<String, AnimatedGraphic> animatedAssets = new
	// ConcurrentHashMap<String, AnimatedGraphic>();
	// private Map<String, Asset> assets = new ConcurrentHashMap<String,
	// Asset>();
	private GameDatabase gdb;
	private List<Input> inputs = new ArrayList<Input>();
	private Input mouseMovementInput = null;
	private float deltaTime;
	private long systemTime;
	private boolean breakthread = false;

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
	
	public ModelFrames getModelReferenceAsset(String name) {
		ModelFrames g = modelAssets.get("TacticianMercSquad" + File.separator + name.replace('\\', File.separatorChar));

		if (g == null) {
			// g = ModelReference.getBlankModel();
			System.out.println("Error Referencing " + name);
		}

		return g;
	}

	public GameDatabase getGameDatabase() {
		return gdb;
	}

	public GameData getFirstFromGameDatabase(String table, GameDataQuery query) {
		return gdb.getTable(table).getFirst(query);
	}

	public List<GameData> getAllFromGameDatabase(String table,
			GameDataQuery query) {
		return gdb.getTable(table).getAll(query);
	}

	public GameData getRandomFromGameDatabase(String table, GameDataQuery query) {
		List<GameData> rows = gdb.getTable(table).getAll(query);

		Random r = new Random();
		return rows.get(r.nextInt(rows.size()));
	}

	public void breakThread() {
		System.out
				.println("Breaking Thread. Was it alive? " + thread.isAlive());
		breakthread = true;
	}

	@Override
	public void run() {
		System.out.println("Started Run");

		System.out.println("Starting OpenGL");
		window.setup();

		System.out.println("Loading Assets");
		loadDatabaseAndModels();

		game.setup();

		game.loadInitialScene();

		ModelReference.compileBuffer();

		// game.getCurrentScene().enterScene();

		while (game.isRunning()
				|| !glfwWindowShouldClose(window.getWindowHandler())) {
			long start = System.nanoTime();

			game.update(deltaTime);

			// drawing
			window.setProgramMain();
			window.setRenderTargetFBO(true);
			game.draw3d();

			window.setProgramBloomHorizontal();
			window.setRenderTargetFBOHOR(true);
			window.drawFBO();

			window.setProgramDrawFBO();
		    window.setRenderTargetWindow(true);
			window.drawFBO();

			// mouse
			int id = window.getWorldObjectID();
			if (id != 0) {
				WorldObject clicked = WorldObject.getWorldObjectFromID(id);
				clicked.onMouseEnter();
			}

//			window.setProgramMain();
//			// window.setRenderTargetFBO();
//			window.setRenderTargetWindow();
//			game.draw3d();

			glfwSwapBuffers(window.getWindowHandler()); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();

			if (Thread.interrupted() || breakthread) {
				System.out.println("Interrupted");
				break;
			}

			long len = System.nanoTime() - start;
			long sleep = 1000000000L / 60 - len;


			if (len == 0) {
				// System.out.println(0);
			} else {
//				System.out.println("FPS: " + 1000000000L / len + " Run: " + len
//						+ ", Sleep: " + sleep);
			}

			if (sleep > 0L) {
				try {
					// System.out.println("Sleep for: " + sleep / 1000000L +
					// "ms " + (int) (sleep % 1000000L) + "ns.");
					Thread.sleep(sleep / 1000000L, (int) (sleep % 1000000L));
				} catch (InterruptedException e) {
				}
			}

			// deltaTime = (System.nanoTime() - start) / 1000000000.0F;
			deltaTime = (System.nanoTime() - start) / 1000000.0F;
			systemTime = System.nanoTime() / 1000000L;
			// System.out.println("systemTime: " + systemTime);
		}

		System.out.println("End Run");

		game.breakdown();

		System.out.println("Exit");

		System.exit(0);
	}

	private void loadDatabaseAndModels() {
		// searchAndLoadAssets(new File("assets"));

		File modelDirectory = new File("TacticianMercSquad" + File.separator + "model");
		loadModels(modelDirectory);

		for (String img : modelAssets.keySet()) {
			System.out.println(img);
		}

		// File animDirectory = new File("anim");
		// loadAnimations(animDirectory);

		gdb = new GameDatabase();
		gdb.load();
	}

	private void loadModels(File base) {
		for (File f : base.listFiles()) {
			if (f.isFile()) {
				System.out.println(f.getPath());
				modelAssets.put(f.getPath(), ModelReference.load(f));
			} else if (f.isDirectory()) {
				loadModels(f);
			}
		}
	}
}
