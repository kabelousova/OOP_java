package ru.nsu.belousova.client.gui.views;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nsu.belousova.client.control.Controller;
import ru.nsu.belousova.client.utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

public class GameArea extends JPanel implements PropertyChangeListener {
    private final ArrayList<Pair<JLabel, JLabel>> _scoreBoard;
    private final GameField _gameField;

    public GameArea(Controller controller) throws IOException {
        _scoreBoard = new ArrayList<>();
        _gameField = new GameField();

        initComponents(controller);
    }

    private void initComponents(Controller controller) {
        JPanel leftPanel = new JPanel();
        JPanel scoreBoard = new JPanel(new GridLayout(11, 1, 20, 20));
        JLabel scoreLabel = new JLabel("Score");
        Button backButton = new Button("Exit");
        Font font = new Font("SansSerif", Font.PLAIN, 20);
        Color textColor = new Color(200, 200, 200);

        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setFont(font);
        scoreLabel.setForeground(textColor);

        scoreBoard.add(scoreLabel);
        scoreBoard.setOpaque(false);

        for (int i = 0; i < 10; ++i) {
            JLabel playerName = new JLabel("Name");
            JLabel playerScore = new JLabel("" + i);
            JPanel horizontalRow = new JPanel();

            playerName.setFont(font);
            playerName.setForeground(textColor);
            playerScore.setFont(font);
            playerScore.setForeground(textColor);

            horizontalRow.setLayout(new BoxLayout(horizontalRow, BoxLayout.X_AXIS));
            horizontalRow.setOpaque(false);

            horizontalRow.add(playerName);
            horizontalRow.add(Box.createHorizontalGlue());
            horizontalRow.add(playerScore);

            scoreBoard.add(horizontalRow);

            _scoreBoard.add(new Pair<>(playerName, playerScore));
        }

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        leftPanel.setOpaque(false);

        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setMinimumSize(new Dimension(100, 40));
        backButton.setMaximumSize(new Dimension(100, 40));

        backButton.addActionListener(e -> {
            controller.stopGame();
        });

        leftPanel.add(scoreBoard);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(backButton);

        leftPanel.setMinimumSize(new Dimension(300, 400));
        leftPanel.setPreferredSize(new Dimension(300, 900));

        setBackground(new Color(50, 50, 50));
        setLayout(new BorderLayout());

        add(leftPanel, BorderLayout.EAST);
        add(_gameField, BorderLayout.WEST);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                new SwingWorker<>() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        controller.sendStep(String.valueOf(e.getKeyChar()));
                        return null;
                    }
                }.execute();
            }
        });

        setFocusable(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        JSONObject data = (JSONObject) evt.getNewValue();

        SwingUtilities.invokeLater(() -> {
            JSONArray worms = (JSONArray) data.get("WORMS");
            int count = worms.size();

            for (int i = 0; i < count; ++i) {
                var pair = _scoreBoard.get(i);
                var worm = (JSONObject) worms.get(i);

                pair.getKey().setText((String) worm.get("NAME"));
                pair.getValue().setText("" + worm.get("SCORE"));
            }

            for (int i = count; i < _scoreBoard.size(); ++i) {
                var pair = _scoreBoard.get(i);

                pair.getKey().setText("");
                pair.getValue().setText("");
            }

            _gameField.update(data);
        });
    }
}
