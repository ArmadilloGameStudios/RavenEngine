package com.armadillogamestudios.reclaim.scene.world;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.engine2d.input.MouseClickHandler;
import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.engine2d.ui.UILabel;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.reclaim.data.Region;
import com.armadillogamestudios.reclaim.data.Unit;

public class RecruitButton extends UILabel<ReclaimWorldScene> {

    public static final Highlight
            CanAfford = new Highlight(1f, 1f, 1f, 1f),
            CanNotAfford = new Highlight(.8f, .5f, .5f, 1f);

    private GameData gameData;
    private Region region;

    public RecruitButton(ReclaimWorldScene scene) {
        super(scene, "", 100, 10);

        UIFont font = this.getFont();
        font.setSmall(true);

        this.addMouseHandler((MouseClickHandler) () -> {
            if (Unit.recruitRequirementsMet(gameData, region.getSettlement())) {
                region.getSettlement().setRecruiting(gameData);

                region.getSettlement().getOwner().addGold(-gameData.getInteger("cost"));

                scene.updateUI();
            }
        });
    }

    public void setRecruit(Region region, GameData gameData) {
        this.gameData = gameData;
        this.region = region;

        this.setText(gameData.getString("name"));
    }


}
