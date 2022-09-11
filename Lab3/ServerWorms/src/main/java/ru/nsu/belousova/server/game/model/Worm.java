package ru.nsu.belousova.server.game.model;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Worm implements PropertyChangeListener {
    private final ArrayList<Point> _body;
    private Point _direction;
    private String _name;
    private int _score;
    private final Object _monitor;
    private boolean _live;
    private final Map<Integer, Point> _directions = Map.of(0, new Point(0, -1),
            1, new Point(1, 0),
            2, new Point(0, 1),
            3, new Point(-1, 0)
    );

    public Worm(Point startPoint, Object monitor) {
        _name = UUID.randomUUID().toString();
        _monitor = monitor;

        _body = new ArrayList<>();
        _score = 0;
        _live = true;

        for (int i = GameProperties.START_WORM_LENGTH - 1; i >= 0; --i)
            _body.add(new Point(startPoint.x + i, startPoint.y));

        _direction = _directions.get(1);
    }

    public ArrayList<Point> getBody() {
        return _body;
    }

    public int getScore() {
        return _score;
    }

    public void move() {
        Point head = _body.get(0);
        Point nextPointOfHead = new Point(head.x + _direction.x, head.y + _direction.y);

        for (int i = _body.size() - 1; i >= 1; --i) {
            var b = _body.get(i);
            var next = _body.get(i - 1);

            b.move(next.x, next.y);
        }

        _body.get(0).move(nextPointOfHead.x, nextPointOfHead.y);
    }

    public void kill() {
        _live = false;
    }

    public boolean isLive() {
        return _live;
    }

    public Point getDirection() {
        return _direction;
    }

    public void increaseScore(int points) {
        _score += points;
    }

    public void eat() {
        Point tail = _body.get(_body.size() - 1);
        _body.add(new Point(tail.x, tail.y));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        synchronized (_monitor) {
            if (evt.getPropertyName().equals("USERNAME")) {
                if (evt.getNewValue() != null)
                    _name = (String) evt.getNewValue();
            } else {
                var nd = _directions.get(((Long) evt.getNewValue()).intValue());

                if (nd.x != _direction.x && nd.y != -_direction.y)
                    _direction = nd;
            }
        }
    }

    public Object getName() {
        return _name;
    }
}
