package com.raven.breakingsands.scenes.battlescene.levelup;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.util.math.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LevelUpBaseStar extends LevelUpStar {

    private Vector2f pos = new Vector2f();

    private LevelUpHexButton startButton,
            abilityButton1, abilityButton2,
            abilityButton3, abilityButton4,
            abilityButton5, abilityButton6,
            abilityButton12, abilityButton7,
            abilityButton8, abilityButton9,
            abilityButton10, abilityButton11,
            classButton1, classButton2, classButton3;

    private List<LevelUpHexButton> abilityButtonList = new ArrayList<>();
    private List<LevelUpHexButton> classButtonList = new ArrayList<>();

    private Pawn pawn;

    public LevelUpBaseStar(UILevelUp uiLevelUp) {
        super(uiLevelUp.getScene());

        startButton = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.START);
        addChild(startButton);
        startButton.setDisable(false);
        startButton.setActive(true);

        // abilities
        abilityButton1 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.ABILITY);
        abilityButton1.setY(40);
        addChild(abilityButton1);
        abilityButtonList.add(abilityButton1);

        abilityButton2 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.ABILITY);
        abilityButton2.setY(20);
        abilityButton2.setX(30);
        addChild(abilityButton2);
        abilityButtonList.add(abilityButton2);

        abilityButton3 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.ABILITY);
        abilityButton3.setY(-20);
        abilityButton3.setX(30);
        addChild(abilityButton3);
        abilityButtonList.add(abilityButton3);

        abilityButton4 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.ABILITY);
        abilityButton4.setY(-40);
        addChild(abilityButton4);
        abilityButtonList.add(abilityButton4);

        abilityButton5 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.ABILITY);
        abilityButton5.setY(-20);
        abilityButton5.setX(-30);
        addChild(abilityButton5);
        abilityButtonList.add(abilityButton5);

        abilityButton6 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.ABILITY);
        abilityButton6.setY(20);
        abilityButton6.setX(-30);
        addChild(abilityButton6);
        abilityButtonList.add(abilityButton6);

        abilityButton7 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.WEAPON);
        abilityButton7.setY(60);
        abilityButton7.setX(30);
        addChild(abilityButton7);
        abilityButtonList.add(abilityButton7);

        abilityButton8 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.WEAPON);
        abilityButton8.setY(0);
        abilityButton8.setX(60);
        addChild(abilityButton8);
        abilityButtonList.add(abilityButton8);

        abilityButton9 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.WEAPON);
        abilityButton9.setY(-60);
        abilityButton9.setX(30);
        addChild(abilityButton9);
        abilityButtonList.add(abilityButton9);

        abilityButton10 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.WEAPON);
        abilityButton10.setY(-60);
        abilityButton10.setX(-30);
        addChild(abilityButton10);
        abilityButtonList.add(abilityButton10);

        abilityButton11 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.WEAPON);
        abilityButton11.setY(0);
        abilityButton11.setX(-60);
        addChild(abilityButton11);
        abilityButtonList.add(abilityButton11);

        abilityButton12 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.WEAPON);
        abilityButton12.setY(60);
        abilityButton12.setX(-30);
        addChild(abilityButton12);
        abilityButtonList.add(abilityButton12);

        // class
        classButton1 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.CLASS);
        classButton1.setY(80);
        classButton1.setX(0);
        addChild(classButton1);
        classButtonList.add(classButton1);

        classButton2 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.CLASS);
        classButton2.setY(-40);
        classButton2.setX(60);
        addChild(classButton2);
        classButtonList.add(classButton2);

        classButton3 = new LevelUpHexButton(uiLevelUp, LevelUpHexButton.Type.CLASS);
        classButton3.setY(-40);
        classButton3.setX(-60);
        addChild(classButton3);
        classButtonList.add(classButton3);

        // Connections
        addChild(new LevelUpHexConnection(getScene(), startButton, abilityButton1));
        addChild(new LevelUpHexConnection(getScene(), startButton, abilityButton2));
        addChild(new LevelUpHexConnection(getScene(), startButton, abilityButton3));
        addChild(new LevelUpHexConnection(getScene(), startButton, abilityButton4));
        addChild(new LevelUpHexConnection(getScene(), startButton, abilityButton5));
        addChild(new LevelUpHexConnection(getScene(), startButton, abilityButton6));

        addChild(new LevelUpHexConnection(getScene(), abilityButton1, abilityButton2));
        addChild(new LevelUpHexConnection(getScene(), abilityButton2, abilityButton3));
        addChild(new LevelUpHexConnection(getScene(), abilityButton3, abilityButton4));
        addChild(new LevelUpHexConnection(getScene(), abilityButton4, abilityButton5));
        addChild(new LevelUpHexConnection(getScene(), abilityButton5, abilityButton6));
        addChild(new LevelUpHexConnection(getScene(), abilityButton6, abilityButton1));

        addChild(new LevelUpHexConnection(getScene(), abilityButton12, abilityButton6));
        addChild(new LevelUpHexConnection(getScene(), abilityButton12, abilityButton1));
        addChild(new LevelUpHexConnection(getScene(), abilityButton7, abilityButton1));
        addChild(new LevelUpHexConnection(getScene(), abilityButton7, abilityButton2));
        addChild(new LevelUpHexConnection(getScene(), abilityButton8, abilityButton2));
        addChild(new LevelUpHexConnection(getScene(), abilityButton8, abilityButton3));
        addChild(new LevelUpHexConnection(getScene(), abilityButton9, abilityButton3));
        addChild(new LevelUpHexConnection(getScene(), abilityButton9, abilityButton4));
        addChild(new LevelUpHexConnection(getScene(), abilityButton10, abilityButton4));
        addChild(new LevelUpHexConnection(getScene(), abilityButton10, abilityButton5));
        addChild(new LevelUpHexConnection(getScene(), abilityButton11, abilityButton5));
        addChild(new LevelUpHexConnection(getScene(), abilityButton11, abilityButton6));

        addChild(new LevelUpHexConnection(getScene(), classButton1, abilityButton12));
        addChild(new LevelUpHexConnection(getScene(), classButton1, abilityButton7));
        addChild(new LevelUpHexConnection(getScene(), classButton2, abilityButton8));
        addChild(new LevelUpHexConnection(getScene(), classButton2, abilityButton9));
        addChild(new LevelUpHexConnection(getScene(), classButton3, abilityButton10));
        addChild(new LevelUpHexConnection(getScene(), classButton3, abilityButton11));
    }

    @Override
    public Vector2f getPosition() {
        return pos;
    }

    @Override
    public int getStyle() {
        return 0;
    }

    @Override
    public float getX() {
        return pos.x;
    }

    @Override
    public void setX(float x) {
        pos.x = x - startButton.getWidth();
    }

    @Override
    public float getY() {
        return pos.y;
    }

    @Override
    public void setY(float y) {
        pos.y = y - startButton.getHeight();
    }

    @Override
    public float getWidth() {
        return 100;
    }

    @Override
    public float getHeight() {
        return 100;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;

        abilityButtonList.forEach(btn -> {
            btn.setLocked(false);
            btn.setActive(false);
            btn.setDisable(true);
        });
        classButtonList.forEach(btn -> {
            btn.setLocked(false);
            btn.setActive(false);
            btn.setDisable(true);
        });

        Random r = new Random(pawn.getAbilityOrder());

        int i = 0;
        List<Ability> abilities = GameDatabase.all("abilities").stream()
                .filter(a -> !a.has("class"))
                .map(Ability::new)
                .collect(Collectors.toList());

        for (; i < 12; i++) {
            int index = r.nextInt(abilities.size());
            Ability a = abilities.remove(index);

            LevelUpHexButton button = abilityButtonList.get(i);
            button.setAbility(a);
        }

        i = 0;
        List<GameData> classes = new GameDataList(GameDatabase.all("classes"));

        for (; i < 3; i++) {
            int index = r.nextInt(classes.size());
            GameData a = classes.remove(index);

            LevelUpHexButton button = classButtonList.get(i);
            button.setClass(a);
        }
    }

    @Override
    public void clear() {
        startButton.clear();
        startButton.setDisable(false);
        startButton.setActive(true);
        abilityButtonList.forEach(LevelUpHexButton::clear);
        classButtonList.forEach(LevelUpHexButton::clear);
    }
}
