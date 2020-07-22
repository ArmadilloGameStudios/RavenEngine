package com.armadillogamestudios.storyteller.resource.layout;

import com.armadillogamestudios.engine2d.database.GameData;

import java.util.List;
import java.util.stream.Collectors;

public class Layout {
    private String layout, inherits;
    private List<LayoutTrait> traits;

    public Layout(GameData gameData) {
        layout = gameData.getString("name");

        gameData.ifHas("inherits", (d) -> inherits = d.asString());
        gameData.ifHas("traits", (d) -> traits = d.asList().stream().map((t) -> new LayoutTrait(t.getString("name"), t.getInteger("value"))).collect(Collectors.toList()));
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
        public String name;
        public int value;

        public LayoutTrait(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
