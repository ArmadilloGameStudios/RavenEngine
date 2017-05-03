package com.raven.engine.worldobject;

import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.graphics3d.AnimatedModel;
import com.raven.engine.input.MouseMovementInput;

public abstract class WorldObject implements Parentable {
	private static int last_id = 0;
	private static HashMap<Integer, WorldObject> worldObjectIDMap = new HashMap<Integer, WorldObject>();

	public static void resetObjectIDs() {
		worldObjectIDMap.clear();
		last_id = 0;
	}

	public static WorldObject getWorldObjectFromID(int id) {
		return worldObjectIDMap.get(id);
	}

	private int id;
	private double x, y, z;
	private int w, h, l;
	private boolean visible = true;
	private List<WorldObject> children = new CopyOnWriteArrayList<WorldObject>();
	private AnimatedModel animatedReference;
	private String animationstate = "idle";
	private boolean mousehovering = false;
	private List<MouseHandler> clickHandlers = new ArrayList<MouseHandler>();
	private long timeOffset = 0;

	private List<TextObject> textObjects = new ArrayList<TextObject>();

	private Parentable parent;

	public WorldObject(Parentable parent, GameData model) {
		this(parent, model, 0, 0, 0, 0);
	}

	public WorldObject(Parentable parent, GameData model, double x, double y) {
		this(parent, model, x, y, 0, 0);
	}

	public WorldObject(Parentable parent, GameData model, double x, double y, int w,
			int h) {
		this.parent = parent;

		// model
		if (model != null) {
			animatedReference = new AnimatedModel(model);
		}

		// click id
		id = ++last_id;
		worldObjectIDMap.put(id, this);

		// pos
		this.x = x;
		this.y = y;
		this.z = 0;

		// size
		this.w = (int)(w == 0 && animatedReference != null ? animatedReference
				.getWidth() : w);
		this.h = (int)(h == 0 && animatedReference != null ? animatedReference
				.getHeight() : h);
		this.l = (int)(l == 0 && animatedReference != null ? animatedReference
				.getLength() : l);
	}

	public String getAnimationState() {
		return animationstate;
	}

	public int setAnimationState(String animationstate) {
		this.animationstate = animationstate;
		timeOffset = GameEngine.getEngine().getSystemTime();
		
		return this.animatedReference.getFrameTime(animationstate);
	}
	
	public int getAnimationTime(String animationstate) {
		return this.animatedReference.getFrameTime(animationstate);
	}

	@Override
	public double getGlobalZ() {
		return this.getX() + parent.getGlobalX();
	}

	public double getZ() {
		return x;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	@Override
	public double getGlobalX() {
		return this.getX() + parent.getGlobalX();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getGlobalY() {
		return this.getY() + parent.getGlobalY();
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public int getLength() {
		return l;
	}

	public void setLength(int l) {
		this.l = l;
	}

	public boolean getVisibility() {
		return this.visible;
	}

	public void setVisibility(boolean visible) {
		this.visible = visible;
	}

	public void addText(TextObject text) {
		this.textObjects.add(text);
	}

	public void removeText(TextObject text) {
		this.textObjects.remove(text);
	}

	public void addMouseHandler(MouseHandler c) {
		this.clickHandlers.add(c);
	}

	public boolean isMouseHovering() {
		return mousehovering;
	}

	public void draw() {
		glTranslated(this.x, this.y, this.z);
		
		// TODO
		GameEngine.getEngine().getWindow().setWorldObjectID(id);
		
		this.animatedReference.draw(animationstate, GameEngine.getEngine().getSystemTime() - timeOffset);
	}

	public void addChild(WorldObject child) {
		children.add(child);
	}
	
	public void removeAllChildren() {
		children.clear();
	}

	public void removeChild(WorldObject child) {
		children.remove(child);
	}

	final public void checkMouseMovement(MouseMovementInput newMMI) {
		if (isMouseHovering()) {
			if (newMMI.getX() > this.getGlobalX()
					&& newMMI.getX() < this.getGlobalX() + this.getWidth()
					&& newMMI.getY() > this.getGlobalY()
					&& newMMI.getY() < this.getGlobalY() + this.getHeight()) {

				this.onMouseMove(newMMI);

				for (WorldObject child : this.children) {
					child.checkMouseMovement(newMMI);
				}
			} else {
				mousehovering = false;

				for (WorldObject child : this.children) {
					child.checkMouseMovement(newMMI);
				}

				this.onMouseLeave(newMMI);
			}
		} else {
			if (newMMI.getX() > this.getGlobalX()
					&& newMMI.getX() < this.getGlobalX() + this.getWidth()
					&& newMMI.getY() > this.getGlobalY()
					&& newMMI.getY() < this.getGlobalY() + this.getHeight()) {

				mousehovering = true;

				// System.out.println("Entered: " + this);

				this.onMouseEnter();

				for (WorldObject child : this.children) {
					child.checkMouseMovement(newMMI);
				}
			}
		}
	}

	final public void onMouseEnter() {
		for (MouseHandler c : clickHandlers) {
			c.onMouseEnter();
		}
	}

	final public void onMouseMove(MouseMovementInput m) {
	}

	final public void onMouseLeave(MouseMovementInput m) {
		for (MouseHandler c : clickHandlers) {
			c.onMouseLeave();
		}
	}

	final public void onMouseClick() {
		for (MouseHandler c : clickHandlers) {
			c.onMouseClick();
		}
	}

	final public void update(float deltaTime) {
		this.onUpdate(deltaTime);

		for (WorldObject c : children) {
			c.update(deltaTime);
		}
	}

	public void onUpdate(float deltaTime) {

	}
}
