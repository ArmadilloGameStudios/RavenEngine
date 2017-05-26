package com.raven.engine.graphics3d;

import com.raven.engine.worldobject.WorldObject;

import java.io.File;
import java.util.List;

/**
 * Created by cookedbird on 5/8/17.
 */
public class ModelData {
    private static List<Float> vertex_list;
    private static List<Float> normal_list;
    private static List<Float> colors_list;
    private ModelFrames modelFrames;
    private AnimatedModel model = null;

    public void setVertexData(List<Float> vertices) {
        vertex_list = vertices;
    }

    public List<Float> getVertexData() {
        return vertex_list;
    }

    public void setNormalData(List<Float> normals) {
        normal_list = normals;
    }

    public List<Float>  getNormalData() {
        return normal_list;
    }

    public void setColorData(List<Float> colors) {
        colors_list = colors;
    }

    public List<Float>  getColorData() {
        return colors_list;
    }

    public static ModelFrames load(File f) {
        return null; // new ModelData();
    }

    void setModelFrames(ModelFrames frames) {
        this.modelFrames = frames;

        if (model != null) {
            model.updateModelFrames(this);
        }
    }

    public ModelFrames getModelFrames() {
        return modelFrames;
    }

    public void setAnimatedModel(AnimatedModel model) {
        this.model = model;
    }
}
