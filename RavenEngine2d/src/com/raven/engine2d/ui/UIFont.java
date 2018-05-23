package com.raven.engine2d.ui;


import static com.raven.engine2d.ui.UIFont.Side.LEFT;

public class UIFont {
    private boolean highlight = true;
    private boolean small = false;

    private int x, y;

    private UIFont.Side side = LEFT;

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public enum Side {
        RIGHT, LEFT
    };

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

    public void setSide(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }


}
