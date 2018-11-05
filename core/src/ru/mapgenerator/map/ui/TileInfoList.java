package ru.mapgenerator.map.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.objects.tiles.Tile;
import ru.mapgenerator.utils.DrawableString;

public class TileInfoList extends Actor {

    private BitmapFont font;
    private DrawableString tileCoordinates, tileType, tileTemperature;
    private ShapeRenderer shapeRenderer;

    public TileInfoList() {
        shapeRenderer = new ShapeRenderer();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/abel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.characters = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNMёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ1234567890:°-.";
        font = generator.generateFont(parameter);
        font.setColor(Color.LIGHT_GRAY);
        generator.dispose();
    }

    void setTile(Tile tile) {
        String latitudeText = (int) tile.getLatitude() + (tile.getY() > Parameters.MAP_HEIGHT / 2 ? " °N" : " °S");
        String longitudeText = (int) tile.getLongitude() + (tile.getX() > Parameters.MAP_WIDTH / 2 ? " °E" : " °W");
        tileCoordinates = new DrawableString(latitudeText + " " + longitudeText, font, 20, 30);
        tileType = new DrawableString(tile.getStringType(), font, 20, 90);
        tileTemperature = new DrawableString((int) tile.getTemperature() + " °C", font, 20, 60);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (tileCoordinates != null) {
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.5f);
            shapeRenderer.rect(0, 0, 250, 120);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL30.GL_BLEND);
            batch.end();
            batch.begin();
            tileCoordinates.render(batch);
            tileType.render(batch);
            tileTemperature.render(batch);
        }
    }
}
