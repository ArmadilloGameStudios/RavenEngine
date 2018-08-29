package com.raven.breakingsands.scenes.battlescene.levelup;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.ui.*;
import com.raven.engine2d.util.math.Vector2f;

public class UILevelUp extends UIObject<BattleScene, UIContainer<BattleScene>> {

    private Vector2f position = new Vector2f();

    private Pawn pawn;

    private UIImage<BattleScene> background;
    private UILabel<BattleScene> lblLevelUp;
    private UILabel<BattleScene> lblDesc;
    private UITextButton<BattleScene> btnConfirmCancel;
    private LevelUpBaseStar starBasic;
    private LevelUpAdvancedStar starAdvanced;

    private Object reward;
    private LevelUpHexButton.Type rewardType;
    private LevelUpHexButton rewardButton;

    private String message = "Pick a new ability, weapon, or class.";

    public UILevelUp(BattleScene scene) {
        super(scene);

        background = new UIImage<>(scene, 256, 256, "sprites/level up.png");
        this.addChild(background);

        lblLevelUp = new UILabel<>(getScene(), "level up!", 256, 14);
        lblLevelUp.setX(90);
        lblLevelUp.setY(472);
        UIFont font = lblLevelUp.getFont();
        font.setSmall(false);
        font.setHighlight(true);
        lblLevelUp.load();
        addChild(lblLevelUp);

        lblDesc = new UILabel<>(getScene(), message, 244, 60);
        lblDesc.setY(330);
        lblDesc.setX(12);
        font = lblDesc.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setWrap(true);
        lblDesc.load();
        addChild(lblDesc);

        btnConfirmCancel = new UITextButton<BattleScene>(scene, "Cancel", "sprites/button.png", "mainbutton") {
            @Override
            public void handleMouseClick() {
                if (reward != null) selectReward();
                else {
                    close();
                }
            }
        };

        btnConfirmCancel.setX(-btnConfirmCancel.getWidth() + getWidth());
        btnConfirmCancel.setY(-btnConfirmCancel.getHeight());
        btnConfirmCancel.load();
        addChild(btnConfirmCancel);

        starBasic = new LevelUpBaseStar(this);
        starBasic.setX(getWidth() / 2);
        starBasic.setY(getHeight() / 2);
        addChild(starBasic);

        starAdvanced = new LevelUpAdvancedStar(this);
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
        clearReward();
        setVisibility(false);
        getScene().setPaused(false);
        getScene().setActivePawn(pawn);
    }

    public void setPawn(Pawn pawn) {
        // TODO split into multiple methods
        this.pawn = pawn;

        lblLevelUp.setText(pawn.getName() + " leveled up!");
        lblLevelUp.load();

        starBasic.clear();
        starAdvanced.clear();

        starBasic.setPawn(pawn);
        starAdvanced.setPawn(pawn);
    }

    public void setReward(LevelUpHexButton.Type type, Object reward, String description, LevelUpHexButton button) {
        clearReward();

        this.rewardType = type;
        this.reward = reward;
        this.rewardButton = button;

        button.setSpriteAnimation("hexbuttonactive");
        button.setActive(true);

        lblDesc.setText(description);
        lblDesc.load();

        btnConfirmCancel.setText("confirm");
        btnConfirmCancel.load();
    }

    public void clearReward() {
        if (rewardButton != null) {
            rewardButton.setSpriteAnimation("hexbutton");
            rewardButton.setActive(false);
//            rewardButton.setDisable(true);
        }

        this.rewardType = null;
        this.reward = null;
        this.rewardButton = null;

        lblDesc.setText(message);
        lblDesc.load();

        btnConfirmCancel.setText("cancel");
        btnConfirmCancel.load();
    }

    private void selectReward() {
        switch (rewardType) {
            default:
            case START:
                break;
            case CLASS:
                pawn.setCharacterClass((GameData) reward);
                break;
            case WEAPON:
                pawn.setWeapon((String) reward);
                break;
            case ABILITY:
                pawn.addAbility((Ability) reward);
                break;
        }

//        pawn.setLevel(pawn.getLevel() + 1);

        close();
    }

    public Pawn getPawn() {
        return pawn;
    }
}
