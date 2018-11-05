package ru.mapgenerator;

public class Parameters {

    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

    public static final float TILE_HEIGHT = 80;
    public static final float TILE_WIDTH = (float) Math.sqrt(3) / 2 * TILE_HEIGHT;

    public static final int MAP_WIDTH = 200;
    public static final int MAP_HEIGHT = 100;

    public static final int TILE_TYPE_WATER = 0;
    public static final int TILE_TYPE_LAND = 1;
    public static final int TILE_TYPE_PLAINS = 2;
    public static final int TILE_TYPE_OCEAN = 3;
    public static final int TILE_TYPE_COAST = 4;
    public static final int TILE_TYPE_ICE = 5;
    public static final int TILE_TYPE_HILLS = 6;
    public static final int TILE_TYPE_HIGH_HILLS = 7;
    public static final int TILE_TYPE_SHALLOW_WATER = 8;
    public static final int TILE_TYPE_DESERT = 9;
    public static final int TILE_TYPE_SEMI_DESERT = 10;
    public static final int TILE_TYPE_DESERT_HILLS = 11;
    public static final int TILE_TYPE_DESERT_HIGH_HILLS = 12;
    public static final int TILE_TYPE_SEMI_DESERT_HILLS = 13;

    public static final double LAND_PERCENT_MIN = 0.4;
    public static final double LAND_PERCENT_MAX = 0.45;
    public static final int LAND_BORDER = 5;
    public static final int OBLIGATORY_LAND_BORDER = 2;

    public static final int TEMPERATURE_MIN = -10;
    public static final int TEMPERATURE_MAX = 40;

    public static final int MAP_MODE_NORMAL = 0;
    public static final int MAP_MODE_TEMPERATURE = 1;
    public static final int MAP_MODE_HEIGHT = 2;

}
