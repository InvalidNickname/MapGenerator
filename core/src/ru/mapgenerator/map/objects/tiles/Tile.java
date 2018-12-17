package ru.mapgenerator.map.objects.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.TileGrid;
import ru.mapgenerator.map.objects.River;
import ru.mapgenerator.map.objects.coasts.Coastline;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Elevation;

import static ru.mapgenerator.Parameters.MAP_HEIGHT;
import static ru.mapgenerator.Parameters.MAP_WIDTH;

public class Tile {

    private final int x, y;
    private final Texture texture;
    private final float latitude, longitude;
    private float tileX, tileY;
    private int z;
    private float temperature;
    private Type type;
    private River river;
    private Coastline coastline;

    public Tile(TypeParameters.Type type, Elevation terrain, int x, int y) {
        this.x = x;
        this.y = y;

        latitude = MathUtils.lerp(90, 0, 2f * y / MAP_HEIGHT > 1 ? 2 - 2f * y / MAP_HEIGHT : 2f * y / MAP_HEIGHT);
        longitude = MathUtils.lerp(180, 0, 2f * x / MAP_WIDTH > 1 ? 2 - 2f * x / MAP_WIDTH : 2f * x / MAP_WIDTH);

        this.type = new Type(type, terrain);

        texture = ((Main) Gdx.app.getApplicationListener()).assetManager.get("tiles/basic_hex.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        coastline = new Coastline(new boolean[]{false, false, false, false, false, false});
    }

    public void render(SpriteBatch spriteBatch, int mode, int x, int y) {
        // координаты для отрисовки
        tileX = (x + (y % 2 == 1 ? 0.5f : 0)) * Parameters.TILE_WIDTH;
        tileY = y * Parameters.TILE_HEIGHT - (y >> 1) * Parameters.TILE_HEIGHT / 2f - (y % 2 == 1 ? 0.25f * Parameters.TILE_HEIGHT : 0);

        switch (mode) {
            case Parameters.MAP_MODE_NORMAL:
                spriteBatch.setColor(type.getBaseColor());
                spriteBatch.draw(texture, tileX, tileY, Parameters.TILE_WIDTH, Parameters.TILE_HEIGHT);
                spriteBatch.setColor(1, 1, 1, 1);
                coastline.render(spriteBatch, tileX, tileY);
                if (river != null) {
                    river.render(spriteBatch, tileX, tileY);
                }
                break;
            case Parameters.MAP_MODE_TEMPERATURE: {
                float temp = (temperature + Math.abs(Parameters.TEMPERATURE_MIN) + 1) / (Math.abs(Parameters.TEMPERATURE_MIN) + Math.abs(Parameters.TEMPERATURE_MAX) + 2);
                spriteBatch.setColor(1, 1 - temp, 1 - temp, 1);
                spriteBatch.draw(texture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                break;
            }
            case Parameters.MAP_MODE_HEIGHT: {
                float temp = (float) z / (TileGrid.getMaxZ() + 1);
                spriteBatch.setColor(1 - temp, 1 - temp, 1, 1);
                spriteBatch.draw(texture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
                break;
            }
            case Parameters.MAP_MODE_BIOMES:
                spriteBatch.setColor(type.getBiomeColor());
                spriteBatch.draw(texture, tileX, tileY, Parameters.TILE_WIDTH + 0.5f, Parameters.TILE_HEIGHT + 0.5f);
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

    public void setType(TypeParameters.Type type) {
        this.type.setType(type, this.type.getElevation());
    }

    public void setType(TypeParameters.Type type, Elevation terrain) {
        this.type.setType(type, terrain);
    }

    public float getTileX() {
        return tileX;
    }

    public float getTileY() {
        return tileY;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void increaseZ(int x) {
        z += x;
    }

    public void setTemperature() {
        temperature = MathUtils.lerp(Parameters.TEMPERATURE_MIN, Parameters.TEMPERATURE_MAX, 1 - latitude / 90f);
        temperature *= 1 - z / (TileGrid.getMaxZ() + 1f);
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

    public void addCoast(int dest) {
        coastline.addCoast(dest);
    }
}
