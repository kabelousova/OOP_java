package ru.nsu.belousova.server;

import ru.nsu.belousova.server.network.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(args[0]);
            server.run();
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
