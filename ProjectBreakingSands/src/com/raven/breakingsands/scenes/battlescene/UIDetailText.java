package com.raven.breakingsands.scenes.battlescene;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.util.math.Vector4f;
import com.raven.engine2d.ui.UIContainer;
import com.raven.engine2d.ui.UIText;

public class UIDetailText
        extends UIText<BattleScene, UIContainer<BattleScene>> {

    public UIDetailText(BattleScene scene) {
        super(scene, "text");
    }

    @Override
    public int getStyle() {
        return getParent().getStyle();
    }

    @Override
    public float getHeight() {
        return 250;
    }

    @Override
    public float getWidth() {
        return 250;
    }

    @Override
    public SpriteAnimationState getSpriteAnimationState() {
        return null;
    }
}
