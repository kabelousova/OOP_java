package ru.nsu.belousova.client.gui.views;

import ru.nsu.belousova.client.control.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class StartPane extends JPanel {
    private final Image _background;

    public StartPane(Controller controller) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        _background = ImageIO.read(new File(classloader.getResource("background.png").getFile()));

        initComponents(controller);
    }

    private void initComponents(Controller controller) {
        JPanel tmp = new JPanel();

        JLabel usernameLabel = new JLabel("Enter username");
        JLabel statusLabel = new JLabel("");
        JButton playButton = new JButton("Play");
        JTextField usernameField = new JTextField();

        Font font = new Font("SansSerif", Font.PLAIN, 20);
        usernameLabel.setFont(font);
        usernameLabel.setHorizontalAlignment(JLabel.CENTER);
        usernameField.setFont(font);

        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        playButton.addActionListener(e -> {
            var username = usernameField.getText();

            if (username != null && !username.isEmpty()) {
                statusLabel.setText("");

                new SwingWorker<>() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        controller.startGame(username);

                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                        } catch (ExecutionException ex) {
                            SwingUtilities.invokeLater(() -> {
                                statusLabel.setText("Connection lost");
                            });
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }.execute();
            } else
                statusLabel.setText("Empty username!");
        });

        tmp.setLayout(new GridLayout(4, 1, 10, 10));
        tmp.add(usernameLabel);
        tmp.add(usernameField);
        tmp.add(playButton);
        tmp.add(statusLabel);

        tmp.setPreferredSize(new Dimension(200, 160));
        tmp.setMinimumSize(new Dimension(200, 160));
        tmp.setMaximumSize(new Dimension(200, 160));
        tmp.setOpaque(false);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        add(Box.createHorizontalGlue());
        add(tmp);
        add(Box.createHorizontalGlue());

        setPreferredSize(new Dimension(_background.getWidth(null), _background.getHeight(null)));
        setMaximumSize(new Dimension(_background.getWidth(null), _background.getHeight(null)));
        setMinimumSize(new Dimension(_background.getWidth(null), _background.getHeight(null)));
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(_background, 0, 0, this);
    }
}
