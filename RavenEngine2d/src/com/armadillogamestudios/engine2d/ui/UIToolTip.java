package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.database.GameDataTable;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.util.math.Vector2f;

public class UIToolTip<S extends Scene<?>> extends UIObject<S> {

    private final int width;
    private final int height;
    private final Vector2f position = new Vector2f();

    private final GameDataTable tips;
    private final UILabel<S> title;
    private final UILabel<S> text;
    private UIMultipartImage<S> background;

    public UIToolTip(S scene, int width, int height, String backgroundSrc, String animation, GameDataTable tips) {
        super(scene);
        setDestination(Layer.Destination.ToolTip);
        clearID();

        setVisibility(false);

        this.width = width;
        this.height = height;

        this.tips = tips;

        if (backgroundSrc != null) {
            background = new UIMultipartImage<>(scene, backgroundSrc, animation);
            background.setDestination(Layer.Destination.ToolTip);
            background.clearID();
            addChild(background);

            background.setY(getHeight() - background.getHeight());
        }

        title = new UILabel<>(scene, "", width, height);
        UIFont font = title.getFont();
        font.setSmall(true);
        title.setY(title.getY() - 5);
        title.setX(title.getX() + 7);
        title.load();
        title.setDestination(Layer.Destination.ToolTip);
        title.clearID();
        addChild(title);

        text = new UILabel<>(scene, "", 100, height);
        font = text.getFont();
        font.setSmall(true);
        font.setWrap(true);
        text.setY(text.getY() - 19);
        text.setX(text.getX() + 5);
        text.load();
        text.setDestination(Layer.Destination.ToolTip);
        text.clearID();
        addChild(text);
    }

    public GameDataTable getTips() {
        return tips;
    }

    public void setText(String text) {
        this.text.setText(text);
        this.text.load(lines -> background.setRows(lines));
    }

    public void setTitle(String title) {
        this.title.setText(title);
        this.title.load();
    }

    @Override
    public float getZ() {
        return .5f;
    }

    @Override
    public Vector2f getPosition() {
        position.x = getX();
        position.y = getY();

        return position;
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

        float y = GameProperties.getHeight() - height - (float) (getScene().getEngine().getMouse().getY() / ((float) GameProperties.getDisplayHeight() / (float) GameProperties.getHeight()));

        if (background != null) {
            if (y - background.getHeight() < 0) {
                y += background.getHeight();
            }
        }

        return y;
    }

    @Override
    public void setY(float y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getX() {
        float x = (float) (getScene().getEngine().getMouse().getX() / ((float) GameProperties.getDisplayHeight() / (float) GameProperties.getHeight()));
//        float x = (float) (getScene().getEngine().getMouse().getX() * GameProperties.getDisplayWidth()) / (GameProperties.getDisplayWidth() * GameProperties.getScaling());

        if (background != null) {
            if (background.getWidth() + x > GameProperties.getDisplayWidth() / ((float) GameProperties.getDisplayHeight() / (float) GameProperties.getHeight())) {
                x -= background.getWidth();
            }
        }

        return x;
    }

    @Override
    public void setX(float x) {
        throw new UnsupportedOperationException();
    }

}
