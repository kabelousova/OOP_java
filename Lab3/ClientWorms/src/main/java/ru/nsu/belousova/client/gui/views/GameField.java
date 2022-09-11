package ru.nsu.belousova.client.gui.views;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameField extends JPanel {
    private int _height;
    private int _width;
    private int _currPlayer;
    private ArrayList<ArrayList<Point>> _worms;
    private ArrayList<Point> _foods;
    private Image _background;
    private Image _apple;
    private final HashMap<Point, Image> _headsGreen;
    private final HashMap<Point, Image> _headsLime;
    private final HashMap<Point, Image> _tailsGreen;
    private final HashMap<Point, Image> _tailsLime;
    private final HashMap<Point, Image> _bodiesGreen;
    private final HashMap<Point, Image> _bodiesLime;
    private final HashMap<Point, Image> _anglesGreen;
    private final HashMap<Point, Image> _anglesLime;

    public GameField() throws IOException {
        setMaximumSize(new Dimension(1500, 1000));
        setPreferredSize(new Dimension(1500, 1000));
        setMinimumSize(new Dimension(1500, 1000));
        setOpaque(false);

        _height = 0;
        _width = 0;

        _currPlayer = 0;
        _worms = new ArrayList<>();
        _foods = new ArrayList<>();

        _headsGreen = new HashMap<>();
        _headsLime = new HashMap<>();

        _tailsGreen = new HashMap<>();
        _tailsLime = new HashMap<>();

        _bodiesGreen = new HashMap<>();
        _bodiesLime = new HashMap<>();

        _anglesGreen = new HashMap<>();
        _anglesLime = new HashMap<>();

        loadPictures();
    }

    private void loadPictures() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        _background = ImageIO.read(new File(classloader.getResource("backgroundGame.jpg").getFile()));
        _apple = ImageIO.read(new File(classloader.getResource("apple.jpg").getFile()));

        _headsGreen.put(new Point(-1, 0), ImageIO.read(new File(classloader.getResource("headGreen1.jpg").getFile())));
        _headsGreen.put(new Point(0, -1), ImageIO.read(new File(classloader.getResource("headGreen2.jpg").getFile())));
        _headsGreen.put(new Point(1, 0), ImageIO.read(new File(classloader.getResource("headGreen3.jpg").getFile())));
        _headsGreen.put(new Point(0, 1), ImageIO.read(new File(classloader.getResource("headGreen4.jpg").getFile())));

        _headsLime.put(new Point(-1, 0), ImageIO.read(new File(classloader.getResource("headLime1.jpg").getFile())));
        _headsLime.put(new Point(0, -1), ImageIO.read(new File(classloader.getResource("headLime2.jpg").getFile())));
        _headsLime.put(new Point(1, 0), ImageIO.read(new File(classloader.getResource("headLime3.jpg").getFile())));
        _headsLime.put(new Point(0, 1), ImageIO.read(new File(classloader.getResource("headLime4.jpg").getFile())));

        _tailsGreen.put(new Point(1, 0), ImageIO.read(new File(classloader.getResource("tailGreen1.jpg").getFile())));
        _tailsGreen.put(new Point(0, 1), ImageIO.read(new File(classloader.getResource("tailGreen2.jpg").getFile())));
        _tailsGreen.put(new Point(-1, 0), ImageIO.read(new File(classloader.getResource("tailGreen3.jpg").getFile())));
        _tailsGreen.put(new Point(0, -1), ImageIO.read(new File(classloader.getResource("tailGreen4.jpg").getFile())));

        _tailsLime.put(new Point(1, 0), ImageIO.read(new File(classloader.getResource("tailLime1.jpg").getFile())));
        _tailsLime.put(new Point(0, 1), ImageIO.read(new File(classloader.getResource("tailLime2.jpg").getFile())));
        _tailsLime.put(new Point(-1, 0), ImageIO.read(new File(classloader.getResource("tailLime3.jpg").getFile())));
        _tailsLime.put(new Point(0, -1), ImageIO.read(new File(classloader.getResource("tailLime4.jpg").getFile())));

        _bodiesGreen.put(new Point(0, 2), ImageIO.read(new File(classloader.getResource("bodyGreen1.jpg").getFile())));
        _bodiesGreen.put(new Point(0, -2), ImageIO.read(new File(classloader.getResource("bodyGreen1.jpg").getFile())));
        _bodiesGreen.put(new Point(2, 0), ImageIO.read(new File(classloader.getResource("bodyGreen2.jpg").getFile())));
        _bodiesGreen.put(new Point(-2, 0), ImageIO.read(new File(classloader.getResource("bodyGreen2.jpg").getFile())));

        _bodiesLime.put(new Point(0, 2), ImageIO.read(new File(classloader.getResource("bodyLime1.jpg").getFile())));
        _bodiesLime.put(new Point(0, -2), ImageIO.read(new File(classloader.getResource("bodyLime1.jpg").getFile())));
        _bodiesLime.put(new Point(2, 0), ImageIO.read(new File(classloader.getResource("bodyLime2.jpg").getFile())));
        _bodiesLime.put(new Point(-2, 0), ImageIO.read(new File(classloader.getResource("bodyLime2.jpg").getFile())));

        _anglesGreen.put(new Point(1, -1), ImageIO.read(new File(classloader.getResource("angleGreen1.jpg").getFile())));
        _anglesGreen.put(new Point(1, 1), ImageIO.read(new File(classloader.getResource("angleGreen2.jpg").getFile())));
        _anglesGreen.put(new Point(-1, 1), ImageIO.read(new File(classloader.getResource("angleGreen3.jpg").getFile())));
        _anglesGreen.put(new Point(-1, -1), ImageIO.read(new File(classloader.getResource("angleGreen4.jpg").getFile())));

        _anglesLime.put(new Point(1, -1), ImageIO.read(new File(classloader.getResource("angleLime1.jpg").getFile())));
        _anglesLime.put(new Point(1, 1), ImageIO.read(new File(classloader.getResource("angleLime2.jpg").getFile())));
        _anglesLime.put(new Point(-1, 1), ImageIO.read(new File(classloader.getResource("angleLime3.jpg").getFile())));
        _anglesLime.put(new Point(-1, -1), ImageIO.read(new File(classloader.getResource("angleLime4.jpg").getFile())));
    }

    @Override
    public void paintComponent(Graphics graphics) {
        int startX = (getWidth() - 30 * _width) / 2;
        int startY = (getHeight() - 30 * _height) / 2;

        for (int i = 0; i < _height; ++i) {
            for (int j = 0; j < _width; ++j) {
                graphics.drawImage(_background, startX + 30 * j, startY + 30 * i, this);
            }
        }

        for (var point : _foods) {
            graphics.drawImage(_apple, startX + 30 * point.x, startY + 30 * point.y, this);
        }

        for (int i = 0; i < _worms.size(); ++i) {
            var worm = _worms.get(i);

            HashMap<Point, Image> heads = _headsGreen;
            HashMap<Point, Image> bodies = _bodiesGreen;
            HashMap<Point, Image> angles = _anglesGreen;
            HashMap<Point, Image> tails = _tailsGreen;

            if (i == _currPlayer) {
                heads = _headsLime;
                bodies = _bodiesLime;
                angles = _anglesLime;
                tails = _tailsLime;
            }

            var head = worm.get(0);
            var next = worm.get(1);
            graphics.drawImage(heads.get(new Point(head.x - next.x, head.y - next.y)), startX + 30 * head.x, startY + 30 * head.y, this);

            for (int j = 1; j < worm.size() - 1; ++j) {
                var p = worm.get(j - 1);
                var b = worm.get(j);
                var n = worm.get(j + 1);

                Image toDraw;

                if (p.x - n.x == 0 || p.y - n.y == 0) {
                    toDraw = bodies.get(new Point(n.x - p.x, n.y - p.y));
                } else {
                    toDraw = angles.get(new Point(p.x + n.x - 2 * b.x, p.y + n.y - 2 * b.y));
                }

                graphics.drawImage(toDraw, startX + 30 * b.x, startY + 30 * b.y, this);
            }

            var tail = worm.get(worm.size() - 1);
            var prev = worm.get(worm.size() - 2);

            graphics.drawImage(tails.get(new Point(tail.x - prev.x, tail.y - prev.y)), startX + 30 * tail.x, startY + 30 * tail.y, this);
        }
    }

    public void update(JSONObject data) {
        _currPlayer = ((Long) data.get("PLAYER")).intValue();
        _width = ((Long) data.get("WIDTH")).intValue();
        _height = ((Long) data.get("HEIGHT")).intValue();

        _foods = new ArrayList<>();
        _worms = new ArrayList<>();

        for (var food : (JSONArray) data.get("FOODS")) {
            JSONObject f = (JSONObject) food;
            _foods.add(new Point(((Long) f.get("x")).intValue(), ((Long) f.get("y")).intValue()));
        }

        var worms = (JSONArray) data.get("WORMS");

        for (int i = 0; i < worms.size(); ++i) {
            JSONObject w = (JSONObject) worms.get(i);


            if ((boolean) w.get("DEAD") && i == _currPlayer) {
                JOptionPane.showMessageDialog(this, "Your worm died!");
            }

            ArrayList<Point> wormList = new ArrayList<>();
            _worms.add(wormList);

            for (var body : (JSONArray) w.get("WORM")) {
                JSONObject b = (JSONObject) body;
                wormList.add(new Point(((Long) b.get("x")).intValue(), ((Long) b.get("y")).intValue()));
            }
        }

        repaint();
    }
}
