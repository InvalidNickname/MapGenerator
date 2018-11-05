package ru.mapgenerator.map.objects.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;

public class Type {

    public static final int NO_ELEVATION = 0;
    public static final int SMALL_ELEVATION = 1;
    public static final int MEDIUM_ELEVATION = 2;
    public static final int HIGH_ELEVATION = 3;

    private int type, megatype;
    private int terrain;

    Type(int type, int terrain) {
        setType(type, terrain);
    }

    public int getType() {
        return type;
    }

    public int getTerrain() {
        return terrain;
    }

    private void setMegatype() {
        switch (type) {
            case Parameters.TILE_TYPE_LAND:
            case Parameters.TILE_TYPE_WATER:
                megatype = Parameters.TILE_MEGATYPE_BASIC;
                break;
            case Parameters.TILE_TYPE_OCEAN:
                megatype = Parameters.TILE_MEGATYPE_WATER;
                break;
            case Parameters.TILE_TYPE_PLAINS:
            case Parameters.TILE_TYPE_JUNGLE:
            case Parameters.TILE_TYPE_TAIGA:
                megatype = Parameters.TILE_MEGATYPE_PLAINS;
                break;
            case Parameters.TILE_TYPE_ICE:
                megatype = Parameters.TILE_MEGATYPE_ICE;
                break;
            case Parameters.TILE_TYPE_DESERT:
            case Parameters.TILE_TYPE_SEMI_DESERT:
                megatype = Parameters.TILE_MEGATYPE_DESERT;
                break;
        }
    }

    Texture setType(int type, int terrain) {
        this.type = type;
        setMegatype();
        this.terrain = terrain;
        String file;
        switch (type) {
            case Parameters.TILE_TYPE_LAND:
                return ((Main) Gdx.app.getApplicationListener()).assetManager.get("tiles/basic_0.png");
            case Parameters.TILE_TYPE_WATER:
                return ((Main) Gdx.app.getApplicationListener()).assetManager.get("tiles/water_2.png");
            case Parameters.TILE_TYPE_OCEAN:
                file = "tiles/water_" + terrain + ".png";
                return ((Main) Gdx.app.getApplicationListener()).assetManager.get(file);
            case Parameters.TILE_TYPE_PLAINS:
                file = "tiles/plains_" + terrain + ".png";
                return ((Main) Gdx.app.getApplicationListener()).assetManager.get(file);
            case Parameters.TILE_TYPE_ICE:
                file = "tiles/ice_" + terrain + ".png";
                return ((Main) Gdx.app.getApplicationListener()).assetManager.get(file);
            case Parameters.TILE_TYPE_DESERT:
                file = "tiles/desert_" + terrain + ".png";
                return ((Main) Gdx.app.getApplicationListener()).assetManager.get(file);
            case Parameters.TILE_TYPE_SEMI_DESERT:
                file = "tiles/semi_desert_" + terrain + ".png";
                return ((Main) Gdx.app.getApplicationListener()).assetManager.get(file);
            case Parameters.TILE_TYPE_JUNGLE:
                file = "tiles/jungle_" + terrain + ".png";
                return ((Main) Gdx.app.getApplicationListener()).assetManager.get(file);
            case Parameters.TILE_TYPE_TAIGA:
                file = "tiles/taiga_" + terrain + ".png";
                return ((Main) Gdx.app.getApplicationListener()).assetManager.get(file);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (type) {
            case Parameters.TILE_TYPE_LAND:
                return "Land";
            case Parameters.TILE_TYPE_WATER:
                return "Water";
            case Parameters.TILE_TYPE_OCEAN:
                if (terrain == NO_ELEVATION)
                    return "Shallow water";
                else if (terrain == SMALL_ELEVATION)
                    return "Coast";
                else if (terrain == MEDIUM_ELEVATION)
                    return "Ocean";
            case Parameters.TILE_TYPE_PLAINS:
                if (terrain == NO_ELEVATION)
                    return "Plains";
                else if (terrain == SMALL_ELEVATION)
                    return "Hills";
                else if (terrain == MEDIUM_ELEVATION)
                    return "High hills";
                else if (terrain == HIGH_ELEVATION)
                    return "Mountains";
            case Parameters.TILE_TYPE_ICE:
                if (terrain == NO_ELEVATION)
                    return "Ice";
                else if (terrain == SMALL_ELEVATION)
                    return "Icy hills";
                else if (terrain == MEDIUM_ELEVATION)
                    return "Icy high hills";
                else if (terrain == HIGH_ELEVATION)
                    return "Icy mountains";
            case Parameters.TILE_TYPE_DESERT:
                if (terrain == NO_ELEVATION)
                    return "Desert";
                else if (terrain == SMALL_ELEVATION)
                    return "Desert hills";
                else if (terrain == MEDIUM_ELEVATION)
                    return "High desert hills";
                else if (terrain == HIGH_ELEVATION)
                    return "Dunes";
            case Parameters.TILE_TYPE_SEMI_DESERT:
                if (terrain == NO_ELEVATION)
                    return "Semi desert";
                else if (terrain == SMALL_ELEVATION)
                    return "Semi desert hills";
                else if (terrain == MEDIUM_ELEVATION)
                    return "High semi desert hills";
                else if (terrain == HIGH_ELEVATION)
                    return "Semi desert mountains";
            case Parameters.TILE_TYPE_JUNGLE:
                if (terrain == NO_ELEVATION)
                    return "Jungle";
                else if (terrain == SMALL_ELEVATION)
                    return "Jungle hills";
                else if (terrain == MEDIUM_ELEVATION)
                    return "High jungle hills";
                else if (terrain == HIGH_ELEVATION)
                    return "Jungle mountains";
            case Parameters.TILE_TYPE_TAIGA:
                if (terrain == NO_ELEVATION)
                    return "Taiga";
                else if (terrain == SMALL_ELEVATION)
                    return "Taiga hills";
                else if (terrain == MEDIUM_ELEVATION)
                    return "High taiga hills";
                else if (terrain == HIGH_ELEVATION)
                    return "Taiga mountains";
            default:
                return null;
        }
    }

    public int getMegatype() {
        return megatype;
    }
}
