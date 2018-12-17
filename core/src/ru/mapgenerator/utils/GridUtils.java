package ru.mapgenerator.utils;

public class GridUtils {

    public static float getRotation(int destination) {
        switch (destination) {
            case 0:
                return 180;
            case 1:
                return 120;
            case 2:
                return 60;
            case 3:
                return 0;
            case 4:
                return 300;
            case 5:
                return 240;
            default:
                return 0;
        }
    }
}
