package ru.mapgenerator.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class DrawableString {

    private float height;
    private String text;
    private BitmapFont font;
    private int x, y;

    public DrawableString(String text, BitmapFont font, int x, int y) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.font = font;
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, text);
        height = layout.height;
    }

    public void render(Batch batch) {
        font.draw(batch, text, x, y + height / 2);
    }
}
