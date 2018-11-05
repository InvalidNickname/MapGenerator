package ru.mapgenerator;

public class Parameters {

    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

    public static final float TILE_HEIGHT = 80;
    public static final float TILE_WIDTH = (float) Math.sqrt(3) / 2 * TILE_HEIGHT;

    public static final int MAP_WIDTH = 200;
    public static final int MAP_HEIGHT = 120;

    public static final int TILE_TYPE_WATER = 0;
    public static final int TILE_TYPE_LAND = 1;
    public static final int TILE_TYPE_PLAINS = 2;
    public static final int TILE_TYPE_OCEAN = 3;
    public static final int TILE_TYPE_JUNGLE = 4;
    public static final int TILE_TYPE_ICE = 5;
    public static final int TILE_TYPE_TAIGA = 6;
    public static final int TILE_TYPE_DESERT = 9;
    public static final int TILE_TYPE_SEMI_DESERT = 10;

    public static final int TILE_MEGATYPE_BASIC = 0;
    public static final int TILE_MEGATYPE_PLAINS = 1;
    public static final int TILE_MEGATYPE_WATER = 2;
    public static final int TILE_MEGATYPE_ICE = 3;
    public static final int TILE_MEGATYPE_DESERT = 4;

    public static final double LAND_PERCENT_MIN = 0.4;
    public static final double LAND_PERCENT_MAX = 0.45;
    public static final int LAND_BORDER = 5;
    public static final int OBLIGATORY_LAND_BORDER = 2;

    public static final int TEMPERATURE_MIN = -10;
    public static final int TEMPERATURE_MAX = 50;

    public static final int DESERT_TEMPERATURE = 40;

    public static final int MAP_MODE_NORMAL = 0;
    public static final int MAP_MODE_TEMPERATURE = 1;
    public static final int MAP_MODE_HEIGHT = 2;
    public static final int MAP_MODE_BIOMES = 3;

}
