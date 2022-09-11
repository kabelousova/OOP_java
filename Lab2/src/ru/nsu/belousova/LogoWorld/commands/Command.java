package ru.nsu.belousova.LogoWorld.commands;

import ru.nsu.belousova.LogoWorld.models.World;

public interface Command {
    void execute(World world, String[] args);
}
