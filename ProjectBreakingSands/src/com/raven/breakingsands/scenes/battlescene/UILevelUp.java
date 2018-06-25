package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.ui.*;
import com.raven.engine2d.util.math.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class UILevelUp extends UIObject<BattleScene, UIContainer<BattleScene>> {

    public enum RewardType {
        WEAPON, CLASS, ABILITY
    }

    private Vector2f position = new Vector2f();

    private Pawn pawn;

    private UIImage<BattleScene> background;
    private UILabel<BattleScene> lblLevelUp;
    private UILevelUpButton btnA, btnB, btnC;
    private List<UILevelUpButton> levelUpButtons = new ArrayList<>();

    public UILevelUp(BattleScene scene) {
        super(scene);

        background = new UIImage<>(scene, 256, 256, "sprites/level up.png");
        this.addChild(background);

        lblLevelUp = new UILabel<>(getScene(), "level up!", 256, 14);
        lblLevelUp.setX(100);
        lblLevelUp.setY(472);
        UIFont font = lblLevelUp.getFont();
        font.setSmall(false);
        font.setHighlight(true);
        lblLevelUp.load();
        addChild(lblLevelUp);

        btnA = new UILevelUpButton(getScene(), "-");
        btnA.load();
        addChild(btnA);
        levelUpButtons.add(btnA);

        btnB = new UILevelUpButton(getScene(), "-");
        btnB.setY(64);
        btnB.load();
        addChild(btnB);
        levelUpButtons.add(btnB);

        btnC = new UILevelUpButton(getScene(), "-");
        btnC.setY(64 * 2);
        btnC.load();
        addChild(btnC);
        levelUpButtons.add(btnC);
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
        return 256;
    }

    @Override
    public float getWidth() {
        return 256;
    }

    public void levelUp() {
        setVisibility(true);
        getScene().setPaused(true);
//        getParent().pack();
//        getScene().setActivePawn(pawn);
    }

    public void close() {
        setVisibility(false);
        getScene().setPaused(false);
        getScene().setActivePawn(pawn);
    }

    public void setPawn(Pawn pawn) {
        // TODO split into multiple methods
        this.pawn = pawn;

        String charClass = pawn.getCharacterClass();
        int lvl = pawn.getLevel() + 1;

        GameDatabase.all("leveling").stream()
                .filter(l -> l.getString("class").equals(charClass))
                .map(l -> l.getList("level").stream()
                        .filter(ll -> lvl == ll.getInteger("level"))
                        .findFirst()
                        .map(ll -> ll.getData("bonus")))
                .findFirst()
                .ifPresent(q -> q.ifPresent(b -> {
                    String type = b.getString("type");

                    switch (type) {
                        case "weapon":
                            String tag = b.getString("tag");

                            List<GameData> weapons = GameDatabase.all("weapon").stream()
                                    .filter(w -> w.getList("tags").stream().anyMatch(t -> t.asString().equals(tag)))
                                    .collect(Collectors.toList());

                            // TODO
                            // select at most 3
//                            Random r = new Random();
//                            GameData weapon = weapons.get(r.nextInt(weapons.size()));
//                            btnA.setReward(pawn, RewardType.WEAPON, weapon);

                            select3(weapons, RewardType.WEAPON);
                            break;
                        case "class":
                            List<GameData> classes = new ArrayList<>(GameDatabase.all("classes"));
//                            r = new Random();
//                            GameData newCharClass = classes.get(r.nextInt(classes.size()));
//
//                            btnA.setReward(pawn, RewardType.CLASS, newCharClass);

//                            System.out.println("Class");
                            select3(classes, RewardType.CLASS);
                            break;
                        case "ability":
//                            System.out.println("ability");
                            GameDatabase.all("classes").stream()
                                    .filter(c -> c.getString("name").equals(pawn.getCharacterClass()))
                                    .findFirst()
                                    .ifPresent(c -> {
                                        List<GameData> abilities = c.getList("abilities").stream()
                                                .filter(a -> {
                                                    List<String> existing = pawn.getAbilities().stream().map(ab -> ab.name).collect(Collectors.toList());

                                                    boolean valid = !existing.contains(a.getString("name"));

                                                    if (a.has("requires_not")) {
                                                        valid &= !existing.contains(a.getString("requires_not"));
                                                    }

                                                    if (a.has("replace")) {
                                                        valid &= existing.contains(a.getString("replace"));
                                                    }

                                                    return valid;
                                                })
                                                .collect(Collectors.toList());

                                        if (abilities.size() > 0) {
                                            select3(abilities, RewardType.ABILITY);
                                        }
                                    });
                            break;
                    }
                }));
    }

    private void select3(List<GameData> choices, RewardType type) {
        Random r = new Random();

        for (int i = 0; i < levelUpButtons.size(); i++) {

            if (choices.size() > 0) {
                GameData choice = choices.get(r.nextInt(choices.size()));
                choices.remove(choice);

                levelUpButtons.get(i).setReward(pawn, type, choice);
            } else {
                levelUpButtons.get(i).clear();
            }
        }
    }
}
