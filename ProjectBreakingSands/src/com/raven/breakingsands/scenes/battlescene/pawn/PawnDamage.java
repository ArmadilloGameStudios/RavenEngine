package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine2d.ui.UIFont;
import com.raven.engine2d.worldobject.WorldTextObject;

public class PawnDamage extends WorldTextObject<BattleScene, Pawn> {
    public PawnDamage(BattleScene scene) {
        super(scene);

        UIFont font = getFont();
        font.setSmall(true);
        font.setHighlight(false);

        setHighlight(BattleScene.OFF);
    }

    @Override
    public float getZ() {
        return .1f;
    }
}
