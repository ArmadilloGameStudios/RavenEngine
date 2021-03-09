package com.armadillogamestudios.tactics.gameengine.scene.battle;

import com.armadillogamestudios.engine2d.input.KeyData;
import com.armadillogamestudios.engine2d.input.KeyboardHandler;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;

public abstract class BattleScene<G extends TacticsGame<G>> extends TacticsScene<G> implements KeyboardHandler {

    private BattleMap<BattleScene<G>> map;

    public BattleScene(G game) {
        super(game);
    }

    @Override
    public final void onInputKey(KeyData keyData) {

    }
}
