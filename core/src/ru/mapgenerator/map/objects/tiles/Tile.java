package ru.mapgenerator.map.objects.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.TileGrid;
import ru.mapgenerator.map.objects.River;

import static ru.mapgenerator.Parameters.MAP_HEIGHT;
import static ru.mapgenerator.Parameters.MAP_WIDTH;

public class Tile {

    private final int x, y;
    private float tileX, tileY;
    private final Texture texture, tempTexture;
    private int z;
    private float temperature, latitude, longitude;
    private float mathLatitude;
    private Type type;
    private Color color;
    private River river;

    public Tile(TypeParameters.Type type, TypeParameters.Elevation terrain, int x, int y) {
        this.x = x;
        this.y = y;

        // широта
        mathLatitude = (float) 2 * y / MAP_HEIGHT;
        if (mathLatitude > 1) mathLatitude = 2 - mathLatitude;
        temperature = MathUtils.lerp(Parameters.TEMPERATURE_MIN, Parameters.TEMPERATURE_MAX, mathLatitude);
        latitude = MathUtils.lerp(90, 0, mathLatitude);

        // долгота
        longitude = (float) 2 * x / MAP_WIDTH;
        if (longitude > 1) longitude = 2 - longitude;
        longitude = MathUtils.lerp(180, 0, longitude);

        this.type = new Type(type, terrain);

        texture = ((Main) Gdx.app.getApplicationListener()).assetManager.get("tiles/basic_hex.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tempTexture = ((Main) Gdx.app.getApplicationListener()).assetManager.get("tiles/basic_hex.png");
        tempTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void render(SpriteBatch spriteBatch, int mode, int x, int y) {
        // координаты для отрисовки
        tileX = (x + (y % 2 == 1 ? 0.5f : 0)) * Parameters.TILE_WIDTH;
        tileY = y * Parameters.TILE_HEIGHT - (y >> 1) * Parameters.TILE_HEIGHT / 2f - (y % 2 == 1 ? 0.25f * Parameters.TILE_HEIGHT : 0);

        switch (mode) {
            case Parameters.MAP_MODE_NORMAL:
                spriteBatch.setColor(color);
                spriteBatch.draw(texture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                if (river != null) {
                    spriteBatch.setColor(1, 1, 1, 1);
                    river.render(spriteBatch, tileX, tileY);
                }
                break;
            case Parameters.MAP_MODE_TEMPERATURE: {
                float temp = (temperature + Math.abs(Parameters.TEMPERATURE_MIN) + 1) / (Math.abs(Parameters.TEMPERATURE_MIN) + Math.abs(Parameters.TEMPERATURE_MAX) + 2);
                spriteBatch.setColor(1, 1 - temp, 1 - temp, 1);
                spriteBatch.draw(tempTexture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                break;
            }
            case Parameters.MAP_MODE_HEIGHT: {
                float temp = (float) z / (TileGrid.maxZ + 1);
                spriteBatch.setColor(1 - temp, 1 - temp, 1, 1);
                spriteBatch.draw(tempTexture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                break;
            }
            case Parameters.MAP_MODE_BIOMES:
                Color color = type.getBiomeColor();
                spriteBatch.setColor(color);
                spriteBatch.draw(tempTexture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
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

    public void setType(TypeParameters.Type type, TypeParameters.Elevation terrain) {
        color = this.type.setType(type, terrain);
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
