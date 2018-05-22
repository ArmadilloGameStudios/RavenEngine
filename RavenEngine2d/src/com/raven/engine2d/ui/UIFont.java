package com.raven.engine2d.ui;

public class UIFont {
    private boolean highlight = true;
    private boolean small = false;

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public boolean isSmall() {
        return small;
    }


}
