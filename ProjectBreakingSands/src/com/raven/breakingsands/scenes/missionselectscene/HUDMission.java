package com.raven.breakingsands.scenes.missionselectscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.mission.Mission;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.*;

import java.awt.*;

public class HUDMission extends HUDText<MissionSelectScene, HUDMissionSelect>
        implements MouseHandler {

    private float x, y;

    private TextObject defaultText, hoverText;

    private String text;

    public HUDMission(MissionSelectScene scene, Mission mission) {
        super(scene);

        this.text = mission.getName();

        defaultText = new TextObject((int)getWidth(), (int)getHeight());
        defaultText.setFont(new Font( "SansSerif", Font.PLAIN, 14));
        defaultText.setText(text);

        this.setTexture(defaultText);

        hoverText = new TextObject((int)getWidth(), (int)getHeight());
        hoverText.setFont(new Font( "SansSerif", Font.PLAIN, 14));
        hoverText.setBackgroundColor(new Vector4f(.1f, .6f, .9f, 1f));
        hoverText.setText(text);

        this.addMouseHandler(this);
    }

    @Override
    public void release() {
        super.release();

        defaultText.release();
        hoverText.release();
    }

    @Override
    public int getStyle() {
        return getParent().getStyle();
    }

    @Override
    public float getHeight() {
        return 75f;
    }

    @Override
    public float getWidth() {
        return 200f;
    }

    @Override
    public float getYOffset() {
        return getParent().getYOffset() + y;
    }

    @Override
    public void setYOffset(float y) {
        this.y = y;
    }

    @Override
    public float getXOffset() {
        return getParent().getXOffset() + x;
    }

    @Override
    public void setXOffset(float x) {
        this.x = x;
    }

    @Override
    public Vector4f getColor() {
        return null;
    }

    @Override
    public boolean doBlend() {
        return false;
    }

    @Override
    public void handleMouseClick() {
        BreakingSandsGame game = getScene().getGame();

        game.prepTransitionScene(new BattleScene(game));
    }

    @Override
    public void handleMouseEnter() {
        this.setTexture(hoverText);
    }

    @Override
    public void handleMouseLeave() {
        this.setTexture(defaultText);
    }

    @Override
    public void handleMouseHover(float delta) {

    }
}
