package ru.nsu.belousova.server.game;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nsu.belousova.server.game.model.Field;
import ru.nsu.belousova.server.game.model.GameProperties;
import ru.nsu.belousova.server.game.model.Worm;
import ru.nsu.belousova.server.network.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Map.entry;

public class GameManager extends Thread {
    private final ArrayList<Player> _players;
    private final Field _field;
    private final int SCORE_fOR_WIN;
    private boolean _isRun;
    private final Object _wormsAccessMonitor = new Object();

    public GameManager() {
        int width = ThreadLocalRandom.current().nextInt(GameProperties.MIN_WIDTH, GameProperties.MAX_WIDTH + 1);
        int height = ThreadLocalRandom.current().nextInt(GameProperties.MIN_HEIGHT, GameProperties.MAX_HEIGHT + 1);

        _field = new Field(width, height);
        _players = new ArrayList<>();

        SCORE_fOR_WIN = width * height / (GameProperties.START_WORM_LENGTH + 5);

        _isRun = true;
    }

    public void run() {
        boolean correctEnd = false;

        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                if (countLive() == 0) {
                    _isRun = false;
                    break;
                }

                if (checkWin()) {
                    _isRun = false;
                    correctEnd = true;
                    break;
                }

                makeStep();

                sendGameData();

                removeDead();
            }

            try {
                Thread.sleep(1000 - GameProperties.GAME_SPEED);
            } catch (InterruptedException ignore) {
            }
        }

        synchronized (this) {
            _players.forEach(Player::disconnect);
        }
    }

    private void removeDead() {
        List<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < _field.getWorms().size(); ++i) {
            var worm = _field.getWorms().get(i);

            if (!worm.isLive() || _players.get(i).isInterrupted()) {
                toRemove.add(i);
            }
        }

        toRemove.forEach(e -> {
            int index = e;
            _field.getWorms().remove(index);
            _players.get(index).disconnect();
            _players.remove(index);
        });
    }

    private void makeStep() {
        synchronized (_wormsAccessMonitor) {
            // Проверка ситуации, когда червяки одновременно ударились головами (все померли)
            for (int i = 0; i < _field.getWorms().size(); ++i) {
                Worm worm1 = _field.getWorms().get(i);
                Point head1 = worm1.getBody().get(0);

                for (int j = i + 1; j < _field.getWorms().size(); ++j) {
                    Worm worm2 = _field.getWorms().get(j);
                    Point head2 = worm2.getBody().get(0);

                    if (head1.x + worm1.getDirection().x == head2.x + worm2.getDirection().x &&
                            head1.y + worm1.getDirection().y == head2.y + worm2.getDirection().y) {
                        worm2.increaseScore(GameProperties.SCORE_FOR_KILL);
                        worm1.kill();

                        worm1.increaseScore(GameProperties.SCORE_FOR_KILL);
                        worm2.kill();
                    }
                }
            }

            // Проверка ситуации, когда червяк ударился в тело другого (помер один)
            for (var worm1 : _field.getWorms()) {
                Point head = worm1.getBody().get(0);
                Point nextPointOfHead = new Point(head.x + worm1.getDirection().x, head.y + worm1.getDirection().y);

                for (var worm2 : _field.getWorms()) {
                    int index = worm2.getBody().lastIndexOf(nextPointOfHead);

                    if (index != -1 && index != worm2.getBody().size() - 1) {
                        worm2.increaseScore(GameProperties.SCORE_FOR_KILL);
                        worm1.kill();
                    }
                }

                if (nextPointOfHead.x < 0 || nextPointOfHead.x >= _field.getWidth() || nextPointOfHead.y < 0 || nextPointOfHead.y >= _field.getHeight())
                    worm1.kill();
            }

            // Проверка ситуации, когда один червяк съедает еду, удлиняется, а второй врезается ему в хвост (помер один)
            for (var worm1 : _field.getWorms()) {
                Point head1 = worm1.getBody().get(0);
                Point nextPointOfHead1 = new Point(head1.x + worm1.getDirection().x, head1.y + worm1.getDirection().y);
                int index = _field.getFood().lastIndexOf(nextPointOfHead1);

                if (index != -1) {
                    Point tail = worm1.getBody().get(worm1.getBody().size() - 1);

                    for (var worm2 : _field.getWorms()) {
                        Point head2 = worm2.getBody().get(0);
                        Point nextPointOfHead2 = new Point(head2.x + worm2.getDirection().x, head2.y + worm2.getDirection().y);

                        if (tail.equals(nextPointOfHead2)) {
                            worm1.increaseScore(GameProperties.SCORE_FOR_KILL);
                            worm2.kill();
                        }

                    }
                }
            }

            // Теперь можно ходить и есть
            for (var worm : _field.getWorms()) {
                if (worm.isLive()) {
                    Point head = worm.getBody().get(0);
                    Point nextPointOfHead = new Point(head.x + worm.getDirection().x, head.y + worm.getDirection().y);

                    int index = _field.getFood().lastIndexOf(nextPointOfHead);

                    if (index != -1) {
                        worm.eat();
                        worm.increaseScore(1);
                        _field.getFood().remove(index);
                    }

                    worm.move();
                }
            }
        }

        for (int i = 0; i < GameProperties.COUNT_FOODS - _field.getFood().size(); ++i)
            _field.addFood();
    }

    private void sendGameData() {
        var data = gameModelToJSON();

        for (int i = 0; i < _players.size(); ++i) {
            var player = _players.get(i);

            data.put("PLAYER", i);

            player.sendUpdates(data);
        }
    }

    private boolean checkWin() {
        boolean win = false;

        for (var worm : _field.getWorms())
            if (worm.getScore() >= SCORE_fOR_WIN) {
                win = true;
                break;
            }

        return win;
    }

    private JSONObject gameModelToJSON() {
        JSONObject data = new JSONObject(Map.ofEntries(
                entry("HEIGHT", _field.getHeight()),
                entry("WIDTH", _field.getWidth())
        ));

        JSONArray array = new JSONArray();

        for (int i = 0; i < _players.size(); ++i) {
            Worm worm = _field.getWorms().get(i);
            JSONArray arrayBody = new JSONArray();

            for (var b : worm.getBody()) {
                arrayBody.add(Map.of("x", b.x, "y", b.y));
            }

            array.add(Map.of("NAME", worm.getName(), "SCORE", worm.getScore(), "WORM", arrayBody, "DEAD", !worm.isLive()));
        }

        data.put("WORMS", array);

        JSONArray foods = new JSONArray();

        for (var p : _field.getFood()) {
            foods.add(Map.of("x", p.x, "y", p.y));
        }

        data.put("FOODS", foods);

        return data;
    }

    public synchronized boolean addPlayer(Player player) {
        Worm worm;

        if (!_isRun)
            return false;

        if (_players.size() == GameProperties.MAX_PLAYERS)
            return false;

        if ((worm = _field.createWorm(_wormsAccessMonitor)) != null) {
            player.registerWorm(worm);
            _players.add(player);

            return true;
        }

        return false;
    }

    public synchronized boolean gameIsRun() {
        return _isRun;
    }

    private int countLive() {
        int count = 0;

        for (var wp : _players)
            if (wp.isLive())
                count++;

        return count;
    }
}
