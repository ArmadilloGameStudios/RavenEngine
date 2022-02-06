package com.armadillogamestudios.saga.scene.world;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.engine2d.ui.UIImage;
import com.armadillogamestudios.engine2d.ui.UILabel;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector2i;
import com.armadillogamestudios.saga.data.world.RegionData;
import com.armadillogamestudios.saga.scene.SagaScene;

import java.sql.SQLSyntaxErrorException;

public class RegionDisplay extends UIContainer<SagaScene> {

    private final UILabel<SagaScene> nameText, terrainText, idText, xyText;

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

        terrainText = new UILabel<>(scene, "", 100, 10);
        font = terrainText.getFont();
        font.setSmall(true);

        textContainer.addChild(terrainText);
        terrainText.load();

        idText = new UILabel<>(scene, "", 100, 10);
        font = idText.getFont();
        font.setSmall(true);

        textContainer.addChild(idText);
        idText.load();

        xyText = new UILabel<>(scene, "", 100, 10);
        font = xyText.getFont();
        font.setSmall(true);

        textContainer.addChild(xyText);
        xyText.load();

        textContainer.pack();
    }

    public void setInfo(RegionData regionData) {
        nameText.setText(regionData.getName());
        nameText.load();

        terrainText.setText(regionData.getTerrain().getName());
        terrainText.load();

        idText.setText(Integer.toString(regionData.getID()));
        idText.load();

        Vector2i vector2i = regionData.getCenter();
        String xy = vector2i.x + ", " + vector2i.y;
        xyText.setText(xy);
        xyText.load();
    }
}
