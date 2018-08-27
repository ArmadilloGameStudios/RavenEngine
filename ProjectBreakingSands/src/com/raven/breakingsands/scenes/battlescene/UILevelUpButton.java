package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.levelup.UILevelUp2;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.ui.UIFont;
import com.raven.engine2d.ui.UITextButton;

public class UILevelUpButton extends UITextButton<BattleScene> {

    private Pawn pawn;
    private UILevelUp2.RewardType type;
    private GameData reward;
    private UILevelUp2 uiLevelUp;

    public UILevelUpButton(UILevelUp2 uiLevelUp, String text) {
        super(uiLevelUp.getScene(), text, "sprites/button.png", "mainbutton");

        this.uiLevelUp = uiLevelUp;

        UIFont font = getFont();
        font.setSmall(true);
        font.setButton(true);
    }

    @Override
    public void handleMouseClick() {

        if (!isDisabled()) {
            switch (type) {
                case WEAPON:
                    pawn.setWeapon(new Weapon(getScene(), reward));
                    break;
                case CLASS:
                    pawn.setCharacterClass(reward);
                    break;
                case ABILITY:
                    pawn.addAbility(new Ability(reward));
                    break;
            }

            pawn.setLevel(pawn.getLevel() + 1);

            getScene().getUILevelUp().close();

            getScene().setActivePawn(pawn);


        }
    }

    public void clear() {
        setVisibility(false);
        setDisable(true);
    }
}
