package com.armadillogamestudios.breakingsands.scenes.battlescene.map;

import com.armadillogamestudios.breakingsands.character.Ability;
import com.armadillogamestudios.breakingsands.character.Weapon;
import com.armadillogamestudios.breakingsands.scenes.battlescene.pawn.Pawn;
import com.armadillogamestudios.breakingsands.scenes.battlescene.BattleScene;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.engine2d.worldobject.WorldTextObject;

import java.util.Optional;

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

        setVisibility(getParent().isMouseHovering());

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
                if (!getParent().isMouseHovering()) {
                    setVisibility(false);
                    break;
                }
            case ATTACK:
                showDamage();
                break;
            case ATTACKABLE:
                if (getScene().isTempState()) {
                    showDamage();
                } else {
                    Pawn pawn = getParent().getPawn();
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

    private void showDamage() {
        Pawn pawn = getParent().getPawn();

        if (pawn != null) {

            Pawn attacker;
            if (getScene().getState() == BattleScene.State.SHOW_ATTACK) {
                if (pawn.getTeam(true) == 0)
                    attacker = getScene().getTargetPawn();
                else
                    attacker = getScene().getActivePawn();
            } else {
                attacker = getScene().getActivePawn();
            }

            if (attacker != null) {
                Weapon w = Optional.ofNullable(getScene().getTempWeapon()).orElse(attacker.getWeapon());
                int damage = pawn.getDamage(w.getDamage(), w.getPiercing() + attacker.getBonusPiercing(), w.getShots(), attacker.getBonusDamage());

                if (damage >= pawn.getRemainingHitPoints() + pawn.getRemainingShield()) {
                    setText("kill -" + damage);
                } else {
                    setText("attack -" + damage);
                }

                setVisibility(true);
            } else {
                if (getScene().getState() == BattleScene.State.SHOW_ATTACK) {
                    if (getParent().isMouseHovering())
                        setText(pawn.getName());
                    else {
                        setVisibility(false);
                    }
                } else {
                    setText(pawn.getName());
                    setVisibility(true);
                }
            }
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
        return getParent().getZ();
    }
}
