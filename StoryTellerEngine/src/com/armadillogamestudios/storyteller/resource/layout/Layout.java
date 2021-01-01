package com.armadillogamestudios.storyteller.resource.layout;

import com.armadillogamestudios.engine2d.database.GameData;

import java.util.List;
import java.util.stream.Collectors;

public class Layout {
    private String layout, inherits;
    private List<LayoutTrait> traits;

    public Layout(GameData gameData) {
        layout = gameData.getString("key");

        gameData.ifHas("inherits", (d) -> inherits = d.asString());
        gameData.ifHas("traits", (d) -> traits = d.asList().stream().map((t) -> new LayoutTrait(t.getString("key"), t.getInteger("value"))).collect(Collectors.toList()));
    }

    public String getInherits() {
        return inherits;
    }

    public boolean hasInherits() {
        return inherits != null;
    }

    public String getLayout() {
        return layout;
    }

    public List<LayoutTrait> getTraits() {
        return traits;
    }

    public boolean hasTraits() {
        return traits != null;
    }

    public static class LayoutTrait {
        public String key;
        public int value;

        public LayoutTrait(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
