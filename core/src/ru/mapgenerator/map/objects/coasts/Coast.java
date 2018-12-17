package ru.mapgenerator.map.objects.coasts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mapgenerator.Main;
import ru.mapgenerator.Parameters;

import static ru.mapgenerator.utils.GridUtils.getRotation;

class Coast {

    private final float width, height;
    private final int dest;
    private final Texture texture;

    Coast(int dest) {
        this.dest = dest;
        texture = ((Main) Gdx.app.getApplicationListener()).assetManager.get("map_objects/coast.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        width = Parameters.TILE_WIDTH;
        height = Parameters.TILE_HEIGHT;
    }

    void render(SpriteBatch spriteBatch, float tileX, float tileY) {
        float rotation = getRotation(dest);
        spriteBatch.draw(texture, tileX, tileY, width / 2f, height / 2f, width, height,
                1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }
}
