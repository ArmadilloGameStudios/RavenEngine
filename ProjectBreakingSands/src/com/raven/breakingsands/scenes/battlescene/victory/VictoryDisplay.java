package com.raven.breakingsands.scenes.battlescene.victory;

import com.raven.breakingsands.character.Augmentation;
import com.raven.breakingsands.character.Character;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.UICenterContainer;
import com.raven.engine2d.ui.UILabel;
import com.raven.engine2d.ui.UIObject;
import com.raven.engine2d.util.math.Vector4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VictoryDisplay extends UICenterContainer<BattleScene> {

    private Vector4f color = new Vector4f(.25f, .25f, .25f, .5f);

    private UILabel<BattleScene, VictoryDisplay> nameLabel, levelLabel, detailsLabel;
    private List<UIAugmentation> augs = new ArrayList<>();

    private List<Character> canLevel;
    private Character currentCharacter;

    public VictoryDisplay(BattleScene scene) {
        super(scene);


        nameLabel =
                new UILabel<>(getScene(), "Name", 200, 70);
        nameLabel.setFont(new Font( "SansSerif", Font.PLAIN, 20 ));
        nameLabel.updateTexture();
        addChild(nameLabel);

        levelLabel =
                new UILabel<>(getScene(), "Level", 200, 70);
        levelLabel.setFont(new Font( "SansSerif", Font.PLAIN, 20 ));
        levelLabel.updateTexture();
        addChild(levelLabel);

        detailsLabel =
                new UILabel<>(getScene(), "Details", 200, 146);
        detailsLabel.setFont(new Font( "SansSerif", Font.PLAIN, 14 ));
        detailsLabel.updateTexture();
        addChild(detailsLabel);

        for (int i = 0; i < 3; i++) {
            UIAugmentation hudAug = new UIAugmentation(getScene(), new Augmentation());
            augs.add(hudAug);
            addChild(hudAug);
            hudAug.updateTexture();
        }
    }

    @Override
    public float getBorder() {
        return 6f;
    }

    @Override
    public float getYOffset() {
        return super.getYOffset() + .5f;
    }

    @Override
    protected void onSetVisibility(boolean visibility) {
        if (visibility) {

            canLevel = getScene().getCharacterToLevel();

            nextCharacter();
        }
    }

    private void nextCharacter() {
        for (int i = 0; i < 3; i++) {
            UIAugmentation hudAug = new UIAugmentation(getScene(), new Augmentation());
            augs.add(hudAug);
            addChild(hudAug);
            hudAug.updateTexture();
        }

        if (canLevel.size() > 0) {
            currentCharacter = canLevel.get(0);
            displayCharacter(currentCharacter);
        } else {
            getScene().getGame().prepTransitionScene(
                    new BattleScene(getScene().getGame()));
        }
    }

    private void displayCharacter(Character character) {
        nameLabel.setText(character.getTitle() + " " + character.getName());
        nameLabel.updateTexture();
        levelLabel.setText("Level: " + character.getLevel());
        levelLabel.updateTexture();
        detailsLabel.setText(character.getTitle());
        detailsLabel.updateTexture();
    }

    @Override
    public Vector4f getColor() {
        return color;
    }

    @Override
    public void pack() {
        nameLabel.setYOffset(76f);
        nameLabel.setXOffset(-103f);

        levelLabel.setXOffset(-103f);

        detailsLabel.setYOffset(76f / 2f);
        detailsLabel.setXOffset(103f);

        for (int i = 0; i < augs.size(); i++) {
            UIAugmentation hudAug = augs.get(i);

            hudAug.setYOffset(-76f);
            hudAug.setXOffset((hudAug.getWidth() + getBorder()) * i - hudAug.getWidth() - getBorder());
        }

        for (UIObject child : getChildren()) {
            width = Math.max(width, child.getWidth() + Math.abs(child.getXOffset()) * 2f);
            height = Math.max(height, child.getHeight() + Math.abs(child.getYOffset()) * 2f);
        }

        width += getBorder() * 2f;
        height += getBorder() * 2f;
    }

    public void selectAugmentation(Augmentation augmentation) {
        currentCharacter.addAugmentation(augmentation);
        canLevel.remove(currentCharacter);

        nextCharacter();
    }
}
