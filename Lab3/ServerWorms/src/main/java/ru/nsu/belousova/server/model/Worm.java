package ru.nsu.belousova.server.model;

import java.awt.*;
import java.util.ArrayList;

public class Worm {
    private final ArrayList<Point> _body;
    private int _score;

    public Worm(int x, int y) {
        _body = new ArrayList<>();

        for (int i = 0; i < GameProperties.START_WORM_LENGTH; ++i)
            _body.add(new Point(x + i, y));
    }

    public ArrayList<Point> getBody() {
        return _body;
    }

    public int getScore() {
        return _score;
    }

    public void move(Point d) {
        for (int i = _body.size() - 1; i >= 1; --i) {
            var b = _body.get(i);
            var next = _body.get(i - 1);

            b.move(next.x, next.y);
        }

        _body.get(0).move(d.x, d.y);
    }
}
