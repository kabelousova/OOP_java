package ru.nsu.belousova.LogoWorld.commands;

import ru.nsu.belousova.LogoWorld.exceptions.NotEnoughArgumentsException;
import ru.nsu.belousova.LogoWorld.exceptions.NotWalidArgumentException;
import ru.nsu.belousova.LogoWorld.exceptions.WorldNotInitializedException;
import ru.nsu.belousova.LogoWorld.models.Field;
import ru.nsu.belousova.LogoWorld.models.Robot;
import ru.nsu.belousova.LogoWorld.models.World;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class MoveCommand implements Command {
    public static Map<String, Point> ways = new HashMap<>() {{
        put("U", new Point(0, 1));
        put("D", new Point(0, -1));
        put("L", new Point(-1, 0));
        put("R", new Point(1, 0));
    }};

    @Override
    public void execute(World world, String[] args) {
        if (world.getField() == null)
            throw new WorldNotInitializedException("World not initialized.");

        if (args.length < 2)
            throw new NotEnoughArgumentsException(new String("MOVE command get {0} args instead of {1}.").
                    formatted(args.length, 2));

        Point direction = ways.get(args[0]);

        if (direction == null)
            throw new NotWalidArgumentException(new String("MOVE command have next directions: U, D, L and R, {0} not support.").
                    formatted(args[0]));
        try {
            int steps = Integer.parseInt(args[1]);

            Robot robot = world.getRobot();
            Field field = world.getField();

            if (robot.isDraw()) {
                for (int i = 0; i <= steps; ++i) {
                    field.colorPoint(robot.getX() + i * direction.x, robot.getY() + i * direction.y);
                }
            }

            robot.setX(robot.getX() + direction.x * steps);
            robot.setY(robot.getY() + direction.y * steps);
        } catch (NumberFormatException exception) {
            throw new NotWalidArgumentException("Secound rgument of MOVE command should be int.");
        }
    }
}
