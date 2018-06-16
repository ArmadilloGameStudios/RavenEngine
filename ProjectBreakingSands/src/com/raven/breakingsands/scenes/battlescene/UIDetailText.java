package com.raven.breakingsands.scenes.battlescene;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.ui.*;
import com.raven.engine2d.util.math.Vector2f;

public class UIDetailText
        extends UIObject<BattleScene, UIContainer<BattleScene>> {

    private static final String bcgImgRightSrc = "sprites/character ui.png";
    private static final String bcgImgLeftSrc = "sprites/selected ui.png";

    private Vector2f position = new Vector2f();

    private UIImage<BattleScene> backgroundImg;
    private SelectionDetails details;
    private UILabel<BattleScene>
            uiName, uiWeapon,
            uiHP, uiLblHP,
            uiMov, uiLblMov,
            uiRes, uiLblRes,
            uiEva, uiLblEva,
            uiDmg, uiLblDmg,
            uiPir, uiLblPir,
            uiRng, uiLblRng,
            uiAcc, uiLblAcc;

    public UIDetailText(BattleScene scene, int style) {
        super(scene);

        if (style == UIContainer.BOTTOM_RIGHT) {
            initRight();
        } else {
            initLeft();
        }
    }

    private void initRight() {
        backgroundImg = new UIImage<>(getScene(),
                (int) getWidth(), (int) getHeight(),
                bcgImgRightSrc);

        addChild(backgroundImg);

        uiName = new UILabel<>(getScene(), "-", 128, 10);
        UIFont font = uiName.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiName.setX(350);
        uiName.setY(122);
        uiName.load();

        addChild(uiName);

        uiHP = new UILabel<>(getScene(), "-", 30, 10);
        font = uiHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiHP.setX(370);
        uiHP.setY(94);
        uiHP.load();

        addChild(uiHP);

        uiLblHP = new UILabel<>(getScene(), "hp:", 60, 10);
        font = uiLblHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblHP.setX(340);
        uiLblHP.setY(94);
        uiLblHP.load();

        addChild(uiLblHP);

        uiMov = new UILabel<>(getScene(), "-", 30, 10);
        font = uiMov.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiMov.setX(480);
        uiMov.setY(94);
        uiMov.load();

        addChild(uiMov);

        uiLblMov = new UILabel<>(getScene(), "mov:", 30, 10);
        font = uiLblMov.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblMov.setX(450);
        uiLblMov.setY(94);
        uiLblMov.load();

        addChild(uiLblMov);

        uiRes = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRes.setX(370);
        uiRes.setY(74);
        uiRes.load();

        addChild(uiRes);

        uiLblRes = new UILabel<>(getScene(), "res:", 30, 10);
        font = uiLblRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRes.setX(340);
        uiLblRes.setY(74);
        uiLblRes.load();

        addChild(uiLblRes);

        uiEva = new UILabel<>(getScene(), "-", 30, 10);
        font = uiEva.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiEva.setX(480);
        uiEva.setY(74);
        uiEva.load();

        addChild(uiEva);

        uiLblEva = new UILabel<>(getScene(), "eva:", 30, 10);
        font = uiLblEva.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblEva.setX(450);
        uiLblEva.setY(74);
        uiLblEva.load();

        addChild(uiLblEva);

        // Weapon
        uiWeapon = new UILabel<>(getScene(), "-", 128, 10);
        font = uiWeapon.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiWeapon.setX(350);
        uiWeapon.setY(46);
        uiWeapon.load();

        addChild(uiWeapon);

        // Damage
        uiDmg = new UILabel<>(getScene(), "-", 30, 10);
        font = uiDmg.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiDmg.setX(370);
        uiDmg.setY(18);
        uiDmg.load();

        addChild(uiDmg);

        uiLblDmg = new UILabel<>(getScene(), "dmg:", 30, 10);
        font = uiLblDmg.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblDmg.setX(340);
        uiLblDmg.setY(18);
        uiLblDmg.load();

        addChild(uiLblDmg);

        // Piercing
        uiPir = new UILabel<>(getScene(), "-", 30, 10);
        font = uiPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiPir.setX(480);
        uiPir.setY(18);
        uiPir.load();

        addChild(uiPir);

        uiLblPir = new UILabel<>(getScene(), "pir:", 30, 10);
        font = uiLblPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblPir.setX(450);
        uiLblPir.setY(18);
        uiLblPir.load();

        addChild(uiLblPir);

        // Range
        uiRng = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRng.setX(370);
        uiRng.setY(-2);
        uiRng.load();

        addChild(uiRng);

        uiLblRng = new UILabel<>(getScene(), "rng:", 30, 10);
        font = uiLblRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRng.setX(340);
        uiLblRng.setY(-2);
        uiLblRng.load();

        addChild(uiLblRng);

        // Accuracy
        uiAcc = new UILabel<>(getScene(), "-", 30, 10);
        font = uiAcc.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiAcc.setX(480);
        uiAcc.setY(-2);
        uiAcc.load();

        addChild(uiAcc);

        uiLblAcc = new UILabel<>(getScene(), "acc:", 30, 10);
        font = uiLblAcc.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblAcc.setX(450);
        uiLblAcc.setY(-2);
        uiLblAcc.load();

        addChild(uiLblAcc);
    }

    private void initLeft() {

        backgroundImg = new UIImage<>(getScene(),
                (int) getWidth(), (int) getHeight(),
                bcgImgLeftSrc);

        addChild(backgroundImg);

        uiName = new UILabel<>(getScene(), "-", 128, 10);
        UIFont font = uiName.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiName.setX(54);
        uiName.setY(122);
        uiName.load();

        addChild(uiName);

        uiHP = new UILabel<>(getScene(), "-", 30, 10);
        font = uiHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiHP.setX(370-334);
        uiHP.setY(94);
        uiHP.load();

        addChild(uiHP);

        uiLblHP = new UILabel<>(getScene(), "hp:", 60, 10);
        font = uiLblHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblHP.setX(6);
        uiLblHP.setY(94);
        uiLblHP.load();

        addChild(uiLblHP);

        uiMov = new UILabel<>(getScene(), "-", 30, 10);
        font = uiMov.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiMov.setX(480-334);
        uiMov.setY(94);
        uiMov.load();

        addChild(uiMov);

        uiLblMov = new UILabel<>(getScene(), "mov:", 30, 10);
        font = uiLblMov.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblMov.setX(450-334);
        uiLblMov.setY(94);
        uiLblMov.load();

        addChild(uiLblMov);

        uiRes = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRes.setX(370-334);
        uiRes.setY(74);
        uiRes.load();

        addChild(uiRes);

        uiLblRes = new UILabel<>(getScene(), "res:", 30, 10);
        font = uiLblRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRes.setX(340-334);
        uiLblRes.setY(74);
        uiLblRes.load();

        addChild(uiLblRes);

        uiEva = new UILabel<>(getScene(), "-", 30, 10);
        font = uiEva.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiEva.setX(480-334);
        uiEva.setY(74);
        uiEva.load();

        addChild(uiEva);

        uiLblEva = new UILabel<>(getScene(), "eva:", 30, 10);
        font = uiLblEva.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblEva.setX(450-334);
        uiLblEva.setY(74);
        uiLblEva.load();

        addChild(uiLblEva);

        // Weapon
        uiWeapon = new UILabel<>(getScene(), "-", 128, 10);
        font = uiWeapon.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiWeapon.setX(6);
        uiWeapon.setY(46);
        uiWeapon.load();

        addChild(uiWeapon);

        // Damage
        uiDmg = new UILabel<>(getScene(), "-", 30, 10);
        font = uiDmg.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiDmg.setX(370-334);
        uiDmg.setY(18);
        uiDmg.load();

        addChild(uiDmg);

        uiLblDmg = new UILabel<>(getScene(), "dmg:", 30, 10);
        font = uiLblDmg.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblDmg.setX(340-334);
        uiLblDmg.setY(18);
        uiLblDmg.load();

        addChild(uiLblDmg);

        // Piercing
        uiPir = new UILabel<>(getScene(), "-", 30, 10);
        font = uiPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiPir.setX(480-334);
        uiPir.setY(18);
        uiPir.load();

        addChild(uiPir);

        uiLblPir = new UILabel<>(getScene(), "pir:", 30, 10);
        font = uiLblPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblPir.setX(450-334);
        uiLblPir.setY(18);
        uiLblPir.load();

        addChild(uiLblPir);

        // Range
        uiRng = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRng.setX(370-334);
        uiRng.setY(-2);
        uiRng.load();

        addChild(uiRng);

        uiLblRng = new UILabel<>(getScene(), "rng:", 30, 10);
        font = uiLblRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRng.setX(340-334);
        uiLblRng.setY(-2);
        uiLblRng.load();

        addChild(uiLblRng);

        // Accuracy
        uiAcc = new UILabel<>(getScene(), "-", 30, 10);
        font = uiAcc.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiAcc.setX(480-334);
        uiAcc.setY(-2);
        uiAcc.load();

        addChild(uiAcc);

        uiLblAcc = new UILabel<>(getScene(), "acc:", 30, 10);
        font = uiLblAcc.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblAcc.setX(450-334);
        uiLblAcc.setY(-2);
        uiLblAcc.load();

        addChild(uiLblAcc);
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
    public float getHeight() {
        return 280;
    }

    @Override
    public float getWidth() {
        return 280;
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

    public void setDetails(SelectionDetails details) {
        this.details = details;

        uiName.setText(details.name);
        uiName.load();

        uiHP.setText(details.hp);
        uiHP.load();

        uiMov.setText(details.movement);
        uiMov.load();

        uiRes.setText(details.resistance);
        uiRes.load();

        uiEva.setText("-");
        uiEva.load();

        uiWeapon.setText(details.weapon);
        uiWeapon.load();

        uiDmg.setText(details.damage);
        uiDmg.load();

        uiPir.setText(details.piercing);
        uiPir.load();

        uiRng.setText(details.range);
        uiRng.load();

        uiAcc.setText("-");
        uiAcc.load();
    }
}
