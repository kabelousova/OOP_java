package ru.nsu.belousova.LogoWorld.models;

public class Robot {
    private int _x;
    private int _y;

    private boolean _isDraw = false;

    public Robot(int x, int y) {
        this._x = x;
        this._y = y;
    }

    public boolean isDraw() {
        return _isDraw;
    }

    public void setDraw(boolean isDraw) {
        this._isDraw = isDraw;
    }

    public int getX() {
        return _x;
    }

    public void setX(int x) {
        this._x = x;
    }

    public int getY() {
        return _y;
    }

    public void setY(int y) {
        this._y = y;
    }
}
