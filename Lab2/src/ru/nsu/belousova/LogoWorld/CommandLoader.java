package ru.nsu.belousova.LogoWorld;

import ru.nsu.belousova.LogoWorld.commands.Command;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

public class CommandLoader {
    private final HashMap<String, Command> _dictionary;

    CommandLoader() throws IOException {
        Properties properties = new Properties();
        _dictionary = new HashMap<>();

        InputStream input = getClass().getClassLoader().getResourceAsStream("properties.properties");

        properties.load(input);

        properties.forEach((key, value) -> {

            try {
                _dictionary.put((String) key, (Command) Class.forName(Command.class.getPackageName() + "." + value).getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public Command getCommand(String name) {
        return _dictionary.get(name);
    }
}