package com.armadillogamestudios.storyteller.gameengine.game;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataTable;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.storyteller.gameengine.compator.TraitComparator;
import com.armadillogamestudios.storyteller.gameengine.scene.SceneStoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.splashscreen.SplashScreenScene;
import com.armadillogamestudios.storyteller.gameengine.scene.mainmenu.MainMenuScene;
import com.armadillogamestudios.storyteller.manager.Manager;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.resource.layout.Layout;
import com.armadillogamestudios.storyteller.resource.trait.Trait;
import com.armadillogamestudios.storyteller.resource.trait.TraitDescription;
import com.armadillogamestudios.storyteller.scenario.Scenario;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public abstract class StoryTeller<G extends StoryTeller<G>>
        extends Game<G> {

    private static Manager<TraitDescription> traitDescriptionManager = new Manager<>();
    private static Manager<Prompt> promptManager = new Manager<>();
    private static Manager<Resource> resourceManager = new Manager<>();
    private static Manager<Layout> layoutManager = new Manager<>();

    private static TraitComparator traitComparator;

    public static <G extends StoryTeller<G>> GameEngine<G> Launch(G story) {
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

    public static TraitComparator getTraitComparator() {
        return traitComparator;
    };

    @Override
    public final void setup() {
        load("traits", traitDescriptionManager, TraitDescription::new);
        load("prompts", promptManager, Prompt::create);
        load("layouts", layoutManager, Layout::new);

        traitComparator = new TraitComparator(getTraitOrder());
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
    public final SceneStoryTeller<G> loadInitialScene() {
        return new SplashScreenScene<>((G) this);
    }

    public abstract Scenario startScenario();

    private <T> void load(String tableName, Manager<T> manager, Function<GameData, T> constructor) {
        GameDataTable table = GameDatabase.all(tableName);

        table.forEach(trait -> manager.put(trait.getString("key"), constructor.apply(trait)));
    }

    public abstract Highlight getTextHighlight();

    public abstract List<String> getTraitOrder();

    public abstract MainMenuScene<G> getMainMenuScene();
}
