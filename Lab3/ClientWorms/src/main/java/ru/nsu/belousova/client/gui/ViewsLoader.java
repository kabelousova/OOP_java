package ru.nsu.belousova.client.gui;

import ru.nsu.belousova.client.control.Controller;
import ru.nsu.belousova.client.gui.views.GameArea;
import ru.nsu.belousova.client.gui.views.StartPane;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

public class ViewsLoader {
    private final HashMap<String, JPanel> _dictionary;

    public ViewsLoader(Controller controller) throws IOException {
        _dictionary = new HashMap<>();

        _dictionary.put("START", new StartPane(controller));
        var area = new GameArea(controller);
        _dictionary.put("GAME", area);

        controller.register(area);
    }

    public JPanel getPanel(String name) {
        return _dictionary.get(name);
    }
}
