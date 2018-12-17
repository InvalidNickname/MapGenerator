package ru.mapgenerator.map.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.mapgenerator.map.objects.tiles.Tile;
import ru.mapgenerator.utils.DrawableString;

import static ru.mapgenerator.Parameters.MAP_HEIGHT;
import static ru.mapgenerator.Parameters.MAP_WIDTH;

public class TileInfoList extends Actor {

    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;
    private DrawableString tileCoordinates, tileType, tileTemperature;

    public TileInfoList() {
        shapeRenderer = new ShapeRenderer();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/abel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.characters = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNMёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ1234567890:°-.";
        font = generator.generateFont(parameter);
        font.setColor(Color.LIGHT_GRAY);
        generator.dispose();

        tileCoordinates = new DrawableString(null, font, 20, 30);
        tileType = new DrawableString("No tile selected", font, 20, 90);
        tileTemperature = new DrawableString(null, font, 20, 60);
    }

    void setTile(Tile tile) {
        String latitudeText = (int) tile.getLatitude() + (tile.getY() > MAP_HEIGHT / 2 ? " °N" : " °S");
        String longitudeText = (int) tile.getLongitude() + (tile.getX() > MAP_WIDTH / 2 ? " °E" : " °W");
        tileCoordinates = new DrawableString(latitudeText + " " + longitudeText, font, 20, 30);
        tileType = new DrawableString(tile.getType().toString(), font, 20, 90);
        tileTemperature = new DrawableString((int) tile.getTemperature() + " °C", font, 20, 60);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(0, 0, 320, 120);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
        batch.end();
        batch.begin();
        tileCoordinates.render(batch);
        tileType.render(batch);
        tileTemperature.render(batch);
    }
}
