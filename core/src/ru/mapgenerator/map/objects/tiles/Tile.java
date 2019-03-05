package ru.mapgenerator.map.objects.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.TileGrid;
import ru.mapgenerator.map.objects.tiles.TypeParameters.Elevation;

import static ru.mapgenerator.Parameters.*;

public class Tile {

    private final int x, y;
    private final float latitude, longitude;
    private float tileX, tileY;
    private int z;
    private float temperature;
    private Type type;

    public Tile(TypeParameters.Type type, Elevation terrain, int x, int y) {
        this.x = x;
        this.y = y;

        latitude = MathUtils.lerp(90, 0, 2f * y / MAP_HEIGHT > 1 ? 2 - 2f * y / MAP_HEIGHT : 2f * y / MAP_HEIGHT);
        longitude = MathUtils.lerp(180, 0, 2f * x / MAP_WIDTH > 1 ? 2 - 2f * x / MAP_WIDTH : 2f * x / MAP_WIDTH);

        this.type = new Type(type, terrain);
    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer renderer, int mode, int x, int y) {
        // координаты для отрисовки
        tileX = (x + (y % 2 == 1 ? 0.5f : 0)) * TILE_WIDTH;
        tileY = y * TILE_HEIGHT - (y >> 1) * TILE_HEIGHT / 2f - (y % 2 == 1 ? 0.25f * TILE_HEIGHT : 0);

        switch (mode) {
            case Parameters.MAP_MODE_NORMAL:
                renderer.setColor(type.getBaseColor());
                drawTile(renderer);
                break;
            case Parameters.MAP_MODE_TEMPERATURE: {
                float temp = (temperature + Math.abs(Parameters.TEMPERATURE_MIN) + 1) / (Math.abs(Parameters.TEMPERATURE_MIN) + Math.abs(Parameters.TEMPERATURE_MAX) + 2);
                renderer.setColor(1, 1 - temp, 1 - temp, 1);
                drawTile(renderer);
                break;
            }
            case Parameters.MAP_MODE_HEIGHT: {
                float temp = (float) z / (TileGrid.getMaxZ() + 1);
                renderer.setColor(1 - temp, 1 - temp, 1, 1);
                drawTile(renderer);
                break;
            }
            case Parameters.MAP_MODE_BIOMES:
                renderer.setColor(type.getBiomeColor());
                drawTile(renderer);
                break;
        }
    }

    private void drawTile(ShapeRenderer renderer) {
        renderer.triangle(
                tileX + TILE_WIDTH / 2, tileY + TILE_HEIGHT / 2,
                tileX + TILE_WIDTH / 2, tileY + TILE_HEIGHT,
                tileX + TILE_WIDTH, tileY + TILE_HEIGHT * 3 / 4
        );
        renderer.triangle(
                tileX + TILE_WIDTH / 2, tileY + TILE_HEIGHT / 2,
                tileX + TILE_WIDTH, tileY + TILE_HEIGHT * 3 / 4,
                tileX + TILE_WIDTH, tileY + TILE_HEIGHT / 4
        );
        renderer.triangle(
                tileX + TILE_WIDTH / 2, tileY + TILE_HEIGHT / 2,
                tileX + TILE_WIDTH, tileY + TILE_HEIGHT / 4,
                tileX + TILE_WIDTH / 2, tileY
        );
        renderer.triangle(
                tileX + TILE_WIDTH / 2, tileY + TILE_HEIGHT / 2,
                tileX + TILE_WIDTH / 2, tileY,
                tileX, tileY + TILE_HEIGHT / 4
        );
        renderer.triangle(
                tileX + TILE_WIDTH / 2, tileY + TILE_HEIGHT / 2,
                tileX, tileY + TILE_HEIGHT / 4,
                tileX, tileY + TILE_HEIGHT * 3 / 4
        );
        renderer.triangle(
                tileX + TILE_WIDTH / 2, tileY + TILE_HEIGHT / 2,
                tileX, tileY + TILE_HEIGHT * 3 / 4,
                tileX + TILE_WIDTH / 2, tileY + TILE_HEIGHT
        );
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

    public void increaseZ(int x) {
        z += x;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(int oceanLevel) {
        temperature = MathUtils.lerp(Parameters.TEMPERATURE_MIN, Parameters.TEMPERATURE_MAX, 1 - latitude / 90f);
        temperature *= 1 - Math.abs(oceanLevel - z) / (TileGrid.getMaxZ() + 1f);
        temperature += MathUtils.random(-1, 1);
    }
}
