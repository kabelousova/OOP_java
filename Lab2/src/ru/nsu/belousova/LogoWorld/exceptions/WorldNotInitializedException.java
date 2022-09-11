package ru.nsu.belousova.LogoWorld.exceptions;

public class WorldNotInitializedException extends RuntimeException {
    public WorldNotInitializedException(String message) {
        super(message);
    }
}
