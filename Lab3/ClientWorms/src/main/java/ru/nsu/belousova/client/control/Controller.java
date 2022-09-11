package ru.nsu.belousova.client.control;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.nsu.belousova.client.gui.GameFrame;
import ru.nsu.belousova.client.gui.ViewsLoader;
import ru.nsu.belousova.client.gui.views.GameArea;

import javax.swing.*;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

public class Controller {
    private final GameFrame _mainWindow;
    private Socket _socket;
    private final ViewsLoader _loader;
    private Thread _thread = null;
    private final Map<String, Integer> _ways;
    private final PropertyChangeSupport _support;

    public Controller(GameFrame frame) throws IOException {
        _mainWindow = frame;
        _support = new PropertyChangeSupport(this);
        _loader = new ViewsLoader(this);

        _ways = Map.of("w", 0, "d", 1, "s", 2, "a", 3);
    }

    public void startGame(String userName) throws IOException {
        try {
            _socket = new Socket();
            _socket.connect(new InetSocketAddress("192.168.0.22", 8000), 10000);
            _thread = Thread.currentThread();

            var output = _socket.getOutputStream();

            JSONObject request = new JSONObject(Map.of("USERNAME", userName));
            output.write(request.toJSONString().getBytes());

            SwingUtilities.invokeLater(() -> _mainWindow.setPanel(_loader.getPanel("GAME")));

            while (!Thread.currentThread().isInterrupted()) {
                JSONObject data = getMessage();

                if (data != null) {
                    if (data.get("WIN") != null) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, data.get("WIN")));
                        break;
                    } else {
                        _support.firePropertyChange("DATA", null, data);
                    }
                }
            }
        } finally {
            SwingUtilities.invokeLater(() -> _mainWindow.setPanel(_loader.getPanel("START")));

            _socket.close();
        }
    }

    private JSONObject getMessage() throws IOException {
        InputStream input = _socket.getInputStream();
        byte[] buffer = new byte[1024];

        int count = input.read(buffer);
        JSONParser parser = new JSONParser();

        try {
            return (JSONObject) parser.parse(new String(buffer, 0, count));
        } catch (ParseException ignore) {
        }

        return null;
    }

    public void stopGame() {
        _thread.interrupt();
    }

    public void sendStep(String way) {
        try {
            if (_ways.containsKey(way)) {
                JSONObject data = new JSONObject();
                data.put("DIRECTION", _ways.get(way));

                _socket.getOutputStream().write(data.toString().getBytes());
            }
        } catch (IOException e) {
            stopGame();
        }
    }

    public void start() {
        _mainWindow.setPanel(_loader.getPanel("START"));
        _mainWindow.setVisible(true);
    }

    public void register(GameArea area) {
        _support.addPropertyChangeListener(area);
    }
}
