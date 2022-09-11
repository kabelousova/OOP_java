package ru.nsu.belousova.LogoWorld.commands;

import ru.nsu.belousova.LogoWorld.exceptions.NotEnoughArgumentsException;
import ru.nsu.belousova.LogoWorld.exceptions.NotWalidArgumentException;
import ru.nsu.belousova.LogoWorld.models.World;

import java.util.Arrays;

public class InitCommand implements Command {
    @Override
    public void execute(World world, String[] args) {
        if (args.length < 4)
            throw new NotEnoughArgumentsException(new String("INIT command get {0} args instead of {1}.").
                    formatted(args.length, 4));
        try {
            int[] argsInt = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
            if (argsInt[0] <= 0 || argsInt[1] <= 0)
                throw new NotWalidArgumentException("Cannot create field with negative or zero dimensions.");

            if (argsInt[0] > 80 || argsInt[1] > 80)
                throw new NotWalidArgumentException("The field size is too large.");

            world.initWorld(argsInt[0], argsInt[1], argsInt[2], argsInt[3]);
        } catch (NumberFormatException exception) {
            throw new NotWalidArgumentException("Arguments of INIT command should be int.");
        }
    }
}
