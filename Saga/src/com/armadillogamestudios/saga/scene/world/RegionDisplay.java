package com.armadillogamestudios.saga.scene.world;

import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.ui.*;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.saga.data.world.RegionData;
import com.armadillogamestudios.saga.scene.SagaScene;

public class RegionDisplay extends UIContainer<SagaScene> {

    private final UILabel<SagaScene> nameText, idText;

    public RegionDisplay(SagaScene scene) {
        super(scene, Location.BOTTOM_LEFT, Layout.VERTICAL);

        UIImage<SagaScene> background = new UIImage<>(scene, "region view.png");
        addChild(background);

        UIContainer<SagaScene> textContainer = new UIContainer<>(scene, Location.BOTTOM_LEFT, Layout.VERTICAL);

        nameText = new UILabel<>(scene, "", 100, 10);
        UIFont font = nameText.getFont();
        font.setSmall(true);

        textContainer.addChild(nameText);
        nameText.load();

        idText = new UILabel<>(scene, "", 100, 10);
        font = idText.getFont();
        font.setSmall(true);

        textContainer.addChild(idText);
        idText.load();

        textContainer.pack();
    }

    public void setInfo(RegionData regionData) {
        nameText.setText(regionData.getName());
        nameText.load();

        idText.setText(Integer.toString(regionData.getID()));
        idText.load();
    }
}
