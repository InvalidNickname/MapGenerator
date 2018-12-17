package ru.mapgenerator.map.objects.tiles;

import com.badlogic.gdx.graphics.Color;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Elevation;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Megatype;

public class Type {

    private TypeParameters.Type type;
    private Megatype megatype;
    private Elevation elevation;
    private Color baseColor, biomeColor;

    Type(TypeParameters.Type type, Elevation elevation) {
        setType(type, elevation);
    }

    void setType(TypeParameters.Type type, Elevation elevation) {
        this.type = type;
        this.elevation = elevation;
        setMegatype();
        setBaseColor();
        setBiomeColor();
    }

    private void setMegatype() {
        switch (type) {
            case LAND:
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

    private void setBaseColor() {
        switch (type) {
            case OCEAN:
                switch (elevation) {
                    case NO:
                        baseColor = new Color(0x55d4ffff);
                        break;
                    case SMALL:
                        baseColor = new Color(0x55c7ffff);
                        break;
                    case MEDIUM:
                        baseColor = new Color(0x55b3ffff);
                        break;
                }
                break;
            case PLAINS:
                switch (elevation) {
                    case NO:
                        baseColor = new Color(0x00bf46ff);
                        break;
                    case SMALL:
                        baseColor = new Color(0x00b146ff);
                        break;
                    case MEDIUM:
                        baseColor = new Color(0x00a746ff);
                        break;
                    case HIGH:
                        baseColor = new Color(0x009c46ff);
                        break;
                }
                break;
            case ICE:
                switch (elevation) {
                    case NO:
                        baseColor = new Color(0xc5f7ffff);
                        break;
                    case SMALL:
                        baseColor = new Color(0xb4e4f4ff);
                        break;
                    case MEDIUM:
                        baseColor = new Color(0xcdedefff);
                        break;
                    case HIGH:
                        baseColor = new Color(0xc3e3efff);
                        break;
                }
                break;
            case DESERT:
                switch (elevation) {
                    case NO:
                        baseColor = new Color(0xffd84aff);
                        break;
                    case SMALL:
                        baseColor = new Color(0xfecd4aff);
                        break;
                    case MEDIUM:
                        baseColor = new Color(0xfbc640ff);
                        break;
                    case HIGH:
                        baseColor = new Color(0xfbb940ff);
                        break;
                }
                break;
            case SEMI_DESERT:
                switch (elevation) {
                    case NO:
                        baseColor = new Color(0xd2d84aff);
                        break;
                    case SMALL:
                        baseColor = new Color(0xc5d400ff);
                        break;
                    case MEDIUM:
                        baseColor = new Color(0xc3ca00ff);
                        break;
                    case HIGH:
                        baseColor = new Color(0xc1c000ff);
                        break;
                }
                break;
            case JUNGLE:
                switch (elevation) {
                    case NO:
                        baseColor = new Color(0x00aa3fff);
                        break;
                    case SMALL:
                        baseColor = new Color(0x009f3fff);
                        break;
                    case MEDIUM:
                        baseColor = new Color(0x00953fff);
                        break;
                    case HIGH:
                        baseColor = new Color(0x008b3fff);
                        break;
                }
                break;
            case TAIGA:
                switch (elevation) {
                    case NO:
                        baseColor = new Color(0x00bf7fff);
                        break;
                    case SMALL:
                        baseColor = new Color(0x00b17fff);
                        break;
                    case MEDIUM:
                        baseColor = new Color(0x00a77fff);
                        break;
                    case HIGH:
                        baseColor = new Color(0x009c7fff);
                        break;
                }
                break;
        }
    }

    private void setBiomeColor() {
        switch (type) {
            case OCEAN:
                biomeColor = new Color(0x55b2feff);
                break;
            case PLAINS:
                biomeColor = new Color(0x00bf46ff);
                break;
            case DESERT:
                biomeColor = new Color(0xffd84aff);
                break;
            case SEMI_DESERT:
                biomeColor = new Color(0xd2d84aff);
                break;
            case JUNGLE:
                biomeColor = new Color(0x00aa3fff);
                break;
            case ICE:
                biomeColor = new Color(0xc5f7ffff);
                break;
            case TAIGA:
                biomeColor = new Color(0x00b17fff);
                break;
        }
    }

    @Override
    public String toString() {
        switch (type) {
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

    Color getBiomeColor() {
        return biomeColor;
    }

    Color getBaseColor() {
        return baseColor;
    }

    public Megatype getMegatype() {
        return megatype;
    }

    public TypeParameters.Type getType() {
        return type;
    }

    Elevation getElevation() {
        return elevation;
    }
}
