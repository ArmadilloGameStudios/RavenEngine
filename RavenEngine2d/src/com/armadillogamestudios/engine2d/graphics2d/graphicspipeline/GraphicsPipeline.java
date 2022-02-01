package com.armadillogamestudios.engine2d.graphics2d.graphicspipeline;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.shader.*;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.ui.UITextWriter;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

public class GraphicsPipeline {

    private final Game<?> game;
    private final GameWindow window;
    private final List<DrawCommand> drawCommandList = new ArrayList<>();

    public GraphicsPipeline(Game<?> game, GameWindow window) {
        this.game = game;
        this.window = window;

        addTextWriterShader();
    }

    private void addTextWriterShader() {
        drawCommandList.add(() -> {
            Scene<?> scene = game.getCurrentScene();

            List<UITextWriter> toWrite = scene.getToWrite();

            if (toWrite.size() > 0) {
                TextShader textShader = window.getTextShader();
                textShader.useProgram();

                toWrite.forEach(textWriter -> {
                    textWriter.write(textShader);
                    window.printErrors("Draw Text Error: ");
                });

                toWrite.clear();
            }
        });
    }

    public void drawLayers(Layer.Destination... destinations) {
        drawCommandList.add(() -> {
            Scene<?> scene = game.getCurrentScene();
            LayerShader layerShader = window.getLayerShader();
            layerShader.useProgram();

            for (int i = 0; i < destinations.length; i++) {
                scene.getLayer(destinations[i]).draw(layerShader);
            }
        });
    }

    public void prepCompilationShader() {
        drawCommandList.add(() -> {
            Scene<?> scene = game.getCurrentScene();
            CompilationShader compilationShader = window.getCompilationShader();
            compilationShader.useProgram();

            compilationShader.clear(scene.getBackgroundColor());
        });
    }

    public void drawCompilationShaderGroupBasic(Layer.Destination... destinations) {
        drawCommandList.add(() -> {
            Scene<?> scene = game.getCurrentScene();
            CompilationShader compilationShader = window.getCompilationShader();
            compilationShader.useProgram();

            for (int i = 0; i < destinations.length; i++) {
                compilationShader.compile(scene.getLayer(destinations[i]).getRenderTarget());
            }
        });
    }

    public void drawCompilationShaderOverlay() {
        drawCommandList.add(() -> {
            Scene<?> scene = game.getCurrentScene();
            game.getEngine().getSecondID();
            IDMapCompilationShader compilationShader = window.getIDMapCompilationShader();
            compilationShader.useProgram();

            compilationShader.compile(window.getIDMapShader().getRenderTarget());
        });
    }

    public void drawCompilationShaderGroupColorOnly(Layer.Destination... destinations) {
        drawCommandList.add(() -> {
            Scene<?> scene = game.getCurrentScene();
            CompilationShader compilationShader = window.getCompilationShader();
            compilationShader.useProgram();

            compilationShader.clearDepthBuffer();
            for (int i = 0; i < destinations.length; i++) {
                compilationShader.compile(scene.getLayer(destinations[i]).getRenderTarget());
            }
            compilationShader.drawColorOnly();
        });
    }

    public void blitCompilationShader() {
        drawCommandList.add(() -> {
            CompilationShader compilationShader = window.getCompilationShader();
            compilationShader.useProgram();

            compilationShader.blitToScreen();
        });
    }

    public void blitIDShader() {
        drawCommandList.add(() -> {
            IDMapShader compilationShader = window.getIDMapShader();
            compilationShader.useProgram();

            compilationShader.blitToScreen();
        });
    }

//    public void addCompilationShader(
//            List<Layer.Destination> primary,
//            Layer.Destination secondary,
//            List<Layer.Destination> last) {
//
//        drawCommandList.add(() -> {
//            Scene<?> scene = game.getCurrentScene();
//            CompilationShader compilationShader = window.getCompilationShader();
//            compilationShader.useProgram();
//
//            compilationShader.clear(scene.getBackgroundColor());
//
//            primary.forEach((destination) -> {
//                compilationShader.compile(scene.getLayer(destination).getRenderTarget());
//            });
//
//            compilationShader.clearDepthBuffer();
//            compilationShader.compile(scene.getLayer(secondary).getRenderTarget());
//            compilationShader.drawColorOnly();
//
//            last.forEach((destination) -> {
//                compilationShader.compile(scene.getLayer(destination).getRenderTarget());
//            });
//
//            compilationShader.blitToScreen();
//        });
//    }

    public void drawIDMap() {
        drawCommandList.add(() -> {
            Scene<?> scene = game.getCurrentScene();

            IDMapShader idMapShader = window.getIDMapShader();
            idMapShader.useProgram();

            idMapShader.clear();
            scene.drawIDMap(idMapShader);
        });
    }

    public void draw() {
        drawCommandList.forEach(DrawCommand::draw);
    }

    interface DrawCommand {
        void draw();
    }
}

