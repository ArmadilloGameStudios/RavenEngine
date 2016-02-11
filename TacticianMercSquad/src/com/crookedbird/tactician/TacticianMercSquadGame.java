package com.crookedbird.tactician;

import com.crookedbird.engine.Game;
import com.crookedbird.engine.GameEngine;
import com.crookedbird.tactician.mainmenu.MainMenuScene;

public class TacticianMercSquadGame extends Game {
	private MainMenuScene mainMenu;
	
	
	public static void main(String[] args) {
		GameEngine.Launch(new TacticianMercSquadGame());
		System.out.println("Lunched");
	}
	
	public TacticianMercSquadGame() {
		mainMenu = new MainMenuScene(this);
	}
	
	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void breakdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadInitialScene() {
		transitionScene(mainMenu);		
	}

	@Override
	public int getWidth() {
		return 1024;
	}

	@Override
	public int getHeight() {
		return 768;
	}

	@Override
	public String getTitle() {
		return "Tactician: Mercenary Squad";
	}

}
