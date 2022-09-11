package ru.nsu.belousova.LogoWorld.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.belousova.LogoWorld.exceptions.WorldNotInitializedException;
import ru.nsu.belousova.LogoWorld.models.World;


public class DrawTest {
    DrawCommand command = new DrawCommand();

    @Test
    @DisplayName("Draw without initialize world.")
    public void nullWorld() {
        World world = new World();

        try {
            command.execute(world, new String[0]);
            Assertions.fail("Expected WorldNotInitializedException");
        } catch (WorldNotInitializedException ignored) {
        }
    }

    @Test
    @DisplayName("Enable Draw.")
    public void isDisableDraw() {
        World world = new World();
        MoveCommand move = new MoveCommand();
        world.initWorld(5, 5, 1, 1);

        command.execute(world, new String[0]);
        move.execute(world, new String[]{"U", "1"});

        Assertions.assertTrue(world.getField().getArray()[1][2]);
        Assertions.assertTrue(world.getField().getArray()[1][1]);
        Assertions.assertTrue(world.getRobot().isDraw());
    }
}
