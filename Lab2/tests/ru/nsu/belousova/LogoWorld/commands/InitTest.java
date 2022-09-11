package ru.nsu.belousova.LogoWorld.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.belousova.LogoWorld.exceptions.NotEnoughArgumentsException;
import ru.nsu.belousova.LogoWorld.exceptions.NotWalidArgumentException;
import ru.nsu.belousova.LogoWorld.models.World;

public class InitTest {
    InitCommand command = new InitCommand();

    @Test
    @DisplayName("Init without params.")
    public void zeroParams() {
        World world = new World();

        try {
            command.execute(world, new String[0]);
            Assertions.fail("Expected NotEnoughArgumentsException");
        } catch (NotEnoughArgumentsException ignored) {
        }
    }

    @Test
    @DisplayName("Init with not int params.")
    public void notIntParams() {
        World world = new World();

        try {
            command.execute(world, new String[]{"1", "2", "3", "hello"});
            Assertions.fail("Expected NotWalidArgumentException");
        } catch (NotWalidArgumentException ignored) {
        }
    }

    @Test
    @DisplayName("Init with negative params.")
    public void negativeParams() {
        World world = new World();

        try {
            command.execute(world, new String[]{"1", "-2", "3", "0"});
            Assertions.fail("Expected NotWalidArgumentException");
        } catch (NotWalidArgumentException ignored) {
        }
    }

    @Test
    @DisplayName("Init with correct params.")
    public void correctParams() {
        World world = new World();

        try {
            command.execute(world, new String[]{"1", "2", "3", "10"});
        } catch (NotEnoughArgumentsException | NotWalidArgumentException e) {
            Assertions.fail(e.getMessage());
        }
    }


}
