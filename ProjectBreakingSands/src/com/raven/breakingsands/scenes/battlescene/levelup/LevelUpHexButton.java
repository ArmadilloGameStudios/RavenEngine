package com.raven.breakingsands.scenes.battlescene.levelup;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.ui.UIButton;

import java.util.ArrayList;
import java.util.List;

public class LevelUpHexButton extends UIButton<BattleScene> {

    public enum Type {
        START, ABILITY, CLASS, WEAPON
    }

    private Type type;
    private LevelUpStar star;
    private List<LevelUpHexConnection> connections = new ArrayList<>();

    private static String getSprite(Type type) {
        switch (type) {
            default:
            case START:
                return "sprites/start hex.png";
            case CLASS:
                return "sprites/class hex.png";
            case WEAPON:
                return "sprites/gun hex.png";
            case ABILITY:
                return "sprites/ability hex.png";
        }
    }

    private Ability ability;
    private GameData pawnClass;

    public LevelUpHexButton(LevelUpStar star, Type type) {
        super(star.getScene(), getSprite(type), "hexbutton");

        setDisable(true);

        this.type = type;
        this.star = star;
    }

    @Override
    public void handleMouseClick() {
        Pawn pawn = star.getParent().getPawn();

        if (!isActive() && !isDisabled()) {
            switch (type) {
                default:
                case START:
                    break;
                case CLASS:
                    pawn.setCharacterClass(pawnClass);
                    break;
                case WEAPON:
                    pawn.setWeapon(ability.weapon);
                    break;
                case ABILITY:
                    pawn.addAbility(ability);
                    break;
            }

            pawn.setLevel(pawn.getLevel() + 1);

            star.getParent().close();
        }
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
    }

    public void setAbility(Ability ability) {
        this.ability = ability;

        if (ability.weapon != null) {
            type = Type.WEAPON;
            boolean active = star.getParent().getPawn().getWeapon().getName().equals(ability.weapon);
            if (active) {
                setDisable(false);
                setActive(true);
            }
        } else {
            type = Type.ABILITY;

            boolean active = star.getParent().getPawn().getAbilities().stream().anyMatch(a -> a.name.equals(ability.name));
            if (active) {
                setDisable(false);
                setActive(true);
            }
        }

        setSprite(getSprite(type));

        this.setToolTip(ability.name, ability.description);

        connections.forEach(LevelUpHexConnection::checkConnection);
    }

    public Ability getAbility() {
        return  ability;
    }

    public void setClass(GameData pawnClass) {
        this.pawnClass = pawnClass;

        Pawn pawn = star.getParent().getPawn();
        boolean active = pawn.getCharacterClass().equals(pawnClass.getString("name"));
        setDisable(!active);
        setActive(active);
        if (isActive()) {
            setLocked(true);
        }
        setDisable(!pawn.getCharacterClass().equals(pawnClass.getString("name")));
        setLocked(!pawn.getCharacterClass().equals("amateur"));
//        boolean disable = !pawn.getCharacterClass().equals("amateur") && !pawn.getCharacterClass().equals(pawnClass.getString("name"));
//        setActive(!disable);
//        setDisable(disable);
//        setLocked(disable);

        this.setToolTip(pawnClass.getString("name"), "");

        connections.forEach(LevelUpHexConnection::checkConnection);
    }

    public Type getType() {
        return type;
    }

    public void addConnection(LevelUpHexConnection connection) {
        this.connections.add(connection);
    }
}
