package com.raven.engine2d.graphics2d.shader;

public class TerrainShader extends Shader {

    public TerrainShader() {
        super("vertex.glsl", "terrain_fragment.glsl");
    }

    @Override
    public void useProgram() {
        super.useProgram();
    }

    @Override
    public void endProgram() {

    }
}
