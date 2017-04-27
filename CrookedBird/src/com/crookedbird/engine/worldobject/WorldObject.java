package com.crookedbird.engine.worldobject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameData;
import com.crookedbird.engine.graphics2d.AnimatedGraphic;
import com.crookedbird.engine.graphics3d.AnimatedModel;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.input.MouseMovementInput;

import static org.lwjgl.opengl.GL11.*;

public abstract class WorldObject implements Parentable {
	private double x, y, z;
	private int w, h, l;
	private boolean visible = true;
	private List<WorldObject> children = new CopyOnWriteArrayList<WorldObject>();
	private AnimatedModel animatedReference;
	private String animationstate = "idle";
	private boolean mousehovering = false;
	private List<ClickHandler> clickHandlers = new ArrayList<ClickHandler>();
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

		if (model != null) {
			animatedReference = new AnimatedModel(model);
		}

		this.x = x;
		this.y = y;
		this.z = 0;
		
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
		System.out.println(animationstate);
		
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

	public void addClickHandler(ClickHandler c) {
		this.clickHandlers.add(c);
	}

	public boolean isMouseHovering() {
		return mousehovering;
	}

	public void draw() {
		glPushMatrix();
		
		glTranslated(this.x, this.y, this.z);
		
		this.animatedReference.draw(animationstate, GameEngine.getEngine().getSystemTime() - timeOffset);
		
		glPopMatrix();
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

				this.onMouseEnter(newMMI);

				for (WorldObject child : this.children) {
					child.checkMouseMovement(newMMI);
				}
			}
		}
	}

	final public void checkMouseClick(MouseClickInput e) {
		if (this.visible && e.getX() > this.getGlobalX()
				&& e.getX() < this.getGlobalX() + this.getWidth()
				&& e.getY() > this.getGlobalY()
				&& e.getY() < this.getGlobalY() + this.getHeight()) {

			System.out.println("Click: " + this);
			this.onMouseClick(e);

			for (WorldObject child : this.children) {
				child.checkMouseClick(e);
			}
		}
	}

	final public void onMouseEnter(MouseMovementInput m) {

	}

	final public void onMouseMove(MouseMovementInput m) {

	}

	final public void onMouseLeave(MouseMovementInput m) {

	}

	final public void onMouseClick(MouseClickInput m) {
		for (ClickHandler c : clickHandlers) {
			c.onMouseClick(m);
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
