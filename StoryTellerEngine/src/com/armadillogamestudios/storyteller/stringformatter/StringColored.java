package com.armadillogamestudios.storyteller.stringformatter;

import com.armadillogamestudios.engine2d.ui.UITextColorFeed;

public class StringColored {
    private final String string;
    private final UITextColorFeed colorFeed;

    public StringColored(String string, UITextColorFeed colorFeed) {
        this.string = string;
        this.colorFeed = colorFeed;
    }

    public StringColored(String string) {
        this.string = string;
        this.colorFeed = null;
    }

    public String getString() {
        return string;
    }

    public UITextColorFeed getColorFeed() {
        return colorFeed;
    }
}
