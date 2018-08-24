package com.raven.breakingsands.scenes.battlescene.levelup;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.ui.*;
import com.raven.engine2d.util.math.Vector2f;

public class UILevelUp2 extends UIObject<BattleScene, UIContainer<BattleScene>> {

    public enum RewardType {
        WEAPON, CLASS, ABILITY
    }

    private Vector2f position = new Vector2f();

    private Pawn pawn;

    private UIImage<BattleScene> background;
    private UILabel<BattleScene> lblLevelUp;
    private LevelUpBaseStar starBasic;
    private LevelUpAdvancedStar starAdvanced;

    public UILevelUp2(BattleScene scene) {
        super(scene);

        background = new UIImage<>(scene, 256, 256, "sprites/level up.png");
        this.addChild(background);

        lblLevelUp = new UILabel<>(getScene(), "level up!", 256, 14);
        lblLevelUp.setX(100);
        lblLevelUp.setY(472);
        UIFont font = lblLevelUp.getFont();
        font.setSmall(false);
        font.setHighlight(true);
        lblLevelUp.load();
        addChild(lblLevelUp);

        starBasic = new LevelUpBaseStar(getScene());
        starBasic.setX(getWidth() / 2);
        starBasic.setY(getHeight() / 2);
        addChild(starBasic);

        starAdvanced = new LevelUpAdvancedStar(getScene());
        starAdvanced.setX(getWidth() * 3 / 2);
        starAdvanced.setY(getHeight() / 2);
        addChild(starAdvanced);
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public int getStyle() {
        return getParent().getStyle();
    }

    @Override
    public final float getY() {
        return position.y;
    }

    @Override
    public final void setY(float y) {
        position.y = y;
    }

    @Override
    public final float getX() {
        return position.x;
    }

    @Override
    public final void setX(float x) {
        position.x = x;
    }

    @Override
    public float getHeight() {
        return 256;
    }

    @Override
    public float getWidth() {
        return 256;
    }

    public void close() {
        setVisibility(false);
        getScene().setPaused(false);
        getScene().setActivePawn(pawn);
    }

    public void setPawn(Pawn pawn) {
        // TODO split into multiple methods
        this.pawn = pawn;

        starBasic.setPawn(pawn);
        starAdvanced.setPawn(pawn);
    }

    public Pawn getPawn() {
        return pawn;
    }
}
