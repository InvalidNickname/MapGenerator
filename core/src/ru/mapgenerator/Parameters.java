package ru.mapgenerator;

public class Parameters {

    public static final float TILE_HEIGHT = 80;
    public static final float TILE_WIDTH = (float) Math.sqrt(3) / 2 * TILE_HEIGHT;

    public static final int MAP_WIDTH = 300;
    public static final int MAP_HEIGHT = 180;

    public static final int LAND_BORDER = (int) (0.05 * MAP_HEIGHT);

    public static final int TEMPERATURE_MIN = -10;
    public static final int TEMPERATURE_MAX = 50;

    public static final int DESERT_TEMPERATURE = 40;

    public static final int MAP_MODE_NORMAL = 0;
    public static final int MAP_MODE_TEMPERATURE = 1;
    public static final int MAP_MODE_HEIGHT = 2;
    public static final int MAP_MODE_BIOMES = 3;

}
