package com.raven.breakingsands.scenes.battlescene.menu;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.UIButton;

public abstract class MenuButton extends UIButton<BattleScene, Menu> {

    public MenuButton(BattleScene scene, String text) {
        super(scene, text);
    }

    @Override
    public float getHeight() {
        return 75f;
    }

    @Override
    public float getWidth() {
        return 200f;
    }
}
