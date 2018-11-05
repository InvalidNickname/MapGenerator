package ru.mapgenerator.map.objects.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.objects.River;
import ru.mapgenerator.map.objects.TileGrid;

public class Tile {

    private int x, y, z;
    private float tileX, tileY, temperature, latitude, longitude;
    private float mathLatitude;
    private Type type;
    private Texture texture, tempTexture;
    private Main applicationListener;
    private River river;

    public Tile(int type, int terrain, int x, int y) {
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
        this.type = new Type(type, terrain);

        // координаты для отрисовки
        tileX = (x + (y % 2 == 1 ? 0.5f : 0)) * Parameters.TILE_WIDTH;
        tileY = y * Parameters.TILE_HEIGHT - (y >> 1) * Parameters.TILE_HEIGHT / 2f - (y % 2 == 1 ? 0.25f * Parameters.TILE_HEIGHT : 0);

        tempTexture = applicationListener.assetManager.get("tiles/basic_0.png");
    }

    public void render(SpriteBatch spriteBatch, int mode) {
        switch (mode) {
            case Parameters.MAP_MODE_NORMAL:
                spriteBatch.draw(texture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                if (river != null) {
                    //spriteBatch.draw(texture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                    river.render(spriteBatch, tileX, tileY);
                }
                break;
            case Parameters.MAP_MODE_TEMPERATURE: {
                float temp = (temperature + Math.abs(Parameters.TEMPERATURE_MIN) + 1) / (Math.abs(Parameters.TEMPERATURE_MIN) + Math.abs(Parameters.TEMPERATURE_MAX) + 2);
                spriteBatch.setColor(1, 1 - temp, 1 - temp, 1);
                spriteBatch.draw(tempTexture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                spriteBatch.setColor(1, 1, 1, 1);
                break;
            }
            case Parameters.MAP_MODE_HEIGHT: {
                float temp = (float) z / (TileGrid.maxZ + 1);
                spriteBatch.setColor(1 - temp, 1 - temp, 1, 1);
                spriteBatch.draw(tempTexture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                spriteBatch.setColor(1, 1, 1, 1);
                break;
            }
            case Parameters.MAP_MODE_BIOMES:
                Color color = new Color(1, 1, 1, 1);
                switch (type.getType()) {
                    case Parameters.TILE_TYPE_OCEAN:
                        color = new Color(0x55b2feff);
                        break;
                    case Parameters.TILE_TYPE_PLAINS:
                        color = new Color(0x00bf46ff);
                        break;
                    case Parameters.TILE_TYPE_DESERT:
                        color = new Color(0xffd84aff);
                        break;
                    case Parameters.TILE_TYPE_SEMI_DESERT:
                        color = new Color(0xd2d84aff);
                        break;
                    case Parameters.TILE_TYPE_JUNGLE:
                        color = new Color(0x00aa3fff);
                        break;
                    case Parameters.TILE_TYPE_ICE:
                        color = new Color(0xc5f7ffff);
                        break;
                    case Parameters.TILE_TYPE_TAIGA:
                        color = new Color(0x00b17fff);
                        break;
                }
                spriteBatch.setColor(color);
                spriteBatch.draw(tempTexture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                spriteBatch.setColor(1, 1, 1, 1);
                break;
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

    public Type getType() {
        return type;
    }

    public void setType(int type, int terrain) {
        texture = this.type.setType(type, terrain);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public float getTileX() {
        return tileX;
    }

    public float getTileY() {
        return tileY;
    }

    public String getStringType() {
        return type.toString();
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setTemperature() {
        temperature = MathUtils.lerp(Parameters.TEMPERATURE_MIN, Parameters.TEMPERATURE_MAX, mathLatitude);
        temperature *= 1 - z / (TileGrid.maxZ + 1f);
        temperature += MathUtils.random(-1, 1);
    }

    public float getTemperature() {
        return temperature;
    }

    public River getRiver() {
        return river;
    }

    public void setRiver(River river) {
        this.river = river;
    }
}
