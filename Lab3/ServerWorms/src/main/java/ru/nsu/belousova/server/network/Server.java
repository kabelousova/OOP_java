package ru.nsu.belousova.server.network;

import ru.nsu.belousova.server.game.GameManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final ServerSocket _server;
    private final ArrayList<GameManager> _managers;
    private final Thread _deleteThread;
    private final Thread _acceptThread;


    public Server(String address) throws IOException {
        _server = new ServerSocket();
        _managers = new ArrayList<>();

        _acceptThread = Thread.currentThread();
        _deleteThread = new Thread(this::deleteStoppedGames);

        _server.bind(new InetSocketAddress(address, 8000));
    }

    public void run() throws IOException {
        _deleteThread.start();

        while (!Thread.currentThread().isInterrupted()) {
            Socket client = _server.accept();
            Player player = new Player(client);

            synchronized (_managers) {
                boolean added = false;

                for (var manager : _managers) {
                    added = manager.addPlayer(player);

                    if (added) {
                        System.out.println("Add new Player");
                        break;
                    }
                }

                if (!added) {
                    System.out.println("Create new game for new Player");
                    GameManager newManager = new GameManager();
                    newManager.addPlayer(player);

                    _managers.add(newManager);
                    newManager.start();
                }

                player.start();
            }
        }

        for (var manager : _managers) {
            manager.interrupt();
        }

        _deleteThread.interrupt();
    }

    private void deleteStoppedGames() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (_managers) {
                _managers.removeIf(GameManager::gameIsRun);
            }

            try {
                Thread.sleep(100000);
            } catch (InterruptedException ignore) {
            }
        }

        _acceptThread.interrupt();
    }
}
