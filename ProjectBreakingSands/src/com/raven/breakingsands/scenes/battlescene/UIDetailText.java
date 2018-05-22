package com.raven.breakingsands.scenes.battlescene;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.ui.*;
import com.raven.engine2d.util.math.Vector2f;

import java.rmi.server.UID;

public class UIDetailText
        extends UIObject<BattleScene, UIContainer<BattleScene>> {

    private static final String bcgImgSrc = "sprites/character ui.png";

    public static SpriteSheet getSpriteSheet() {
        return GameEngine.getEngine().getSpriteSheet(bcgImgSrc);
    }

    private Vector2f position = new Vector2f();

    private UIImage<BattleScene> backgroundImg;
    private SelectionDetails details;
    private UILabel<BattleScene> uiName;

    public UIDetailText(BattleScene scene) {
        super(scene);

        backgroundImg = new UIImage<>(getScene(),
                (int) getWidth(), (int) getHeight(),
                bcgImgSrc);

        addChild(backgroundImg);

        uiName = new UILabel<>(getScene(), "-", 128, 128);

        UIFont font = uiName.getFont();
        font.setSmall(true);
        font.setHighlight(false);

        uiName.setX(325);
        uiName.setY(-105);

        uiName.load();

        addChild(uiName);

//        getFont().setSmall(true);
//        getFont().setHighlight(false);
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
    public float getHeight() {
        return 280;
    }

    @Override
    public float getWidth() {
        return 280;
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

    public void setDetails(SelectionDetails details) {
        this.details = details;

        uiName.setText(details.name);
        uiName.load();

    }
}
