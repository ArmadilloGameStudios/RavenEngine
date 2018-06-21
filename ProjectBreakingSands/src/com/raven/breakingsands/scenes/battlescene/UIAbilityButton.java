package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.character.Ability;
import com.raven.engine2d.ui.UIButton;

public abstract class UIAbilityButton extends UIButton<BattleScene> {
    private Ability ability;

    public UIAbilityButton(BattleScene scene, String btnImgSrc, String animation) {
        super(scene, btnImgSrc, animation);
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }
}
