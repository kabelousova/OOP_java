package ru.nsu.belousova.LogoWorld.ui;

import ru.nsu.belousova.LogoWorld.models.World;

import java.awt.*;

public class WorldPrinter {
    public static void print(World world) {
        Point robotPoint = world.getField().getPointOnField(world.getRobot().getX(), world.getRobot().getY());
        System.out.println("=".repeat(Math.max(0, world.getField().getWidth() + 6)));

        for (int y = world.getField().getHeight() - 1; y >= 0; --y) {
            StringBuilder builder = new StringBuilder();
            builder.append("|| ");
            for (int x = 0; x < world.getField().getWidth(); ++x) {
                if (robotPoint.x == x && robotPoint.y == y) {
                    builder.append(ConsoleColors.RED);
                    builder.append("R");
                } else if (world.getField().getArray()[x][y]) {
                    builder.append(ConsoleColors.PURPLE);
                    builder.append(".");
                } else {
                    builder.append(" ");
                }
            }
            builder.append(ConsoleColors.RESET);
            builder.append(" ||");
            System.out.println(builder);
        }
        System.out.println("=".repeat(Math.max(0, world.getField().getWidth() + 6)));

        System.out.println();
    }
}
