package com.armadillogamestudios.storyteller.gameengine;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataTable;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.engine2d.database.GameDatable;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.storyteller.gameengine.scene.SplashScreenScene;
import com.armadillogamestudios.storyteller.gameengine.scene.mainmenu.MainMenuScene;
import com.armadillogamestudios.storyteller.manager.Manager;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.resource.layout.Layout;
import com.armadillogamestudios.storyteller.resource.trait.TraitDescription;
import com.armadillogamestudios.storyteller.scenario.Scenario;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.function.Function;

public abstract class StoryTeller<S extends StoryTeller<S>> extends Game<S> {

    private static Manager<TraitDescription> traitDescriptionManager = new Manager<>();
    private static Manager<Prompt> promptManager = new Manager<>();
    private static Manager<Resource> resourceManager = new Manager<>();
    private static Manager<Layout> layoutManager = new Manager<>();

    public static <S extends StoryTeller<S>> GameEngine<S> Launch(S story) {
        return GameEngine.Launch(story);
    }

    public static Manager<TraitDescription> getTraitManager() {
        return traitDescriptionManager;
    }

    public static Manager<Prompt> getPromptManager() {
        return promptManager;
    }

    public static Manager<Resource> getResourceManager() {
        return resourceManager;
    }

    public static Manager<Layout> getLayoutManager() {
        return layoutManager;
    }

    @Override
    public final void setup() {
        load("traits", traitDescriptionManager, TraitDescription::new);
        load("prompts", promptManager, Prompt::new);
        load("layouts", layoutManager, Layout::new);
    }

    @Override
    public final void breakdown() {

    }

    @Override
    public final boolean saveGame() {
        return false;
    }

    @Override
    public final boolean loadGame() {
        return false;
    }

    @Override
    public final Scene<S> loadInitialScene() {
        return new SplashScreenScene(this);
    }

    public abstract Scenario startScenario();

    private <T> void load(String tableName, Manager<T> manager, Function<GameData, T> constructor) {
        GameDataTable table = GameDatabase.all(tableName);

        table.forEach(trait -> manager.put(trait.getString("name"), constructor.apply(trait)));
    }

    public abstract UIContainer<MainMenuScene<S>> getMainMenuDecor(MainMenuScene<S> scene);
}
