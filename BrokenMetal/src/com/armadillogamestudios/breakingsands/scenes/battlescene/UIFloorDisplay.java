package com.armadillogamestudios.breakingsands.scenes.battlescene;

import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.engine2d.ui.UILabel;
import com.armadillogamestudios.engine2d.ui.UIObject;
import com.armadillogamestudios.engine2d.util.math.Vector2f;

public class UIFloorDisplay extends UIObject<BattleScene, UIContainer<BattleScene>> {

    private Vector2f position = new Vector2f();

    private UILabel<BattleScene> label;

    public UIFloorDisplay(BattleScene scene) {
        super(scene);

        label = new UILabel<>(scene, "floor ?", (int) getWidth(), (int) getHeight());
        UIFont font = label.getFont();
        font.setSmall(true);
        label.load();
        addChild(label);
    }

    @Override
    public Vector2f getPosition() {
        return position;
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
        return 16;
    }

    @Override
    public float getWidth() {
        return 36;
    }

    public void setFloor(int difficulty) {
        label.setText("floor " + Integer.toString(difficulty));
        label.load();
    }
}
