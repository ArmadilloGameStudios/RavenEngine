package com.raven.engine.input;

import java.awt.event.MouseEvent;

import com.raven.engine.Game;

public class MouseMovementInput extends Input {
	private int x, y;

	public MouseMovementInput(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}
	
	@Override
	public void read(Game game) {
		game.mouseMove(this);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
