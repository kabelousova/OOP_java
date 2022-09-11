package ru.nsu.belousova.client.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nsu.belousova.client.utils.Pair;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class GameField {
    private ArrayList<Point> _foods;
    private ArrayList<Worm> _worms;
    private ArrayList<Pair<String, Integer>> _scoreBoard;
    private long _currPlayer;
    private long _width;
    private long _height;
    private final PropertyChangeSupport _support;

    public GameField() {
        _support = new PropertyChangeSupport(this);

        _width = 0;
        _height = 0;

        _foods = new ArrayList<>();
        _worms = new ArrayList<>();
        _scoreBoard = new ArrayList<>();

        _currPlayer = 0;
    }

    public void update(JSONObject data) {
        _height = (Long) data.get("HEIGHT");
        _width = (Long) data.get("WIDTH");

        _currPlayer = (Long) data.get("PLAYER");

//        var foods = (JSONArray) data.get("FOODS");
//        for (int i = 0; i < foods.size(); ) {
//            var curFood = _foods.get(i);
//            var newFood = (JSONObject) foods.get(i);
//
//            int x = Integer.parseInt((String) newFood.get("x"));
//            int y = Integer.parseInt((String) newFood.get("y"));
//
//            if (curFood == null) {
//                _foods.add(new Point(x, y));
//            }
//        }

//        _support.firePropertyChange("GAME", null, this);
    }
}
