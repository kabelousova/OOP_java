package ru.nsu.belousova.LogoWorld.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.belousova.LogoWorld.exceptions.NotEnoughArgumentsException;
import ru.nsu.belousova.LogoWorld.exceptions.NotWalidArgumentException;
import ru.nsu.belousova.LogoWorld.exceptions.WorldNotInitializedException;
import ru.nsu.belousova.LogoWorld.models.World;

public class MoveTest {
    MoveCommand command = new MoveCommand();

    @Test
    @DisplayName("Move without initialize world.")
    public void nullWorld() {
        World world = new World();

        try {
            command.execute(world, new String[0]);
            Assertions.fail("Expected WorldNotInitializedException");
        } catch (WorldNotInitializedException ignored) {
        }
    }

    @Test
    @DisplayName("Move without params.")
    public void noneParams() {
        World world = new World();

        world.initWorld(5, 5, 1, 1);
        world.getRobot().setDraw(true);

        try {
            command.execute(world, new String[0]);
            Assertions.fail("Expected NotEnoughArgumentsException");
        } catch (NotEnoughArgumentsException ignored) {
        }
    }

    @Test
    @DisplayName("Move with wrong param.")
    public void notIntParam() {
        World world = new World();

        world.initWorld(5, 5, 1, 1);

        try {
            command.execute(world, new String[]{"hello", "hello"});
            Assertions.fail("Expected NotWalidArgumentException");
        } catch (NotWalidArgumentException ignored) {
        }
    }

    @Test
    @DisplayName("Correct move.")
    public void correctParams() {
        World world = new World();
        world.initWorld(5, 5, 1, 1);

        command.execute(world, new String[]{"U", "5"});

        Assertions.assertEquals(1, world.getRobot().getX());
        Assertions.assertEquals(6, world.getRobot().getY());
    }
}
