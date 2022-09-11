package ru.nsu.belousova.server.network;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.nsu.belousova.server.game.model.Worm;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Player extends Thread{
    private final Socket _socket;
    private final PropertyChangeSupport _support;

    public Player(Socket socket) {
        _support = new PropertyChangeSupport(this);
        _socket = socket;
    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                JSONObject data = getMessage();

                if (data != null) {
                    String name = data.get("USERNAME") != null ? "USERNAME" : "DIRECTION";
                    Object value = data.get(name);

                    _support.firePropertyChange(name, null, value);
                }
            }
        } catch (IOException exception) {
            disconnect();
        }
    }

    private JSONObject getMessage() throws IOException {
        InputStream input = _socket.getInputStream();
        byte[] buffer = new byte[1024];

        int count = input.read(buffer);
        JSONParser parser = new JSONParser();

        if (count == -1) {
            disconnect();
            return null;
        }

        try {
            return (JSONObject) parser.parse(new String(buffer, 0, count));
        } catch (ParseException ignore) {
        }

        return null;
    }

    public synchronized void disconnect() {
        this.interrupt();

        try {
            _socket.close();
        } catch (IOException ignore) {
        }
    }

    public synchronized boolean isLive() {
        return !_socket.isClosed();
    }

    public void sendUpdates(Object data) {
        if (isLive()) {
            try {
                OutputStream output = _socket.getOutputStream();

                output.write(data.toString().getBytes());
            } catch (IOException e) {
                disconnect();
            }
        }
    }

    public void registerWorm(Worm worm) {
        _support.addPropertyChangeListener(worm);
    }
}
