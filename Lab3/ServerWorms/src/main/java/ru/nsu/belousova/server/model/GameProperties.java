package ru.nsu.belousova.server.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GameProperties {
    public static final int MAX_PLAYERS;
    public static final int MIN_WIDTH;
    public static final int MAX_WIDTH;
    public static final int MIN_HEIGHT;
    public static final int MAX_HEIGHT;
    public static final int START_WORM_LENGTH;
    public static final int GAME_SPEED;


    static {
        Properties properties = new Properties();

        InputStream input = GameProperties.class.getClassLoader().getResourceAsStream("properties.properties");

        try {
            properties.load(input);

            MAX_PLAYERS = Integer.parseInt(properties.getProperty("MAX_PLAYERS"));
            MIN_WIDTH = Integer.parseInt(properties.getProperty("MIN_WIDTH"));
            MAX_WIDTH = Integer.parseInt(properties.getProperty("MAX_WIDTH"));
            MIN_HEIGHT = Integer.parseInt(properties.getProperty("MAX_HEIGHT"));
            MAX_HEIGHT = Integer.parseInt(properties.getProperty("MAX_HEIGHT"));
            START_WORM_LENGTH = Integer.parseInt(properties.getProperty("START_WORM_LENGTH"));
            GAME_SPEED = Integer.parseInt(properties.getProperty("GAME_SPEED"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GameProperties() {
    }
}
