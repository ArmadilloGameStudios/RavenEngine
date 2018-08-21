package com.raven.engine2d.ui;

import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameDataTable;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.worldobject.Parentable;

public class UIToolTip<S extends Scene> extends UIObject<S, UIObject<S, Parentable<UIObject>>> {

    private final int width;
    private final int height;
    private Vector2f position = new Vector2f();

    private final GameDataTable tips;

    private UIMultipartImage<S> background;
    private UILabel<S> title;
    private UILabel<S> text;

    public UIToolTip(S scene, int width, int height, String backgroundSrc, String animation, GameDataTable tips) {
        super(scene);
        setDestination(Layer.Destination.TOOLTIP);
        clearID();

        setVisibility(false);

        this.width = width;
        this.height = height;

        this.tips = tips;

        if (backgroundSrc != null) {
            background = new UIMultipartImage<>(scene, backgroundSrc, animation);
            addChild(background);
            background.setDestination(Layer.Destination.TOOLTIP);
            background.clearID();

            background.setY(getHeight() * 2f - background.getHeight() * 2f);
        }

        title = new UILabel<>(scene, "", width, height);
        addChild(title);
        UIFont font = title.getFont();
        font.setSmall(true);
        title.setY(title.getY() - 10);
        title.setX(title.getX() + 14);
        title.load();
        title.setDestination(Layer.Destination.TOOLTIP);
        title.clearID();

        text = new UILabel<>(scene, "", width, height);
        addChild(text);
        font = text.getFont();
        font.setSmall(true);
        text.setY(text.getY() - 38);
        text.setX(text.getX() + 10);
        text.load();
        text.setDestination(Layer.Destination.TOOLTIP);
        text.clearID();
    }

    public void setText(String src) {
        tips.stream().filter(gd -> gd.getString("src").equals(src)).findFirst().ifPresent(tip -> {
            title.setText(tip.getString("title"));
            title.load();
            text.setText(tip.getString("text"));
            text.load();
        });
    }

    @Override
    public float getZ() {
        return 1.0f;
    }

    @Override
    public Vector2f getPosition() {
        position.x = getX();
        position.y = getY();

        return position;
    }

    @Override
    public int getStyle() {
        return 0;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getY() {
        return GameProperties.getScreenHeight() - height * 2f - (float) (getScene().getEngine().getMouse().getY() * 2f / GameProperties.getScaling());
    }

    @Override
    public void setY(float y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getX() {
        return (float) (getScene().getEngine().getMouse().getX() * 2f / GameProperties.getScaling());
    }

    @Override
    public void setX(float x) {
        throw new UnsupportedOperationException();
    }
}
