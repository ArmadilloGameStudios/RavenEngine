package com.armadillogamestudios.tactics.gameengine.scene.base;

import com.armadillogamestudios.engine2d.input.KeyData;
import com.armadillogamestudios.engine2d.input.KeyboardHandler;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;

import java.util.List;

public abstract class BaseScene<S extends BaseScene<S, G>, G extends TacticsGame<G>> extends TacticsScene<G> implements KeyboardHandler {

    public BaseScene(final G game) {
        super(game);
        addKeyboardHandler(this);
    }

    @Override
    public final void onInputKey(KeyData keyData) {

    }

    @Override
    public void updateUI(float deltaTime) {

    }

    @Override
    protected void loadUI() {
        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        getViews().forEach(view -> {

            UIContainer<S> viewContainer = new UIContainer<>(
                    (S) this,
                    UIContainer.Location.BOTTOM_LEFT,
                    UIContainer.Layout.VERTICAL);

            view.loadUI(viewContainer);

            addChild(viewContainer);
        });
    }

    protected abstract List<BaseView<S>> getViews();
}
