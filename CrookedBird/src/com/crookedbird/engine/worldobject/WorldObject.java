package com.crookedbird.engine.worldobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.crookedbird.engine.database.GameDataRow;
import com.crookedbird.engine.graphics.AnimatedGraphic;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.input.MouseMovementInput;

public abstract class WorldObject implements Parentable {
	private int x, y, w, h;
	private boolean visible = true;
	private List<WorldObject> children = new ArrayList<WorldObject>();
	private AnimatedGraphic animatedReference;
	private String animationstate = "idle";
	private boolean mousehovering = false;
	private List<ClickHandler> clickHandlers = new ArrayList<ClickHandler>();
	
	private TextObject text;

	private Parentable parent;

	public WorldObject(Parentable parent, GameDataRow anim) {
		this(parent, anim, 0, 0, 0, 0);
	}

	public WorldObject(Parentable parent, GameDataRow anim, int x, int y,
			int w, int h) {
		this.parent = parent;

		if (anim != null) {
			animatedReference = new AnimatedGraphic(anim);
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

	public void setAnimationState(String animationstate) {
		this.animationstate = animationstate;
	}

	@Override
	public int getGlobalX() {
		return this.getX() + parent.getGlobalX();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getGlobalY() {
		return this.getY() + parent.getGlobalY();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
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
	
	public void setText(TextObject text) {
		this.text= text;
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

			BufferedImage img = animatedReference.getImage(animationstate, 0);

			g.drawImage(img, getGlobalX(), getGlobalY(), null);
			
			if (text != null) {
				text.draw(g, getGlobalX(), getGlobalY());
				// g.setFont(Font.getFont(Font.DIALOG));
				// g.setColor(Color.RED);
			}
		}

		for (WorldObject child : children) {
			child.draw(i);
		}
	}

	public void addChild(WorldObject child) {
		children.add(child);
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

	final public void update(double deltaTime) {
		this.onUpdate(deltaTime);

		for (WorldObject c : children) {
			c.update(deltaTime);
		}
	}

	public void onUpdate(double deltaTime) {

	}

}
