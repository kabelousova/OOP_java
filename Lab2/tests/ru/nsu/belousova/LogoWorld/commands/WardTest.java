package ru.nsu.belousova.LogoWorld.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.belousova.LogoWorld.exceptions.WorldNotInitializedException;
import ru.nsu.belousova.LogoWorld.models.World;

public class WardTest {
    WardCommand command = new WardCommand();

    @Test
    @DisplayName("Ward without initialize world.")
    public void nullWorld() {
        World world = new World();

        try {
            command.execute(world, new String[0]);
            Assertions.fail("Expected WorldNotInitializedException");
        } catch (WorldNotInitializedException ignored) {
        }
    }

    @Test
    @DisplayName("Disable Draw.")
    public void isDisableDraw() {
        World world = new World();
        MoveCommand move = new MoveCommand();

        world.initWorld(5, 5, 1, 1);
        world.getRobot().setDraw(true);

        command.execute(world, new String[0]);
        move.execute(world, new String[]{"U", "1"});

        Assertions.assertFalse(world.getField().getArray()[1][2]);
        Assertions.assertFalse(world.getField().getArray()[1][1]);
        Assertions.assertFalse(world.getRobot().isDraw());
    }
}
