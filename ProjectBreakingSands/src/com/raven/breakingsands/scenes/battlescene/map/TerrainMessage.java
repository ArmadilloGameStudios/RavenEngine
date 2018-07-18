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
    private Ability ability;

    public TerrainMessage(BattleScene scene) {
        super(scene);
        clearID();

        UIFont font = getFont();
        font.setSmall(true);
        font.setHighlight(false);

        setHighlight(BattleScene.OFF);
    }

    public void setState(Terrain.State state) {
        if (state == Terrain.State.SELECTABLE || this.state != state || this.ability != getScene().getActiveAbility()) {
            this.state = state;
            this.ability = getScene().getActiveAbility();

            switch (state) {
                case SELECTABLE:
                    if (getParent().getPawn() == getScene().getActivePawn()) {
                        setText("active");
                    } else
                        setText("select");
                    break;
                case MOVEABLE:
                case MOVE:
                    setText("move");
                    break;
                case ATTACKABLE:
                case ATTACK:
                    Pawn pawn = getParent().getPawn();
                    if (pawn != null) {
                        Weapon w = getScene().getActivePawn().getWeapon();
                        int damage = pawn.getDamage(w.getDamage(), w.getPiercing(), w.getShots());

                        if (damage >= pawn.getRemainingHitPoints()) {
                            setText("kill -" + damage);
                        } else {
                            setText("attack -" + damage);
                        }
                    } else {
                        setText("attack");
                    }
                    break;
                case UNSELECTABLE:
                    setText("");
                    break;
                case ABILITY:
                case ABILITYABLE:
                    setText(ability.name);
                    break;
            }
        }
    }

    @Override
    public void setVisibility(boolean visibility) {
        super.setVisibility(visibility && state != Terrain.State.UNSELECTABLE);
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Details;
    }

    @Override
    public float getZ() {
        return .1f;
    }
}
