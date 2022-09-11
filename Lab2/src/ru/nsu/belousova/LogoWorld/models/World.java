package ru.nsu.belousova.LogoWorld.models;

public class World {
    private Robot _robot;
    private Field _field;

    public World() {
        _robot = null;
        _field = null;
    }

    public Robot getRobot() {
        return _robot;
    }

    public Field getField() {
        return _field;
    }

    public void initWorld(int worldX, int worldY, int robotX, int robotY) {
        this._field = new Field(worldX, worldY);
        this._robot = new Robot(robotX, robotY);
    }
}
