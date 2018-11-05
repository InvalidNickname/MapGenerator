package ru.mapgenerator.map.objects.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.objects.TileGrid;

public class Tile {

    private int x, y, z;
    private float tileX, tileY, temperature, latitude, longitude;
    private float mathLatitude;
    private int type;
    private Texture texture, tempTexture;
    private Main applicationListener;

    Tile(int type, int x, int y) {
        this.x = x;
        this.y = y;

        // широта
        mathLatitude = (float) 2 * y / Parameters.MAP_HEIGHT;
        if (mathLatitude > 1) mathLatitude = 2 - mathLatitude;
        temperature = MathUtils.lerp(Parameters.TEMPERATURE_MIN, Parameters.TEMPERATURE_MAX, mathLatitude);
        latitude = MathUtils.lerp(90, 0, mathLatitude);

        // долгота
        longitude = (float) 2 * x / Parameters.MAP_WIDTH;
        if (longitude > 1) longitude = 2 - longitude;
        longitude = MathUtils.lerp(180, 0, longitude);

        applicationListener = (Main) Gdx.app.getApplicationListener();
        setType(type);

        // координаты для отрисовки
        tileX = (x + (y % 2 == 1 ? 0.5f : 0)) * Parameters.TILE_WIDTH;
        tileY = y * Parameters.TILE_HEIGHT - (y >> 1) * Parameters.TILE_HEIGHT / 2f - (y % 2 == 1 ? 0.25f * Parameters.TILE_HEIGHT : 0);

        tempTexture = applicationListener.assetManager.get("tiles/basic_hex.png");
    }

    public void render(SpriteBatch spriteBatch, int mode) {
        if (mode == Parameters.MAP_MODE_NORMAL)
            spriteBatch.draw(texture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
        else if (mode == Parameters.MAP_MODE_TEMPERATURE) {
            float temp = (temperature + Math.abs(Parameters.TEMPERATURE_MIN)) / (Math.abs(Parameters.TEMPERATURE_MIN) + Math.abs(Parameters.TEMPERATURE_MAX));
            spriteBatch.setColor(1, 1 - temp, 1 - temp, 1);
            spriteBatch.draw(tempTexture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
            spriteBatch.setColor(1, 1, 1, 1);
        } else if (mode == Parameters.MAP_MODE_HEIGHT) {
            float temp = (float) z / (TileGrid.maxZ + 1);
            spriteBatch.setColor(1 - temp, 1 - temp, 1, 1);
            spriteBatch.draw(tempTexture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
            spriteBatch.setColor(1, 1, 1, 1);
        }
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        switch (type) {
            case Parameters.TILE_TYPE_COAST:
                texture = applicationListener.assetManager.get("tiles/water_hex.png");
                break;
            case Parameters.TILE_TYPE_OCEAN:
                texture = applicationListener.assetManager.get("tiles/ocean_hex.png");
                break;
            case Parameters.TILE_TYPE_PLAINS:
                texture = applicationListener.assetManager.get("tiles/plain_hex.png");
                break;
            case Parameters.TILE_TYPE_ICE:
                texture = applicationListener.assetManager.get("tiles/ice_hex.png");
                break;
            case Parameters.TILE_TYPE_HILLS:
                texture = applicationListener.assetManager.get("tiles/hill_hex.png");
                break;
            case Parameters.TILE_TYPE_HIGH_HILLS:
                texture = applicationListener.assetManager.get("tiles/high_hill_hex.png");
                break;
            case Parameters.TILE_TYPE_SHALLOW_WATER:
                texture = applicationListener.assetManager.get("tiles/shallow_water_hex.png");
                break;
            case Parameters.TILE_TYPE_DESERT:
                texture = applicationListener.assetManager.get("tiles/desert_hex.png");
                break;
            case Parameters.TILE_TYPE_SEMI_DESERT:
                texture = applicationListener.assetManager.get("tiles/semi_desert_hex.png");
                break;
            case Parameters.TILE_TYPE_DESERT_HILLS:
                texture = applicationListener.assetManager.get("tiles/desert_hill_hex.png");
                break;
            case Parameters.TILE_TYPE_DESERT_HIGH_HILLS:
                texture = applicationListener.assetManager.get("tiles/desert_high_hill_hex.png");
                break;
            case Parameters.TILE_TYPE_SEMI_DESERT_HILLS:
                texture = applicationListener.assetManager.get("tiles/semi_desert_hill_hex.png");
                break;
            default:
                texture = applicationListener.assetManager.get("tiles/basic_hex.png");
                break;
        }
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public float getTileX() {
        return tileX;
    }

    public float getTileY() {
        return tileY;
    }

    public String getStringType() {
        switch (type) {
            case Parameters.TILE_TYPE_LAND:
                return "Land";
            case Parameters.TILE_TYPE_WATER:
                return "Water";
            case Parameters.TILE_TYPE_COAST:
                return "Coast";
            case Parameters.TILE_TYPE_OCEAN:
                return "Ocean";
            case Parameters.TILE_TYPE_PLAINS:
                return "Plains";
            case Parameters.TILE_TYPE_ICE:
                return "Ice";
            case Parameters.TILE_TYPE_HILLS:
                return "Hills";
            case Parameters.TILE_TYPE_HIGH_HILLS:
                return "High hills";
            case Parameters.TILE_TYPE_SHALLOW_WATER:
                return "Shallow water";
            case Parameters.TILE_TYPE_DESERT:
                return "Desert";
            case Parameters.TILE_TYPE_SEMI_DESERT:
                return "Semi desert";
            case Parameters.TILE_TYPE_DESERT_HILLS:
                return "Desert hills";
            case Parameters.TILE_TYPE_DESERT_HIGH_HILLS:
                return "Desert high hills";
            default:
                return null;
        }
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;

    }

    public void setTemperature() {
        temperature = MathUtils.lerp(Parameters.TEMPERATURE_MIN, Parameters.TEMPERATURE_MAX, mathLatitude);
        temperature = temperature * (1 - z / (TileGrid.maxZ + 1f));
    }

    public float getTemperature() {
        return temperature;
    }
}
