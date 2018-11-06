package ru.mapgenerator.map.objects.tiles;

public class TypeParameters {

    public enum Type {
        LAND, WATER, OCEAN, PLAINS, ICE, DESERT, SEMI_DESERT, JUNGLE, TAIGA
    }

    public enum Megatype {
        BASIC, PLAINS, WATER, ICE, DESERT
    }

    public enum Elevation {
        NO, SMALL, MEDIUM, HIGH
    }

}
