package ru.mapgenerator.map.objects.coasts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Coastline {

    private boolean[] coasts;

    public Coastline(boolean[] coasts) {
        this.coasts = coasts;
    }

    public void render(SpriteBatch spriteBatch, float tileX, float tileY) {
        for (int i = 0; i < 6; i++)
            if (coasts[i])
                (new Coast(i)).render(spriteBatch, tileX, tileY);
    }

    public void addCoast(int dest) {
        coasts[dest] = true;
    }

}
