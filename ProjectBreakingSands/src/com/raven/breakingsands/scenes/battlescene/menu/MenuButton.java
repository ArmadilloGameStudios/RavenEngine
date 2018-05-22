package com.raven.breakingsands.scenes.battlescene.menu;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine2d.ui.UIButton;
import com.raven.engine2d.database.GameData;

public abstract class MenuButton extends UIButton<BattleScene> {

    public MenuButton(BattleScene scene, String text) {
        super(scene, text);
    }

    @Override
    public float getHeight() {
        return 64f;
    }

    @Override
    public float getWidth() {
        return 200f;
    }
}
