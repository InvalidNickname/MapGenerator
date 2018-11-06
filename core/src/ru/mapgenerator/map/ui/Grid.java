package ru.mapgenerator.map.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.generator.Generator;
import ru.mapgenerator.map.objects.TileGrid;

public class Grid {

    private final TileGrid tileGrid;
    private final int height, width;
    private int selectedX, selectedY;
    private final Texture selectedTexture;

    public Grid(int height, int width) {
        this.height = height;
        this.width = width;
        selectedX = -1;
        selectedY = -1;
        Generator generator = new Generator(height, width);
        tileGrid = generator.generate();
        selectedTexture = ((Main) Gdx.app.getApplicationListener()).assetManager.get("tiles/selected_hex.png");
        selectedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void render(SpriteBatch spriteBatch, int mode) {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                tileGrid.getTile(j, i).render(spriteBatch, mode);
        // отрисовка выделения гекса
        if (selectedY >= 0 && selectedX >= 0) {
            spriteBatch.draw(selectedTexture,
                    tileGrid.getTile(selectedX, selectedY).getTileX(),
                    tileGrid.getTile(selectedX, selectedY).getTileY(),
                    Parameters.TILE_WIDTH,
                    Parameters.TILE_HEIGHT);
        }
    }

    public void updateSelection(Vector3 touchPos, TileInfoList tileInfoList) {
        float tempY = touchPos.y / (Parameters.TILE_HEIGHT / 2);
        double relativeY = tempY - (int) (tempY / 1.5) * 1.5;
        int tileY;

        float tempX = touchPos.x / (Parameters.TILE_WIDTH);
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

        if (tileX >= 0 && tileX < Parameters.MAP_WIDTH && tileY >= 0 && tileY < Parameters.MAP_HEIGHT) {
            selectedY = tileY;
            selectedX = tileX;
            tileInfoList.setTile(tileGrid.getTile(selectedX, selectedY));
        }
    }
}
