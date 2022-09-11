package ru.nsu.belousova.LogoWorld.commands;

import ru.nsu.belousova.LogoWorld.exceptions.WorldNotInitializedException;
import ru.nsu.belousova.LogoWorld.models.World;

public class DrawCommand implements Command {
    @Override
    public void execute(World world, String[] args) {
        if (world.getField() == null)
            throw new WorldNotInitializedException("World not initialized.");

        world.getRobot().setDraw(true);
    }
}
