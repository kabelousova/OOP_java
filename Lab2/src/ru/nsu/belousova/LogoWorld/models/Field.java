package ru.nsu.belousova.LogoWorld.models;

import java.awt.*;

public class Field {
    private final boolean[][] _array;

    public Field(int x, int y) {
        _array = new boolean[x][y];

        for (int i = 0; i < x; ++i)
            for (int j = 0; j < y; ++j)
                _array[i][j] = false;
    }

    public boolean[][] getArray() {
        return _array;
    }

    public int getWidth() {
        return _array.length;
    }

    public int getHeight() {
        return _array[0].length;
    }

    public void colorPoint(int x, int y) {
        x = ((x % getWidth()) + getWidth()) % getWidth();
        y = ((y % getHeight()) + getHeight()) % getHeight();

        _array[x][y] = true;
    }

    public Point getPointOnField(int x, int y) {
        int newX = ((x % getWidth()) + getWidth()) % getWidth();
        int newY = ((y % getHeight()) + getHeight()) % getHeight();

        return new Point(newX, newY);
    }
}
