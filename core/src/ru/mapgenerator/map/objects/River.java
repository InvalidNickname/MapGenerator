package ru.mapgenerator.map.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;

import static ru.mapgenerator.utils.GridUtils.getRotation;

public class River {

    private final float width, height;
    private final int fromDst, toDst;
    private final Texture[] textures;

    public River(int fromDst, int toDst) {
        this.fromDst = fromDst;
        this.toDst = toDst;
        textures = new Texture[4];
        textures[0] = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_0.png");
        textures[0].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures[1] = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_1.png");
        textures[1].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures[2] = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_2.png");
        textures[2].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures[3] = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_3.png");
        textures[3].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        width = Parameters.TILE_WIDTH;
        height = Parameters.TILE_HEIGHT;
    }

    public River(int toDst) {
        fromDst = -1;
        this.toDst = toDst;
        textures = new Texture[4];
        textures[0] = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_0.png");
        textures[0].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures[1] = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_1.png");
        textures[1].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures[2] = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_2.png");
        textures[2].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures[3] = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/river_3.png");
        textures[3].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        width = Parameters.TILE_WIDTH;
        height = Parameters.TILE_HEIGHT;
    }

    public void render(SpriteBatch spriteBatch, float tileX, float tileY) {
        if (fromDst == -1) {
            spriteBatch.draw(textures[3], tileX, tileY, width / 2f, height / 2f, width, height,
                    1, 1, getRotation(toDst), 0, 0, textures[3].getWidth(), textures[3].getHeight(), false, false);
        } else if (Math.abs(fromDst - toDst) == 1 || Math.abs(fromDst - toDst) == 5) {
            float rotation = getRotation(fromDst);
            if (toDst >= fromDst && (fromDst != 0 || toDst != 5)) {
                rotation -= 60;
            }
            spriteBatch.draw(textures[1], tileX, tileY, width / 2f, height / 2f, width, height,
                    1, 1, rotation, 0, 0, textures[1].getWidth(), textures[1].getHeight(), false, false);
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
            spriteBatch.draw(textures[2], tileX, tileY, width / 2f, height / 2f, width, height,
                    1, 1, rotation, 0, 0, textures[2].getWidth(), textures[2].getHeight(), false, false);
        } else {
            float rotation = getRotation(fromDst);
            spriteBatch.draw(textures[0], tileX, tileY, width / 2f, height / 2f, width, height,
                    1, 1, rotation, 0, 0, textures[0].getWidth(), textures[0].getHeight(), false, false);
        }
    }
}
