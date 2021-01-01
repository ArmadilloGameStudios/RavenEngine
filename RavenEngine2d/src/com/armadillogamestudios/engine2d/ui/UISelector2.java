package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.util.Range;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

import java.util.List;

public class UISelector2<S extends Scene<?>> extends UIObject<S> {

    private final Vector2f position = new Vector2f();
    private final UIImage<S> image, marker;
    private final UIButton<S> leftBtn, rightBtn;
    private final UILabel<S> uiText, uiTitle;
    private List<Integer> values;
    private List<String> displays;
    private int index;
    private final int mx = 40;
    private final int mj = 20;

    public UISelector2(S scene,
                       String imgSrc, String imgMrkSrc, String leftBtnSrc, String rightBtnSrc) {
        super(scene);

        index = 2;

        image = new UIImage<>(scene, (int) getWidth(), (int) getHeight() * 2, imgSrc);
        addChild(image);
        image.setZ(.1f);

        marker = new UIImage<>(scene, 10, 10, imgMrkSrc);
        addChild(marker);
        marker.setZ(.2f);
        marker.setY(7);

        leftBtn = new UIButton<>(scene, leftBtnSrc, "selectorbutton") {
            @Override
            public void handleMouseClick() {
                index--;
                rightBtn.setDisable(false);
                if (index <= 0) {
                    leftBtn.setDisable(true);
                    index = 0;
                }

                uiText.setText(displays.get(index));
                uiText.load();

                updateMarkerPlace();
            }
        };
        leftBtn.setY(1);
        leftBtn.setX(5);
        leftBtn.setZ(.3f);
        addChild(leftBtn);

        rightBtn = new UIButton<>(scene, rightBtnSrc, "selectorbutton") {
            @Override
            public void handleMouseClick() {
                index++;
                leftBtn.setDisable(false);
                if (index >= values.size() - 1) {
                    rightBtn.setDisable(true);
                    index = values.size() - 1;
                }

                uiText.setText(displays.get(index));
                uiText.load();

                updateMarkerPlace();
            }
        };
        rightBtn.setY(1);
        rightBtn.setX(113 + rightBtn.getWidth());
        rightBtn.setZ(.3f);
        addChild(rightBtn);

        uiTitle = new UILabel<>(scene, "", 100, 24);
        uiTitle.setX(15);
        uiTitle.setY(5);
        UIFont f = uiTitle.getFont();
        f.setSmall(true);
        uiTitle.load();
        addChild(uiTitle);
        uiTitle.setZ(.4f);

        uiText = new UILabel<>(scene, "", 100, 24);
        uiText.setX(51);
        uiText.setY(5);
        f = uiText.getFont();
        f.setSmall(true);
        f.setSide(UIFont.Side.RIGHT);
        uiText.load();
        addChild(uiText);
        uiText.setZ(.5f);

        updateMarkerPlace();
    }

    private void updateMarkerPlace() {
        marker.setX(mx + mj * index);
    }

    public int getValue() {
        return values.get(index);
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
    public final Vector2f getPosition() {
        return position;
    }

    @Override
    public float getHeight() {
        if (image == null) {
            return 0;
        } else {
            return image.getTexture().getHeight();
        }
    }

    @Override
    public float getWidth() {
        if (image == null) {
            return 0;
        } else {
            return image.getTexture().getWidth();
        }
    }

    public void setTitle(String text) {
        uiTitle.setText(text);
        uiTitle.load();
    }

    public void setDisplays(List<String> displays) {
        this.displays = displays;
        uiText.setText(displays.get(index));
        uiText.load();

        this.values = Range.of(this.displays.size());
    }

    public void setTextHighlight(Highlight textHighlight) {
        uiTitle.setHighlight(textHighlight);
        uiText.setHighlight(textHighlight);
    }
}
