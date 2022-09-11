package ru.nsu.belousova.LogoWorld;

import ru.nsu.belousova.LogoWorld.commands.Command;
import ru.nsu.belousova.LogoWorld.models.World;
import ru.nsu.belousova.LogoWorld.ui.WorldPrinter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Simulator {
    private final CommandLoader _loader;
    private final World _world;
    private final BufferedReader _reader;

    Simulator(String filename) throws IOException {
        _reader = new BufferedReader(new FileReader(filename));
        _loader = new CommandLoader();
        _world = new World();
    }

    public void run() throws IOException {
        String line;

        while ((line = _reader.readLine()) != null) {
            if (_world.getField() != null)
                WorldPrinter.print(_world);

            String[] parts = line.split("\\s+");

            String commandName = parts[0];
            String[] args = new String[parts.length - 1];

            System.arraycopy(parts, 1, args, 0, parts.length - 1);

            Command command = _loader.getCommand(commandName);
            command.execute(_world, args);

            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
