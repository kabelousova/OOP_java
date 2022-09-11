package ru.nsu.belousova.LogoWorld.exceptions;

public class NotEnoughArgumentsException extends RuntimeException {
    public NotEnoughArgumentsException(String message) {
        super(message);
    }
}
