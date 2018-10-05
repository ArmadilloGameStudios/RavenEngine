package com.raven.breakingsands.scenes.battlescene.levelup;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.ui.UIImage;

public class LevelUpHexConnection extends UIImage<BattleScene> {

    private LevelUpHexButton buttonA, buttonB;

    public LevelUpHexConnection(BattleScene scene, LevelUpHexButton buttonA, LevelUpHexButton buttonB) {
        super(scene, 0, 0, "sprites/connection.png");

        this.buttonA = buttonA;
        this.buttonB = buttonB;

        buttonA.addConnection(this);
        buttonB.addConnection(this);

        if (buttonA.getX() == buttonB.getX()) {

            setSpriteAnimation(new SpriteAnimationState(this, scene.getEngine().getAnimation("connection")));

            if (buttonA.getY() > buttonB.getY()) {
                setY(buttonB.getY() + buttonB.getHeight() * 2f);
                setX(buttonB.getX());
            } else {
                setY(buttonA.getY() + buttonA.getHeight() * 2f);
                setX(buttonA.getX());
            }

        } else if (buttonA.getX() > buttonB.getX()) {

            if (buttonA.getY() > buttonB.getY()) {

                SpriteAnimationState state = new SpriteAnimationState(this, scene.getEngine().getAnimation("connectiondag"));
                state.setFlip(true);
                setSpriteAnimation(state);
                setY(buttonA.getY() - 6);
                setX(buttonA.getX() - state.getWidth() - 6);

            } else {

                SpriteAnimationState state = new SpriteAnimationState(this, scene.getEngine().getAnimation("connectiondag"));
                setSpriteAnimation(state);
                setY(buttonA.getY() + buttonA.getHeight() * 2f - 4);
                setX(buttonA.getX() - state.getWidth() - 2);

            }
        } else {

            if (buttonA.getY() > buttonB.getY()) {

                setSpriteAnimation(new SpriteAnimationState(this, scene.getEngine().getAnimation("connectiondag")));
                setY(buttonA.getY() - 6);
                setX(buttonA.getX() + buttonA.getWidth() * 2f - 4);
            } else {

                SpriteAnimationState state = new SpriteAnimationState(this, scene.getEngine().getAnimation("connectiondag"));
                state.setFlip(true);
                setSpriteAnimation(state);
                setY(buttonA.getY() + buttonA.getHeight() * 2f - 4);
                setX(buttonA.getX() + buttonA.getWidth() * 2f - 8);
            }
        }

        checkConnection();
    }

    public void checkConnection() {

        setAnimationAction("idle");

        Ability a = buttonA.getAbility();
        Ability b = buttonB.getAbility();

        System.out.println();

        if (a != null && b != null) {

            System.out.println("Check");
            System.out.println(a.name);
            System.out.println(buttonA.isDisabled());
            System.out.println(b.name);
            System.out.println(buttonB.isDisabled());

            if (a.replace != null && !b.name.equals(a.replace)) {
                setVisibility(false);
                System.out.println("g");
                return;
            } else if (b.replace != null && !a.name.equals(b.replace)) {
                setVisibility(false);
                System.out.println("h");
                return;
            } else {
                setVisibility(true);
                System.out.println("i");
            }
        } else {
            System.out.println("Check");
            if (a != null) {
                System.out.println(a.name);
                System.out.println(buttonA.isDisabled());
            }
            if (b != null) {
                System.out.println(b.name);
                System.out.println(buttonB.isDisabled());
            }
            setVisibility(true);
        }

        if (buttonA.getType() == LevelUpHexButton.Type.CLASS && b != null && b.replace != null) {
            setVisibility(false);
            System.out.println("a");
            return;
        } else if (buttonB.getType() == LevelUpHexButton.Type.CLASS && a != null && a.replace != null) {
            setVisibility(false);
            System.out.println("b");
            return;
        }

        if (buttonA.isActive() && buttonB.isActive()) {
            setAnimationAction("connected");
            System.out.println("f");
        } else if (((a != null && b != null) ||
                (buttonA.getType() == LevelUpHexButton.Type.START && !buttonA.isDisabled()) ||
                (buttonB.getType() == LevelUpHexButton.Type.START && !buttonA.isDisabled()) ||
                (buttonA.getType() == LevelUpHexButton.Type.CLASS) ||
                (buttonB.getType() == LevelUpHexButton.Type.CLASS)) &&
                ((buttonA.isActive() && !buttonB.isLocked()) || (buttonB.isActive() && !buttonA.isLocked()))) {
            if (buttonA.isActive()) {
                buttonB.setDisable(false);
                if (buttonB.getAbility() != null)
                    System.out.println(buttonB.getAbility().name);
                System.out.println("d");
            } else {
                buttonA.setDisable(false);
                if (buttonA.getAbility() != null)
                    System.out.println(buttonA.getAbility().name);
                System.out.println("e");
            }
            setAnimationAction("connected partial");
        }
        System.out.println("over");
    }
}
