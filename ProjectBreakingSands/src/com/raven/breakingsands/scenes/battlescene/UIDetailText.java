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
    private final Pawn pawn;

    private Vector2f position = new Vector2f();

    private UIImage<BattleScene> backgroundImg;
    private UIImage<BattleScene> pawnImg;

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

    public UIDetailText(BattleScene scene, Pawn pawn) {
        super(scene);

        this.addMouseHandler(this);

        this.pawn = pawn;

        if (0 == pawn.getTeam(true)) {
            initLeft();
        } else {
            initRight();
        }
    }

    private void initLeft() {
        backgroundImg = new UIImage<>(getScene(),
                220, 54,
                bcgImgRightSrc);
        SpriteAnimationState state = new SpriteAnimationState(getScene().getEngine().getAnimation("details"));
        state.setFlip(false);
        backgroundImg.setSpriteAnimation(state);
        addChild(backgroundImg);

        pawnImg = new UIImage<>(getScene(),
                32, 32,
                pawn.getSpriteSheetName());
        state = new SpriteAnimationState(getScene().getEngine().getAnimation(pawn.getAnimationName()));
        state.setFlip(true);
        pawnImg.setSpriteAnimation(state);
        pawnImg.setX(182);
        pawnImg.setY(-2);
        addChild(pawnImg);

        uiName = new UILabel<>(getScene(), "-", 128, 10);
        UIFont font = uiName.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiName.setX(16);
        uiName.setY(56);
        uiName.load();

        addChild(uiName);

        uiLvl = new UILabel<>(getScene(), "", 100, 10);
        font = uiLvl.getFont();
        font.setSmall(true);
        font.setSide(UIFont.Side.RIGHT);
        font.setHighlight(false);
        uiLvl.setX(116 - 140);
        uiLvl.setY(56);
        uiLvl.load();

        addChild(uiLvl);

        uiHP = new UILabel<>(getScene(), "-", 30, 10);
        font = uiHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiHP.setX(46);
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

        uiSld = new UILabel<>(getScene(), "-", 30, 10);
        font = uiSld.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiSld.setX(46);
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

        uiLblMov = new UILabel<>(getScene(), "mov:", 30, 10);
        font = uiLblMov.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblMov.setX(112);
        uiLblMov.setY(26);
        uiLblMov.load();

        addChild(uiLblMov);

        uiMov = new UILabel<>(getScene(), "-", 30, 10);
        font = uiMov.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiMov.setX(128);
        uiMov.setY(26);
        uiMov.load();

        addChild(uiMov);

        uiRes = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRes.setX(128);
        uiRes.setY(6);
        uiRes.load();

        addChild(uiRes);

        uiLblRes = new UILabel<>(getScene(), "res:", 30, 10);
        font = uiLblRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRes.setX(112);
        uiLblRes.setY(6);
        uiLblRes.load();

        addChild(uiLblRes);

        // Weapon
        uiWeapon = new UILabel<>(getScene(), "-", 128, 10);
        font = uiWeapon.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiWeapon.setX(254);
        uiWeapon.setY(56);
        uiWeapon.load();

        addChild(uiWeapon);

        // Damage
        uiDmg = new UILabel<>(getScene(), "-", 30, 10);
        font = uiDmg.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiDmg.setX(272);
        uiDmg.setY(26);
        uiDmg.load();

        addChild(uiDmg);

        uiLblDmg = new UILabel<>(getScene(), "dmg:", 30, 10);
        font = uiLblDmg.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblDmg.setX(242);
        uiLblDmg.setY(26);
        uiLblDmg.load();

        addChild(uiLblDmg);

        // Piercing
        uiPir = new UILabel<>(getScene(), "-", 30, 10);
        font = uiPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiPir.setX(368);
        uiPir.setY(6);
        uiPir.load();

        addChild(uiPir);

        uiLblPir = new UILabel<>(getScene(), "pir:", 30, 10);
        font = uiLblPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblPir.setX(338);
        uiLblPir.setY(6);
        uiLblPir.load();

        addChild(uiLblPir);

        // Range
        uiRng = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRng.setX(368);
        uiRng.setY(26);
        uiRng.load();

        addChild(uiRng);

        uiLblRng = new UILabel<>(getScene(), "rng:", 30, 10);
        font = uiLblRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRng.setX(338);
        uiLblRng.setY(26);
        uiLblRng.load();

        addChild(uiLblRng);

        // Shots
        uiShots = new UILabel<>(getScene(), "-", 30, 10);
        font = uiShots.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiShots.setX(272);
        uiShots.setY(6);
        uiShots.load();

        addChild(uiShots);

        uiLblShots = new UILabel<>(getScene(), "shots:", 30, 10);
        font = uiLblShots.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblShots.setX(242);
        uiLblShots.setY(6);
        uiLblShots.load();

        addChild(uiLblShots);
    }

    private void initRight() {
        int offset = 10;

        backgroundImg = new UIImage<>(getScene(),
                220, 54,
                bcgImgRightSrc);
        SpriteAnimationState state = new SpriteAnimationState(getScene().getEngine().getAnimation("details"));
        state.setFlip(true);
        backgroundImg.setSpriteAnimation(state);
        addChild(backgroundImg);

        pawnImg = new UIImage<>(getScene(),
                32, 32,
                pawn.getSpriteSheetName());
        state = new SpriteAnimationState(getScene().getEngine().getAnimation(pawn.getAnimationName()));
        state.setFlip(false);
        pawnImg.setSpriteAnimation(state);
        pawnImg.setX(182 + offset + 2);
        pawnImg.setY(-2);
        addChild(pawnImg);

        uiName = new UILabel<>(getScene(), "-", 128, 10);
        UIFont font = uiName.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiName.setX(16 + offset);
        uiName.setY(56);
        uiName.load();

        addChild(uiName);

        uiLvl = new UILabel<>(getScene(), "", 100, 10);
        font = uiLvl.getFont();
        font.setSmall(true);
        font.setSide(UIFont.Side.RIGHT);
        font.setHighlight(false);
        uiLvl.setX(116 - 140 + offset);
        uiLvl.setY(56);
        uiLvl.load();

        addChild(uiLvl);

        uiHP = new UILabel<>(getScene(), "-", 30, 10);
        font = uiHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiHP.setX(46 + offset);
        uiHP.setY(26);
        uiHP.load();

        addChild(uiHP);

        uiLblHP = new UILabel<>(getScene(), "hp:", 60, 10);
        font = uiLblHP.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblHP.setX(4 + offset);
        uiLblHP.setY(26);
        uiLblHP.load();

        addChild(uiLblHP);

        uiSld = new UILabel<>(getScene(), "-", 30, 10);
        font = uiSld.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiSld.setX(46 + offset);
        uiSld.setY(6);
        uiSld.load();

        addChild(uiSld);

        uiLblSld = new UILabel<>(getScene(), "sld:", 30, 10);
        font = uiLblSld.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblSld.setX(4 + offset);
        uiLblSld.setY(6);
        uiLblSld.load();

        addChild(uiLblSld);

        uiLblMov = new UILabel<>(getScene(), "mov:", 30, 10);
        font = uiLblMov.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblMov.setX(112 + offset);
        uiLblMov.setY(26);
        uiLblMov.load();

        addChild(uiLblMov);

        uiMov = new UILabel<>(getScene(), "-", 30, 10);
        font = uiMov.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiMov.setX(128 + offset);
        uiMov.setY(26);
        uiMov.load();

        addChild(uiMov);

        uiRes = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRes.setX(128 + offset);
        uiRes.setY(6);
        uiRes.load();

        addChild(uiRes);

        uiLblRes = new UILabel<>(getScene(), "res:", 30, 10);
        font = uiLblRes.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRes.setX(112 + offset);
        uiLblRes.setY(6);
        uiLblRes.load();

        addChild(uiLblRes);

        // Weapon
        uiWeapon = new UILabel<>(getScene(), "-", 128, 10);
        font = uiWeapon.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiWeapon.setX(254 + offset);
        uiWeapon.setY(56);
        uiWeapon.load();

        addChild(uiWeapon);

        // Damage
        uiDmg = new UILabel<>(getScene(), "-", 30, 10);
        font = uiDmg.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiDmg.setX(272 + offset);
        uiDmg.setY(26);
        uiDmg.load();

        addChild(uiDmg);

        uiLblDmg = new UILabel<>(getScene(), "dmg:", 30, 10);
        font = uiLblDmg.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblDmg.setX(242 + offset);
        uiLblDmg.setY(26);
        uiLblDmg.load();

        addChild(uiLblDmg);

        // Piercing
        uiPir = new UILabel<>(getScene(), "-", 30, 10);
        font = uiPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiPir.setX(368 + offset);
        uiPir.setY(6);
        uiPir.load();

        addChild(uiPir);

        uiLblPir = new UILabel<>(getScene(), "pir:", 30, 10);
        font = uiLblPir.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblPir.setX(338 + offset);
        uiLblPir.setY(6);
        uiLblPir.load();

        addChild(uiLblPir);

        // Range
        uiRng = new UILabel<>(getScene(), "-", 30, 10);
        font = uiRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiRng.setX(368 + offset);
        uiRng.setY(26);
        uiRng.load();

        addChild(uiRng);

        uiLblRng = new UILabel<>(getScene(), "rng:", 30, 10);
        font = uiLblRng.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblRng.setX(338 + offset);
        uiLblRng.setY(26);
        uiLblRng.load();

        addChild(uiLblRng);

        // Shots
        uiShots = new UILabel<>(getScene(), "-", 30, 10);
        font = uiShots.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setSide(UIFont.Side.RIGHT);
        uiShots.setX(272 + offset);
        uiShots.setY(6);
        uiShots.load();

        addChild(uiShots);

        uiLblShots = new UILabel<>(getScene(), "shots:", 30, 10);
        font = uiLblShots.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        uiLblShots.setX(242 + offset);
        uiLblShots.setY(6);
        uiLblShots.load();

        addChild(uiLblShots);
    }

    public void setAnimationAction(String animation) {
        backgroundImg.setAnimationAction(animation);
    }

    public Pawn getPawn() {
        return pawn;
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
        return 44;
    }

    @Override
    public float getWidth() {
        return 220;
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
