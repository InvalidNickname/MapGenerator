package ru.mapgenerator.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class DrawableString {

    private final float height;
    private final String text;
    private final BitmapFont font;
    private final int x, y;

    public DrawableString(String text, BitmapFont font, int x, int y) {
        this.x = x;
        this.y = y;
        if (text == null) text = "";
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
