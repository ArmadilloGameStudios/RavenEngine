package com.armadillogamestudios.tactics.gameengine.scene.base;

import com.armadillogamestudios.engine2d.ui.container.UIContainer;

public abstract class BaseView<S extends BaseScene<S, ?>> {

    public abstract void loadUI(UIContainer<S> viewContainer);
}
