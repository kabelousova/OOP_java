package ru.nsu.belousova.LogoWorld.commands;

import ru.nsu.belousova.LogoWorld.exceptions.NotEnoughArgumentsException;
import ru.nsu.belousova.LogoWorld.exceptions.NotWalidArgumentException;
import ru.nsu.belousova.LogoWorld.exceptions.WorldNotInitializedException;
import ru.nsu.belousova.LogoWorld.models.Robot;
import ru.nsu.belousova.LogoWorld.models.World;

import java.util.Arrays;

public class TeleportCommand implements Command {
    @Override
    public void execute(World world, String[] args) {
        if (world.getField() == null)
            throw new WorldNotInitializedException("World not initialized.");

        if (args.length < 2)
            throw new NotEnoughArgumentsException(new String("TeleportCommand get {0} args insted of {1}.").
                    formatted(args.length, 2));
        try {
            int[] argsInt = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
            Robot robot = world.getRobot();

            robot.setX(argsInt[0]);
            robot.setY(argsInt[1]);
        } catch (NumberFormatException exception) {
            throw new NotWalidArgumentException("Arguments of TELEPORT command should be int.");
        }
    }
}
