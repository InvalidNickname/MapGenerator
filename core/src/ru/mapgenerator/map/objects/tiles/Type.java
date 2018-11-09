package ru.mapgenerator.map.objects.tiles;

import com.badlogic.gdx.graphics.Color;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Elevation;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Megatype;

public class Type {

    private TypeParameters.Type type;
    private Megatype megatype;
    private Elevation elevation;

    Type(TypeParameters.Type type, Elevation elevation) {
        setType(type, elevation);
    }

    public TypeParameters.Type getType() {
        return type;
    }

    public Elevation getElevation() {
        return elevation;
    }

    private void setMegatype() {
        switch (type) {
            case LAND:
            case WATER:
                megatype = Megatype.BASIC;
                break;
            case OCEAN:
                megatype = Megatype.WATER;
                break;
            case PLAINS:
            case JUNGLE:
            case TAIGA:
                megatype = Megatype.PLAINS;
                break;
            case ICE:
                megatype = Megatype.ICE;
                break;
            case DESERT:
            case SEMI_DESERT:
                megatype = Megatype.DESERT;
                break;
        }
    }

    Color setType(TypeParameters.Type type, Elevation terrain) {
        this.type = type;
        setMegatype();
        this.elevation = terrain;
        switch (type) {
            case LAND:
                return new Color(0xffffffff);
            case WATER:
                return new Color(0x55b3ffff);
            case OCEAN:
                switch (terrain) {
                    case NO:
                        return new Color(0x55d4ffff);
                    case SMALL:
                        return new Color(0x55c7ffff);
                    case MEDIUM:
                        return new Color(0x55b3ffff);
                }
            case PLAINS:
                switch (terrain) {
                    case NO:
                        return new Color(0x00bf46ff);
                    case SMALL:
                        return new Color(0x00b146ff);
                    case MEDIUM:
                        return new Color(0x00a746ff);
                    case HIGH:
                        return new Color(0x009c46ff);
                }
            case ICE:
                switch (terrain) {
                    case NO:
                        return new Color(0xc5f7ffff);
                    case SMALL:
                        return new Color(0xb4e4f4ff);
                    case MEDIUM:
                        return new Color(0xcdedefff);
                    case HIGH:
                        return new Color(0xc3e3efff);
                }
            case DESERT:
                switch (terrain) {
                    case NO:
                        return new Color(0xffd84aff);
                    case SMALL:
                        return new Color(0xfecd4aff);
                    case MEDIUM:
                        return new Color(0xfbc640ff);
                    case HIGH:
                        return new Color(0xfbb940ff);
                }
            case SEMI_DESERT:
                switch (terrain) {
                    case NO:
                        return new Color(0xd2d84aff);
                    case SMALL:
                        return new Color(0xc5d400ff);
                    case MEDIUM:
                        return new Color(0xc3ca00ff);
                    case HIGH:
                        return new Color(0xc1c000ff);
                }
            case JUNGLE:
                switch (terrain) {
                    case NO:
                        return new Color(0x00aa3fff);
                    case SMALL:
                        return new Color(0x009f3fff);
                    case MEDIUM:
                        return new Color(0x00953fff);
                    case HIGH:
                        return new Color(0x008b3fff);
                }
            case TAIGA:
                switch (terrain) {
                    case NO:
                        return new Color(0x00bf7fff);
                    case SMALL:
                        return new Color(0x00b17fff);
                    case MEDIUM:
                        return new Color(0x00a77fff);
                    case HIGH:
                        return new Color(0x009c7fff);
                }
            default:
                return null;
        }
    }

    Color getBiomeColor() {
        switch (type) {
            case OCEAN:
                return new Color(0x55b2feff);
            case PLAINS:
                return new Color(0x00bf46ff);
            case DESERT:
                return new Color(0xffd84aff);
            case SEMI_DESERT:
                return new Color(0xd2d84aff);
            case JUNGLE:
                return new Color(0x00aa3fff);
            case ICE:
                return new Color(0xc5f7ffff);
            case TAIGA:
                return new Color(0x00b17fff);
        }
        return null;
    }

    @Override
    public String toString() {
        switch (type) {
            case LAND:
                return "Land";
            case WATER:
                return "Water";
            case OCEAN:
                if (elevation == Elevation.NO)
                    return "Shallow water";
                else if (elevation == Elevation.SMALL)
                    return "Coast";
                else if (elevation == Elevation.MEDIUM)
                    return "Ocean";
            case PLAINS:
                if (elevation == Elevation.NO)
                    return "Plains";
                else if (elevation == Elevation.SMALL)
                    return "Hills";
                else if (elevation == Elevation.MEDIUM)
                    return "High hills";
                else if (elevation == Elevation.HIGH)
                    return "Mountains";
            case ICE:
                if (elevation == Elevation.NO)
                    return "Ice";
                else if (elevation == Elevation.SMALL)
                    return "Icy hills";
                else if (elevation == Elevation.MEDIUM)
                    return "Icy high hills";
                else if (elevation == Elevation.HIGH)
                    return "Icy mountains";
            case DESERT:
                if (elevation == Elevation.NO)
                    return "Desert";
                else if (elevation == Elevation.SMALL)
                    return "Desert hills";
                else if (elevation == Elevation.MEDIUM)
                    return "High desert hills";
                else if (elevation == Elevation.HIGH)
                    return "Dunes";
            case SEMI_DESERT:
                if (elevation == Elevation.NO)
                    return "Semi desert";
                else if (elevation == Elevation.SMALL)
                    return "Semi desert hills";
                else if (elevation == Elevation.MEDIUM)
                    return "High semi desert hills";
                else if (elevation == Elevation.HIGH)
                    return "Semi desert mountains";
            case JUNGLE:
                if (elevation == Elevation.NO)
                    return "Jungle";
                else if (elevation == Elevation.SMALL)
                    return "Jungle hills";
                else if (elevation == Elevation.MEDIUM)
                    return "High jungle hills";
                else if (elevation == Elevation.HIGH)
                    return "Jungle mountains";
            case TAIGA:
                if (elevation == Elevation.NO)
                    return "Taiga";
                else if (elevation == Elevation.SMALL)
                    return "Taiga hills";
                else if (elevation == Elevation.MEDIUM)
                    return "High taiga hills";
                else if (elevation == Elevation.HIGH)
                    return "Taiga mountains";
            default:
                return null;
        }
    }

    public Megatype getMegatype() {
        return megatype;
    }

}
