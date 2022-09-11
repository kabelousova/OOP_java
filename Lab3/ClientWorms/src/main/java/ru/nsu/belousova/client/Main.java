package ru.nsu.belousova.client;

import ru.nsu.belousova.client.control.Controller;
import ru.nsu.belousova.client.gui.GameFrame;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        GameFrame frame = new GameFrame();
        Controller controller = new Controller(frame);

        controller.start();
    }
}
