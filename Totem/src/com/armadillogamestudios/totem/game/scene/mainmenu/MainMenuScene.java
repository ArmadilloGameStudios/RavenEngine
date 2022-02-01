package com.armadillogamestudios.totem.game.scene.mainmenu;

import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.ScreenQuad;
import com.armadillogamestudios.engine2d.input.*;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.totem.game.TotemGame;
import com.armadillogamestudios.totem.game.scene.TotemScene;
import com.armadillogamestudios.totem.neuralnetwork.NeuralNetwork2;
import com.armadillogamestudios.totem.neuralnetwork.RandomGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.armadillogamestudios.totem.neuralnetwork.Main.SPELL_DATA;

public class MainMenuScene extends TotemScene implements KeyboardHandler, MouseClickHandler {

    private String[] savedGames;
    private NeuralNetwork2 neuralNetwork;

    private boolean currentlyDrawing = false;
    private double oldX, oldY;

    public MainMenuScene(TotemGame game) {
        super(game);

        Path charPath = Paths.get(getGame().getMainDirectory(), "save");

        File sFile = charPath.toFile();
        if (sFile.list() == null) {
            sFile.mkdir();
        }

        savedGames = sFile.list();

        addKeyboardHandler(this);

//        float[] spell = SPELL_DATA[SPELL_DATA.length - 1];
//        float[] drawnSpell = drawSpell(spell, 8f);
//        ScreenQuad.getLines().update(drawnSpell);
    }

    @Override
    protected void loadUI() {
        NewGameButton newGameButton = new NewGameButton(this);
        this.addChild(newGameButton);

        newGameButton.load();
    }

    @Override
    public void onUpdate(float deltaTime) {
        Mouse mouse = this.getGame().getEngine().getMouse();

        double x = mouse.getX();
        double y = mouse.getY();
    }

    @Override
    public final void onKeyPress(KeyData keyData) {
        if (keyData.getKey() ==  KeyData.Key.ESCAPE) {
            getGame().exit();
        }
    }

    @Override
    public void onKeyRelease(KeyData i) {

    }

    public final boolean isLoadGame() {
        return savedGames != null && savedGames.length > 0;
    }

    public final void onLoadGameClick() {

    }

    public final void onNewGameClick() {
        getGame().prepTransitionScene(getGame().getNewGameScene());
    }

    public final void onSettingsClick() {
    }

    public final void onCreditsClick() {
    }

    public void onExitClick() {
        getGame().exit();
    }

    @Override
    public void handleMouseClick() {
        this.currentlyDrawing = true;

        System.out.println("boop");
    }
}
