package ru.nsu.belousova.server.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nsu.belousova.server.network.Player;
import ru.nsu.belousova.server.utils.Pair;

import java.awt.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Map.entry;

public class GameManager {
    private final ArrayList<Pair<Worm, Player>> _wormPlayerPairs;
    private final ArrayList<Point> _food;
    private final int _width;
    private final int _height;
    private final int SCORE_fOR_WIN;
    private final Thread _mainThread;
    private boolean _isRun;
    private Map<Integer, Point> _dictionary = Map.of(0, new Point(0, -1),
            1, new Point(1, 0),
            2, new Point(0, 1),
            3, new Point(-1, 0)
    );

    public GameManager() {
        _width = ThreadLocalRandom.current().nextInt(GameProperties.MIN_WIDTH, GameProperties.MAX_WIDTH + 1);
        _height = ThreadLocalRandom.current().nextInt(GameProperties.MIN_HEIGHT, GameProperties.MAX_HEIGHT + 1);

        SCORE_fOR_WIN = _width * _height / 50;

        _wormPlayerPairs = new ArrayList<>();
        _food = new ArrayList<>();

        _mainThread = new Thread(this::run);

        _isRun = true;
    }

    public void run() {
        boolean correctEnd = false;

        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                if (countLive() == 0)
                    break;

                if (checkWin()) {
                    correctEnd = true;
                    break;
                }

                makeStep();

                sendGameData();
            }

            try {
                Thread.sleep(1000 - GameProperties.GAME_SPEED);
            } catch (InterruptedException ignore) {
            }
        }

        if (correctEnd) {

        }

        synchronized (this) {
            _isRun = false;
            _wormPlayerPairs.forEach(wp -> wp.getValue().disconnect());
        }
    }

    private void makeStep() {
        boolean[][] tmp = new boolean[_height][_width];

        for (var wp : _wormPlayerPairs) {
            for (var p : wp.getKey().getBody()) {
                tmp[p.y][p.x] = true;
            }
        }

        for (var wp : _wormPlayerPairs) {
            var d = _dictionary.get(wp.getValue().getWay());
            if (wp.getKey() != null) {
                var head = wp.getKey().getBody().get(0);
                Point nextPoint = new Point(((head.x + d.x) % _width + _width) % _width, ((head.y + d.y) % _height + _height) % _height);

                if (!tmp[nextPoint.y][nextPoint.x])
                    wp.getKey().move(nextPoint);
            }
        }
    }

    private void sendGameData() {
        var data = gameModelToJSON();

        for (int i = 0; i < _wormPlayerPairs.size(); ++i) {
            var wp = _wormPlayerPairs.get(i);

            data.put("PLAYER", i);
            System.out.println(data);
            wp.getValue().sendMessage(data);
        }
    }

    private boolean checkWin() {
        boolean win = false;

        for (var wp : _wormPlayerPairs)
            if (wp.getKey().getScore() >= SCORE_fOR_WIN) {
                win = true;
                break;
            }

        return win;
    }

    private JSONObject gameModelToJSON() {
        JSONObject data = new JSONObject(Map.ofEntries(
                entry("HEIGHT", _height),
                entry("WIDTH", _width)
        ));

        JSONArray array = new JSONArray();

        for (var wp : _wormPlayerPairs) {
            JSONArray arrayBody = new JSONArray();

            for (var b : wp.getKey().getBody()) {
                arrayBody.add(Map.of("x", b.x, "y", b.y));
            }

            array.add(Map.of("USERNAME", wp.getValue().getUserName(), "SCORE", 1, "WORM", arrayBody));
        }

        data.put("WORMS", array);

        JSONArray foods = new JSONArray();

        for (var p : _food) {
            foods.add(Map.of("x", p.x, "y", p.y));
        }

        data.put("FOODS", foods);

        return data;
    }

    public synchronized boolean addPlayer(Socket client) {
        Point point;

        if (!_isRun)
            return false;

        if (_wormPlayerPairs.size() == GameProperties.MAX_PLAYERS)
            return false;

        if ((point = findPlace()) != null) {
            Worm worm = new Worm(point.x, point.y);
            _wormPlayerPairs.add(new Pair<>(worm, new Player(client)));

            if (_wormPlayerPairs.size() == 1)
                _mainThread.start();

            return true;
        }

        return false;
    }

    private synchronized Point findPlace() {
        Point point = null;

        boolean[][] tmp = new boolean[_height][_width];
        boolean flag = true;

        for (var wp : _wormPlayerPairs) {
            for (var p : wp.getKey().getBody()) {
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
                    point = new Point(x + 2, y);
                    break;
                }
            }
            if (flag)
                break;
        }

        return point;
    }

    public synchronized boolean gameIsRun() {
        return _isRun;
    }

    public void stop() {
        _mainThread.interrupt();
    }

    private int countLive() {
        int count = 0;

        for (var wp : _wormPlayerPairs)
            if (wp.getValue().isLive())
                count++;

        return count;
    }
}
