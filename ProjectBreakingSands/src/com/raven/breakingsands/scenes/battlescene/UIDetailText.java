package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.input.Mouse;
import com.raven.engine2d.ui.*;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.worldobject.MouseHandler;

public class UIDetailText
        extends UIObject<BattleScene, UIContainer<BattleScene>>
        implements MouseHandler {

    private static final String bcgImgRightSrc = "sprites/character ui.png";
    private static final String bcgImgLeftSrc = "sprites/selected ui.png";
    private final Pawn pawn;

    private Vector2f position = new Vector2f();

    private UIImage<BattleScene> backgroundImg;
    private SelectionDetails details;
    private UILabel<BattleScene>
            uiName, uiLvl, uiWeapon,
            uiHP, uiLblHP,
            uiMov, uiLblMov,
            uiRes, uiLblRes,
            uiSld, uiLblSld,
            uiDmg, uiLblDmg,
            uiPir, uiLblPir,
            uiRng, uiLblRng,
            uiShots, uiLblShots;

    public UIDetailText(BattleScene scene, Pawn pawn, int style) {
        super(scene);

        this.addMouseHandler(this);

        this.pawn = pawn;
        pawn.setUIDetailText(this);

        if (style == UIContainer.UPPER_LEFT) {
            initLeft();
        } else {
            initRight();
        }
    }

    private void initLeft() {
        backgroundImg = new UIImage<>(getScene(),
                116, 48,
                bcgImgRightSrc);

        backgroundImg.setSpriteAnimation(new SpriteAnimationState(getScene().getEngine().getAnimation("details")));

        addChild(backgroundImg);

        uiName = new UILabel<>(getScene(), "-", 128, 10);
        UIFont font = uiName.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiName.setX(58);
        uiName.setY(56);
        uiName.load();

        addChild(uiName);

        uiLvl = new UILabel<>(getScene(), "", 30, 10);
        font = uiLvl.getFont();
        font.setSmall(true);
        font.setSide(UIFont.Side.RIGHT);
        font.setHighlight(false);
        uiLvl.setX(150);
        uiLvl.setY(56);
        uiLvl.load();

        addChild(uiLvl);

        uiHP = new UILabel<>(getScene(), "-", 30, 10);
        font = uiHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiHP.setX(156);
        uiHP.setY(26);
        uiHP.load();

        addChild(uiHP);

        uiLblHP = new UILabel<>(getScene(), "hp:", 60, 10);
        font = uiLblHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblHP.setX(4);
        uiLblHP.setY(26);
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

        uiSld = new UILabel<>(getScene(), "-", 30, 10);
        font = uiSld.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiSld.setX(156);
        uiSld.setY(6);
        uiSld.load();

        addChild(uiSld);

        uiLblSld = new UILabel<>(getScene(), "sld:", 30, 10);
        font = uiLblSld.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblSld.setX(4);
        uiLblSld.setY(6);
        uiLblSld.load();

        addChild(uiLblSld);

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

        // Shots
        uiShots = new UILabel<>(getScene(), "-", 30, 10);
        font = uiShots.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiShots.setX(480);
        uiShots.setY(-2);
        uiShots.load();

        addChild(uiShots);

        uiLblShots = new UILabel<>(getScene(), "shots:", 30, 10);
        font = uiLblShots.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblShots.setX(450);
        uiLblShots.setY(-2);
        uiLblShots.load();

        addChild(uiLblShots);
    }

    private void initRight() {

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

        uiLvl = new UILabel<>(getScene(), "", 30, 10);
        font = uiLvl.getFont();
        font.setSmall(true);
        font.setSide(UIFont.Side.RIGHT);
        font.setHighlight(false);
        uiLvl.setX(146);
        uiLvl.setY(122);
        uiLvl.load();

        addChild(uiLvl);


        uiHP = new UILabel<>(getScene(), "-", 30, 10);
        font = uiHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiHP.setX(370 - 334);
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
        uiMov.setX(480 - 334);
        uiMov.setY(94);
        uiMov.load();

        addChild(uiMov);

        uiLblMov = new UILabel<>(getScene(), "mov:", 30, 10);
        font = uiLblMov.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblMov.setX(450 - 334);
        uiLblMov.setY(94);
        uiLblMov.load();

        addChild(uiLblMov);

        uiRes = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRes.setX(370 - 334);
        uiRes.setY(74);
        uiRes.load();

        addChild(uiRes);

        uiLblRes = new UILabel<>(getScene(), "res:", 30, 10);
        font = uiLblRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRes.setX(340 - 334);
        uiLblRes.setY(74);
        uiLblRes.load();

        addChild(uiLblRes);

        uiSld = new UILabel<>(getScene(), "-", 30, 10);
        font = uiSld.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiSld.setX(480 - 334);
        uiSld.setY(74);
        uiSld.load();

        addChild(uiSld);

        uiLblSld = new UILabel<>(getScene(), "sld:", 30, 10);
        font = uiLblSld.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblSld.setX(450 - 334);
        uiLblSld.setY(74);
        uiLblSld.load();

        addChild(uiLblSld);

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
        uiDmg.setX(370 - 334);
        uiDmg.setY(18);
        uiDmg.load();

        addChild(uiDmg);

        uiLblDmg = new UILabel<>(getScene(), "dmg:", 30, 10);
        font = uiLblDmg.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblDmg.setX(340 - 334);
        uiLblDmg.setY(18);
        uiLblDmg.load();

        addChild(uiLblDmg);

        // Piercing
        uiPir = new UILabel<>(getScene(), "-", 30, 10);
        font = uiPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiPir.setX(480 - 334);
        uiPir.setY(18);
        uiPir.load();

        addChild(uiPir);

        uiLblPir = new UILabel<>(getScene(), "pir:", 30, 10);
        font = uiLblPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblPir.setX(450 - 334);
        uiLblPir.setY(18);
        uiLblPir.load();

        addChild(uiLblPir);

        // Range
        uiRng = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRng.setX(370 - 334);
        uiRng.setY(-2);
        uiRng.load();

        addChild(uiRng);

        uiLblRng = new UILabel<>(getScene(), "rng:", 30, 10);
        font = uiLblRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRng.setX(340 - 334);
        uiLblRng.setY(-2);
        uiLblRng.load();

        addChild(uiLblRng);

        // Accuracy
        uiShots = new UILabel<>(getScene(), "-", 30, 10);
        font = uiShots.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiShots.setX(480 - 334);
        uiShots.setY(-2);
        uiShots.load();

        addChild(uiShots);

        uiLblShots = new UILabel<>(getScene(), "shots:", 30, 10);
        font = uiLblShots.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblShots.setX(450 - 334);
        uiLblShots.setY(-2);
        uiLblShots.load();

        addChild(uiLblShots);
    }

    public void setAnimationAction(String animation) {
        backgroundImg.setAnimationAction(animation);
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
        return 50;
    }

    @Override
    public float getWidth() {
        return 140;
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

        if (!uiName.getText().equals(details.name)) {
            uiName.setText(details.name);
            uiName.load();
        }

        if (!uiLvl.getText().equals(details.level)) {
            uiLvl.setText(details.level);
            uiLvl.load();
        }

        if (!uiHP.getText().equals(details.hp)) {
            uiHP.setText(details.hp);
            uiHP.load();
        }

        if (!uiMov.getText().equals(details.movement)) {
            uiMov.setText(details.movement);
            uiMov.load();
        }

        if (!uiRes.getText().equals(details.resistance)) {
            uiRes.setText(details.resistance);
            uiRes.load();
        }

        if (!uiSld.getText().equals(details.shield)) {
            uiSld.setText(details.shield);
            uiSld.load();
        }

        if (!uiWeapon.getText().equals(details.weapon)) {
            uiWeapon.setText(details.weapon);
            uiWeapon.load();
        }

        if (!uiDmg.getText().equals(details.damage)) {
            uiDmg.setText(details.damage);
            uiDmg.load();
        }

        if (!uiPir.getText().equals(details.piercing)) {
            uiPir.setText(details.piercing);
            uiPir.load();
        }

        if (!uiRng.getText().equals(details.range)) {
            uiRng.setText(details.range);
            uiRng.load();
        }

        if (!uiShots.getText().equals(details.shots)) {
            uiShots.setText(details.shots);
            uiShots.load();
        }
    }

    @Override
    public void handleMouseClick() {
        if (!getScene().isPaused())
            pawn.getParent().handleMouseClick();
    }

    @Override
    public void handleMouseEnter() {
        if (!getScene().isPaused())
            pawn.getParent().handleMouseEnter();
    }

    @Override
    public void handleMouseLeave() {
        if (!getScene().isPaused())
            pawn.getParent().handleMouseLeave();
    }

    @Override
    public void handleMouseHover(float delta) {
        if (!getScene().isPaused())
            pawn.getParent().handleMouseHover(delta);

    }
}
