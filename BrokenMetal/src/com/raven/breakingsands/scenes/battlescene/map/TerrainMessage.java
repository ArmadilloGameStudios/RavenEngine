package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.ui.UIFont;
import com.raven.engine2d.worldobject.WorldTextObject;

public class TerrainMessage extends WorldTextObject<BattleScene, Terrain> {
    private Terrain.State state;

    public TerrainMessage(BattleScene scene) {
        super(scene);
        clearID();

        UIFont font = getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setWrap(true);

        setHighlight(BattleScene.OFF);
    }

    public void setState(Terrain.State state) {
        this.state = state;
        Ability ability = getScene().getActiveAbility();

        switch (state) {
            case SELECTABLE:
                if (getParent().getPawn() == getScene().getActivePawn()) {
                    setText("deselect");
                } else {
                    setText("select\n" + getParent().getPawn().getName());
                }
                break;
            case MOVEABLE:
            case MOVE:
                setText("move");
                break;
            case UNSELECTABLE:
                if (getParent().getPawn() == null || getParent().getPawn().getTeam(true) != 1 || getScene().getActiveTeam() == 1) {
                    setText("");
                    break;
                }
            case ATTACKABLE:
            case ATTACK:
                Pawn pawn = getParent().getPawn();
                if (pawn != null && getScene().getActivePawn() != null) {
                    Weapon w = getScene().getActivePawn().getWeapon();
                    int damage = pawn.getDamage(w.getDamage(), w.getPiercing() + getScene().getActivePawn().getBonusPiercing(), w.getShots());

                    if (damage >= pawn.getRemainingHitPoints()) {
                        setText("kill -" + damage);
                    } else {
                        setText("attack -" + damage);
                    }
                } else {
                    if (pawn != null)
                        setText(pawn.getName());
                    else
                        setText("attack");
                }
                break;
            case ABILITY:
            case ABILITYABLE:
                setText(ability.name);
                break;
        }
    }

    @Override
    public void setVisibility(boolean visibility) {
        super.setVisibility(visibility && (state != Terrain.State.UNSELECTABLE || getParent().getPawn() != null));
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.ToolTip;
    }

    @Override
    public float getZ() {
        return 10 + getParent().getZ();
    }
}
