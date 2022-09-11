package ru.nsu.belousova.LogoWorld;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Simulator simulator = new Simulator(args[0]);
            simulator.run();
        } catch (IOException exception) {
            System.out.println("Error reading commands from file.");
            exception.printStackTrace();
        } catch (IndexOutOfBoundsException exception){
            System.out.println("Filename not specified.");
            exception.printStackTrace();
        }
    }
}
