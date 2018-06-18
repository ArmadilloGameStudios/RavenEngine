package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.character.CharacterClass;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.hud.UICenterContainer;
import com.raven.breakingsands.scenes.hud.UIRightContainer;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class UILevelUp extends UICenterContainer<BattleScene> {

    private Pawn pawn;

    public UILevelUp(BattleScene scene) {
        super(scene);
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
                            Random r = new Random();
                            GameData weapon = weapons.get(r.nextInt(weapons.size()));

                            pawn.setWeapon(new Weapon(getScene(), weapon));
                            pawn.setLevel(lvl);
                            break;
                        case "class":
                            List<GameData> classes = GameDatabase.all("classes");

                            r = new Random();
                            GameData newCharClass = classes.get(r.nextInt(classes.size()));

                            pawn.setCharacterClass(newCharClass);

                            break;
                    }
                }));

        setVisibility(false);
        getScene().setPaused(false);
        getScene().setActivePawn(pawn);
    }
}
