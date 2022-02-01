package com.armadillogamestudios.totem.game.scene.testspell;

import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.ScreenQuad;
import com.armadillogamestudios.engine2d.input.Mouse;
import com.armadillogamestudios.engine2d.input.MouseHandler;
import com.armadillogamestudios.engine2d.ui.UIImage;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.totem.game.scene.mainmenu.MainMenuScene;

import java.util.Arrays;

import static com.armadillogamestudios.totem.neuralnetwork.Main.SPELL_DATA;

public class SpellDrawing extends UIImage<TestSpellScene> implements MouseHandler {

    private final Mouse mouse;
    private double oldX, oldY;
    private boolean isActivlyDrawing;
    private float[] spell;
    private int index = 0;

    public SpellDrawing(TestSpellScene scene) {
        super(scene, "world map.png");

        mouse = scene.getGame().getEngine().getMouse();

        addMouseHandler(this);

        spell = SPELL_DATA[SPELL_DATA.length - 1];
    }

    @Override
    public void handleMouseClick() {
        isActivlyDrawing = true;
        oldX = mouse.getX();
        oldY = mouse.getY();
        index = 0;
    }

    @Override
    public void handleMouseEnter() {

    }

    @Override
    public void handleMouseLeave() {

    }

    Vector2f mouseNorm = new Vector2f();

    @Override
    public void handleMouseHover(float delta) {
        if (isActivlyDrawing) {

            mouseNorm.x = (float) (mouse.getX() - oldX);
            mouseNorm.y = (float) (mouse.getY() - oldY);

            if (mouseNorm.length2() > 224) {
                mouseNorm.normalize(mouseNorm);

                oldX = mouse.getX();
                oldY = mouse.getY();

                System.out.println(mouseNorm);

                spell[index] = mouseNorm.x;
                index++;
                spell[index] = -mouseNorm.y;
                index++;

//                float[] drawnSpell = drawSpell(spell, 8f);
//                ScreenQuad.getLines().update(drawnSpell);

                if (index >= spell.length) {
                    index = 0;
                    isActivlyDrawing = false;

                    float[] predict = getScene().getNeuralNetwork().predict(spell);

                    System.out.println(Arrays.toString(predict));
                }
            }
        }
    }

    private float[] drawSpell(float[] spell, float scale) {

        float[] drawnSpell = new float[spell.length + 2];

        Vector2f normalizer = new Vector2f();
        for (int i = 0; i < spell.length; i += 2) {

            normalizer.x = spell[i];
            normalizer.y = spell[i + 1];

            normalizer.normalize(normalizer);

            drawnSpell[i + 2] = drawnSpell[i] + normalizer.x * scale / (float) GameProperties.getWidth();
            drawnSpell[i + 2 + 1] = drawnSpell[i + 1] + normalizer.y * scale / (float) GameProperties.getHeight();
        }

        return drawnSpell;
    }
}
