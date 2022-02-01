package com.armadillogamestudios.tactics.gameengine.game.graphicspipeline;

import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.graphicspipeline.GraphicsPipeline;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;

public class TacticsGraphicsPipeline {

    public static GraphicsPipeline createPipeline(TacticsGame<?> game, GameWindow window) {
        GraphicsPipeline pipeline = new GraphicsPipeline(game, window);

        pipeline.drawIDMap();

        pipeline.drawLayers(
                Layer.Destination.Terrain,
                Layer.Destination.Details,
                Layer.Destination.Effects,
                Layer.Destination.UI,
                Layer.Destination.ToolTip);

        pipeline.prepCompilationShader();
        pipeline.drawCompilationShaderGroupBasic(
                Layer.Destination.Terrain,
                Layer.Destination.Details,
                Layer.Destination.Effects);
        pipeline.drawCompilationShaderOverlay();
        pipeline.drawCompilationShaderGroupColorOnly(Layer.Destination.UI);
        pipeline.drawCompilationShaderGroupBasic(Layer.Destination.ToolTip);
        pipeline.blitCompilationShader();

        return pipeline;
    }

}
