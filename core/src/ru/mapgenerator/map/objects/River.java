package ru.mapgenerator.map.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;

public class River {

    private int fromDst, toDst;
    private Texture texture, texture1, texture2, texture3;

    public River(int fromDst, int toDst) {
        this.fromDst = fromDst;
        this.toDst = toDst;
        texture = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river.png");
        texture1 = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_0.png");
        texture2 = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_1.png");
        texture3 = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_2.png");
    }

    public River(int toDst) {
        fromDst = -1;
        this.toDst = toDst;
        texture = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river.png");
        texture1 = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_0.png");
        texture2 = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_1.png");
        texture3 = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_2.png");
    }

    public void render(SpriteBatch spriteBatch, float tileX, float tileY) {
        if (fromDst == -1) {
            spriteBatch.draw(texture, tileX, tileY,
                    (Parameters.TILE_WIDTH + 0.5f) / 2,
                    (Parameters.TILE_HEIGHT + 0.5f) / 2,
                    Parameters.TILE_WIDTH + 0.5f,
                    Parameters.TILE_HEIGHT + 0.5f,
                    1, 1, getRotation(toDst), 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        } else if (Math.abs(fromDst - toDst) == 1 || Math.abs(fromDst - toDst) == 5) {
            float rotation = getRotation(fromDst);
            if (toDst >= fromDst && (fromDst != 0 || toDst != 5)) {
                rotation -= 60;
            }
            spriteBatch.draw(texture2, tileX, tileY,
                    (Parameters.TILE_WIDTH + 0.5f) / 2,
                    (Parameters.TILE_HEIGHT + 0.5f) / 2,
                    Parameters.TILE_WIDTH + 0.5f,
                    Parameters.TILE_HEIGHT + 0.5f,
                    1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        } else if (Math.abs(fromDst - toDst) == 2 || Math.abs(fromDst - toDst) == 4) {
            float rotation = getRotation(fromDst);
            if ((fromDst == 0 && toDst == 2)
                    || (fromDst == 1 && toDst == 3)
                    || (fromDst == 2 && toDst == 4)
                    || (fromDst == 3 && toDst == 5)
                    || (fromDst == 4 && toDst == 0)
                    || (fromDst == 5 && toDst == 1)) {
                rotation -= 120;
            }
            spriteBatch.draw(texture3, tileX, tileY,
                    (Parameters.TILE_WIDTH + 0.5f) / 2,
                    (Parameters.TILE_HEIGHT + 0.5f) / 2,
                    Parameters.TILE_WIDTH + 0.5f,
                    Parameters.TILE_HEIGHT + 0.5f,
                    1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        } else {
            float rotation = getRotation(fromDst);
            spriteBatch.draw(texture1, tileX, tileY,
                    (Parameters.TILE_WIDTH + 0.5f) / 2,
                    (Parameters.TILE_HEIGHT + 0.5f) / 2,
                    Parameters.TILE_WIDTH + 0.5f,
                    Parameters.TILE_HEIGHT + 0.5f,
                    1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        }
    }

    private float getRotation(int destination) {
        switch (destination) {
            case 0:
                return 180;
            case 1:
                return 120;
            case 2:
                return 60;
            case 3:
                return 0;
            case 4:
                return 300;
            case 5:
                return 240;
            default:
                return 0;
        }
    }
}
