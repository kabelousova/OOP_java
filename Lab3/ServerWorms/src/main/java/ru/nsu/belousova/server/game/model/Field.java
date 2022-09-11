package ru.nsu.belousova.server.game.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Field {
    private final ArrayList<Worm> _worms;
    private final ArrayList<Point> _food;
    private final int _width;
    private final int _height;

    public Field(int width, int height) {
        _height = height;
        _width = width;

        _worms = new ArrayList<>();
        _food = new ArrayList<>();

        for (int i = 0; i < GameProperties.COUNT_FOODS; ++i)
            addFood();
    }

    public void addFood() {
        boolean flag;
        Point newFood;

        do {
            int x = ThreadLocalRandom.current().nextInt(0, _width);
            int y = ThreadLocalRandom.current().nextInt(0, _height);

            newFood = new Point(x, y);
            flag = true;

            for (var worm : _worms)
                if (worm.getBody().lastIndexOf(newFood) != -1) {
                    flag = false;
                    break;
                }
        } while (_food.contains(newFood) || !flag);

        _food.add(newFood);
    }

    public ArrayList<Worm> getWorms() {
        return _worms;
    }

    public ArrayList<Point> getFood() {
        return _food;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public Worm createWorm(Object wormsAccessMonitor) {
        Point point = null;

        boolean[][] tmp = new boolean[_height][_width];
        boolean flag = true;

        for (var worm : _worms) {
            for (var p : worm.getBody()) {
                tmp[p.y][p.x] = true;
            }
        }

        for (var apple : _food) {
            tmp[apple.y][apple.x] = true;
        }

        for (int y = 0; y < _height; ++y) {
            for (int x = 2; x < _width - GameProperties.START_WORM_LENGTH - 1; ++x) {
                flag = true;

                for (int xx = x - 2; xx < x + GameProperties.START_WORM_LENGTH + 1; ++xx) {
                    if (tmp[y][xx]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    point = new Point(x, y);
                    break;
                }
            }
            if (flag)
                break;
        }

        if (point == null)
            return null;

        Worm newWorm = new Worm(point, wormsAccessMonitor);
        _worms.add(newWorm);

        return newWorm;
    }
}
