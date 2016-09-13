package com.crookedbird.engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.crookedbird.engine.database.GameData;
import com.crookedbird.engine.database.GameDataQuery;
import com.crookedbird.engine.database.GameDatabase;
import com.crookedbird.engine.forms.GameWindow;
import com.crookedbird.engine.graphics.ImageGraphic;
import com.crookedbird.engine.graphics.ImageReference;
import com.crookedbird.engine.input.Input;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.input.MouseMovementInput;

public class GameEngine implements Runnable, MouseListener, MouseMotionListener {
	private static GameEngine engine;

	public static GameEngine Launch(Game game) {
		GameEngine engine = new GameEngine(game);

		GameEngine.engine = engine;

		engine.window = new GameWindow(engine);

		engine.thread = new Thread(engine);
		engine.thread.start();

		return engine;
	}

	public static GameEngine getEngine() {
		return engine;
	}

	private Game game;
	private Thread thread;
	private GameWindow window;
	private Map<String, ImageReference> imageAssets = new ConcurrentHashMap<String, ImageReference>();
	// private Map<String, AnimatedGraphic> animatedAssets = new ConcurrentHashMap<String, AnimatedGraphic>();
	// private Map<String, Asset> assets = new ConcurrentHashMap<String, Asset>();
	private GameDatabase gdb;
	private List<Input> inputs = new ArrayList<Input>();
	private Input mouseMovementInput = null;
	private double deltaTime;
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
	}

	public double getDeltaTime() {
		return deltaTime;
	}

	public ImageReference getImageReferenceAsset(String name) {
		ImageReference g = imageAssets.get(name.replace('\\', File.separatorChar));

		if (g == null) {
			g = new ImageGraphic(ImageReference.genErrorImage());
			System.out.println("Error Referencing " + name);
		}

		return g;
	}

	/*public AnimatedGraphic getAnimationReferenceAsset(String name) {
		AnimatedGraphic a = animatedAssets.get(name);

		if (a == null) {
			a = new AnimatedGraphic(ImageReference.genErrorImage());
		}

		return a;
	}*/

	public GameDatabase getGameDatabase() {
		return gdb;
	}

	public GameData getFirstFromGameDatabase(String table, GameDataQuery query) {
		return gdb.getTable(table).getFirst(query);
	}

	public List<GameData> getAllFromGameDatabase(String table, GameDataQuery query) {
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
		System.out.println("Loading Assets");

		loadAssets();

		game.setup();

		game.loadInitialScene();

		// game.getCurrentScene().enterScene();

		while (game.isRunning()) {
			long start = System.nanoTime();

			synchronized (inputs) {
				if (mouseMovementInput != null) {
					inputs.add(mouseMovementInput);
				}

				for (Input i : inputs) {
					i.read(game);
				}

				inputs.clear();
			}

			game.update(deltaTime);

			window.draw(game.draw());

			if (Thread.interrupted() || breakthread) {
				System.out.println("Interrupted");
				break;
			}

			long len = System.nanoTime() - start;
			long sleep = 1000000000L / 60 - len;

			if (len == 0) {
				// System.out.println(0);
			} else {
				// System.out.println("FPS: " + 1000000000L / len + " Run: " +
				// len + ", Sleep: " + sleep);
			}

			if (sleep > 0L) {
				try {
					// System.out.println("Sleep for: " + sleep / 1000000L +
					// "ms " + (int) (sleep % 1000000L) + "ns.");
					Thread.sleep(sleep / 1000000L, (int) (sleep % 1000000L));
				} catch (InterruptedException e) {
				}
			}

			deltaTime = (double) (System.nanoTime() - start) / 1000000000.0;
			// System.out.println("DeltaTime: " + deltaTime);
		}

		System.out.println("End Run");

		game.breakdown();

		System.out.println("Exit");

		System.exit(0);
	}

	private void loadAssets() {
		// searchAndLoadAssets(new File("assets"));

		File imgDirectory = new File("img");
		loadImages(imgDirectory);
		
		
		for (String img :  imageAssets.keySet()) {
			System.out.println(img);
		}

		// File animDirectory = new File("anim");
		// loadAnimations(animDirectory);
		
		gdb = new GameDatabase();
		gdb.load();
	}

//	private void searchAndLoadAssets(File cDir) {
//		searchAndLoadAssets(cDir, "");
//	}

//	private void searchAndLoadAssets(File cDir, String path) {
//		List<File> files = Arrays.asList(cDir.listFiles());
//
//		for (File file : files) {
//			String name = path + file.getName();
//
//			if (file.isDirectory()) {
//				searchAndLoadAssets(file, name + "_");
//			} else {
//				assets.put(name, AssetReader.readAsset(file));
//			}
//		}
//	}

	private void loadImages(File base) {
		for (File f : base.listFiles()) {
			if (f.isFile()) {
				imageAssets.put(f.getPath(), new ImageGraphic(f));
			} else if (f.isDirectory()) {
				loadImages(f);
			}
		}
	}

	/*private void loadAnimations(File base) {
		for (File f : base.listFiles()) {
			if (f.isFile()) {
				animatedAssets.put(f.getPath(), new AnimatedGraphic(f));
			} else if (f.isDirectory()) {
				loadImages(f);
			}
		}
	}*/

	@Override
	public void mouseClicked(MouseEvent e) {
		synchronized (inputs) {
			inputs.add(new MouseClickInput(e));
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		synchronized (inputs) {
			mouseMovementInput = null;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		synchronized (inputs) {
			mouseMovementInput = new MouseMovementInput(e);
		}
	}
}
