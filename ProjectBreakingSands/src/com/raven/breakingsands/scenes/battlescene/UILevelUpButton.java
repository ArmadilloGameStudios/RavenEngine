package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.ui.UIFont;
import com.raven.engine2d.ui.UITextButton;

public class UILevelUpButton extends UITextButton<BattleScene> {

    private Pawn pawn;
    private UILevelUp.RewardType type;
    private GameData reward;

    public UILevelUpButton(BattleScene scene, String text) {
        super(scene, text, "sprites/button.png", "mainbutton");

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
        }
    }

    public void setReward(Pawn pawn, UILevelUp.RewardType type, GameData reward) {
        this.pawn = pawn;
        this.type = type;
        this.reward = reward;

        setVisibility(true);
        setDisable(false);

        setText(reward.getString("name"));
        load();
    }

    public void clear() {
        setVisibility(false);
        setDisable(true);
    }
}
