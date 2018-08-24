package com.raven.breakingsands.scenes.battlescene.levelup;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.ui.UIObject;

public abstract class LevelUpStar extends UIObject<BattleScene, UILevelUp2> {

    public LevelUpStar(BattleScene scene) {
        super(scene);
    }

    public abstract void setPawn(Pawn pawn);
}
