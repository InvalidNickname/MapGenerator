package ru.mapgenerator.map.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.generator.Generator;
import ru.mapgenerator.map.TileGrid;

import static ru.mapgenerator.Parameters.MAP_HEIGHT;
import static ru.mapgenerator.Parameters.MAP_WIDTH;

public class Grid {

    private final TileGrid tileGrid;
    private final Texture selectedTexture;
    private int selectedX, selectedY;

    public Grid(int height, int width) {
        selectedX = -1;
        selectedY = -1;
        Generator generator = new Generator(height, width);
        tileGrid = generator.generate();
        selectedTexture = ((Main) Gdx.app.getApplicationListener()).assetManager.get("tiles/selected_hex.png");
        selectedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void render(SpriteBatch spriteBatch, int mode, int x0, int y0, int x1, int y1) {
        if (y0 < 0) y0 = 0;
        if (y1 > MAP_HEIGHT - 1) y1 = MAP_HEIGHT - 1;
        if (x0 < 0) {
            for (int i = y0; i < y1; i++) {
                for (int j = 0; j < x1; j++)
                    tileGrid.getTile(j, i).render(spriteBatch, mode, j, i);
                for (int j = MAP_WIDTH + x0; j < MAP_WIDTH; j++)
                    tileGrid.getTile(j, i).render(spriteBatch, mode, j - MAP_WIDTH, i);
            }
        } else if (x1 > MAP_WIDTH) {
            x1 = x1 % MAP_WIDTH;
            for (int i = y0; i < y1; i++) {
                for (int j = 0; j < x1; j++)
                    tileGrid.getTile(j, i).render(spriteBatch, mode, j + MAP_WIDTH, i);
                for (int j = x0; j < MAP_WIDTH; j++)
                    tileGrid.getTile(j, i).render(spriteBatch, mode, j, i);
            }
        } else {
            for (int i = y0; i < y1; i++)
                for (int j = x0; j < x1; j++)
                    tileGrid.getTile(j, i).render(spriteBatch, mode, j, i);
        }
        // отрисовка выделения гекса
        if (selectedY >= 0 && selectedX >= 0) {
            spriteBatch.draw(selectedTexture,
                    tileGrid.getTile(selectedX, selectedY).getTileX(),
                    tileGrid.getTile(selectedX, selectedY).getTileY(),
                    Parameters.TILE_WIDTH + 0.5f,
                    Parameters.TILE_HEIGHT + 0.5f);
        }
    }

    public void updateSelection(Vector3 touchPos, TileInfoList tileInfoList) {
        Vector2 tile = getTileByCoordinates(touchPos.x, touchPos.y);
        tile.x = tile.x > MAP_WIDTH - 1 ? tile.x - MAP_WIDTH : (tile.x < 0 ? MAP_WIDTH + tile.x : tile.x);
        if (tile.y >= 0 && tile.y < MAP_HEIGHT) {
            selectedY = (int) tile.y;
            selectedX = (int) tile.x;
            tileInfoList.setTile(tileGrid.getTile(selectedX, selectedY));
        }
    }

    public Vector2 getTileByCoordinates(float x, float y) {

        float tempY = y / (Parameters.TILE_HEIGHT / 2);
        double relativeY = tempY - (int) (tempY / 1.5) * 1.5;
        int tileY;

        float tempX = x / (Parameters.TILE_WIDTH);
        double relativeX = tempX - (int) tempX;
        int tileX;

        if (relativeY >= 0.5 && relativeY < 1.5)
            // нажатие на центральную часть гекса
            tileY = (int) (tempY / 1.5);
        else {
            // нажатие на верхнюю или нижнюю часть гекса
            if (tempY - (int) tempY > 0.5 && tempY - (int) tempY < 1) // четный ряд
                if (relativeX < 0.5) {
                    if (relativeY / relativeX < 1)
                        tileY = (int) (tempY / 1.5) - 1;
                    else tileY = (int) (tempY / 1.5);
                } else {
                    if (relativeY < 1 - relativeX)
                        tileY = (int) (tempY / 1.5) - 1;
                    else tileY = (int) (tempY / 1.5);
                }
            else { // нечетный ряд
                if (relativeX < 0.5) {
                    relativeX += 0.5;
                    if (relativeY < 1 - relativeX)
                        tileY = (int) (tempY / 1.5) - 1;
                    else tileY = (int) (tempY / 1.5);
                } else {
                    relativeX -= 0.5;
                    if (relativeY / relativeX < 1)
                        tileY = (int) (tempY / 1.5) - 1;
                    else tileY = (int) (tempY / 1.5);
                }
            }
        }

        if (tileY % 2 == 0) {
            tileX = (int) tempX;
        } else {
            tileX = (int) (tempX - 0.5);
        }

        return new Vector2(tileX, tileY);
    }
}
