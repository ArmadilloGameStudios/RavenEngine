package com.crookedbird.engine.forms;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.crookedbird.engine.GameEngine;

public class GameWindow extends JFrame implements WindowListener {
	private GameEngine engine;
	private JPanel pane;

	public GameWindow(GameEngine engine) {
		super();

		this.engine = engine;

		setResizable(false);

		pane = new JPanel();
		pane.addMouseListener(engine);
		pane.addMouseMotionListener(engine);
		add(pane);
		pane.setVisible(true);
		Dimension size = new Dimension(engine.getGame().getWidth(), engine.getGame().getHeight());
		
		pane.setMinimumSize(size);
		pane.setPreferredSize(size);
		pane.setSize(size);
		
		setTitle(engine.getGame().getTitle());

		pack();
		
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addWindowListener(this);
	}

	public GameEngine getGameEngine() {
		return engine;
	}

	public void draw(BufferedImage view) {
		pane.getGraphics().drawImage(view, 0, 0, null);
	}
	
	public void draw2(BufferedImage view) {
		double s = 0;
		
		int x = 0, y = 0;

		if (view.getWidth() / view.getHeight() > pane.getWidth()
				/ pane.getHeight()) {
			s = (double) view.getWidth() / (double) pane.getWidth();
		} else {
			s = (double) view.getHeight() / (double) pane.getHeight();
		}
		
		pane.getGraphics().drawImage(view, x, y, (int) (view.getWidth() / s),
				(int) (view.getHeight() / s), null);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("Closing Window");
		this.engine.breakThread();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}
