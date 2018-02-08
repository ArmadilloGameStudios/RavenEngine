package com.raven.breakingsands.scenes.battlescene.menu;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.HUDButton;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.HUDText;
import com.raven.engine.worldobject.MouseHandler;
import com.raven.engine.worldobject.TextObject;

import java.awt.*;

public abstract class MenuButton extends HUDButton<BattleScene, Menu> {

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
