package com.crookedbird.engine.worldobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameData;
import com.crookedbird.engine.graphics.AnimatedGraphic;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.input.MouseMovementInput;

public abstract class WorldObject implements Parentable {
	private double x, y;
	private int w, h;
	private boolean visible = true;
	private List<WorldObject> children = new CopyOnWriteArrayList<WorldObject>();
	private AnimatedGraphic animatedReference;
	private String animationstate = "idle";
	private boolean mousehovering = false;
	private List<ClickHandler> clickHandlers = new ArrayList<ClickHandler>();
	private long timeOffset = 0;

	private List<TextObject> textObjects = new ArrayList<TextObject>();

	private Parentable parent;

	public WorldObject(Parentable parent, GameData img) {
		this(parent, img, 0, 0, 0, 0);
	}

	public WorldObject(Parentable parent, GameData img, double x, double y) {
		this(parent, img, x, y, 0, 0);
	}

	public WorldObject(Parentable parent, GameData img, double x, double y, int w,
			int h) {
		this.parent = parent;

		if (img != null) {
			animatedReference = new AnimatedGraphic(img);
		}

		this.x = x;
		this.y = y;
		this.w = (w == 0 && animatedReference != null ? animatedReference
				.getWidth() : w);
		this.h = (h == 0 && animatedReference != null ? animatedReference
				.getHeight() : h);
	}

	public String getAnimationState() {
		return animationstate;
	}

	public int setAnimationState(String animationstate) {
		this.animationstate = animationstate;
		timeOffset = GameEngine.getEngine().getSystemTime();
		
		return this.animatedReference.getFrameTime(animationstate);
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

	public void draw(BufferedImage i) {
		if (animatedReference != null && visible) {
			// System.out.println("Image for: " + asset);

			Graphics g = i.getGraphics();
			
			BufferedImage img = animatedReference.getImage(animationstate,
					GameEngine.getEngine().getSystemTime() - timeOffset);

			g.drawImage(img, (int)getGlobalX(), (int)getGlobalY(), null);

			if (textObjects != null) {
				for (TextObject t : textObjects)
					t.draw(g, (int)getGlobalX(), (int)getGlobalY());
			}
		}

		for (WorldObject child : children) {
			child.draw(i);
		}
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
