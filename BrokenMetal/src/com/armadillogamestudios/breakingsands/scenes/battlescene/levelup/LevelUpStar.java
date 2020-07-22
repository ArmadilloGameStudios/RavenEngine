package com.armadillogamestudios.breakingsands.scenes.battlescene.levelup;

import com.armadillogamestudios.breakingsands.scenes.battlescene.BattleScene;
import com.armadillogamestudios.breakingsands.scenes.battlescene.pawn.Pawn;
import com.armadillogamestudios.engine2d.ui.UIObject;

public abstract class LevelUpStar extends UIObject<BattleScene, UILevelUp> {

    public LevelUpStar(BattleScene scene) {
        super(scene);
    }

    public abstract void setPawn(Pawn pawn);

    public abstract void clear();
}
