package ru.mapgenerator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.MapInputProcessor;
import ru.mapgenerator.map.ui.Grid;
import ru.mapgenerator.map.ui.TileInfoList;

import static ru.mapgenerator.Main.SCREEN_HEIGHT;
import static ru.mapgenerator.Main.SCREEN_WIDTH;
import static ru.mapgenerator.Parameters.MAP_HEIGHT;
import static ru.mapgenerator.Parameters.MAP_WIDTH;

public class MapScreen implements Screen {

    public static float minZoom;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer renderer;
    private final OrthographicCamera camera;
    private final Grid grid;
    private final Stage stage;
    private final TileInfoList tileInfoList;
    private int mapMode;

    public MapScreen(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        tileInfoList = new TileInfoList();
        stage = new Stage(new ScreenViewport());
        stage.addActor(tileInfoList);
        grid = new Grid(MAP_HEIGHT, MAP_WIDTH);
        camera = new OrthographicCamera(stage.getViewport().getScreenWidth() / 2f, stage.getViewport().getScreenHeight() / 2f);
        float camX = Parameters.TILE_WIDTH * (0.5f + MAP_WIDTH) / 2;
        float camY = 0.125f * Parameters.TILE_HEIGHT * (1 + 3 * MAP_HEIGHT);
        camera.position.set(camX, camY, 0);
        camera.update();
        minZoom = camY * 2 / camera.viewportHeight;
        camera.zoom = minZoom;
        Gdx.input.setInputProcessor(new MapInputProcessor(camera));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        handleInput();
        camera.update();
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setProjectionMatrix(camera.combined);
        Vector3 lowerLeftCoordinates = camera.unproject(new Vector3(0, SCREEN_HEIGHT, 0));
        Vector2 lowerLeftTile = grid.getTileByCoordinates(lowerLeftCoordinates.x, lowerLeftCoordinates.y);
        Vector3 upperRightCoordinates = camera.unproject(new Vector3(SCREEN_WIDTH, 0, 0));
        Vector2 upperRightTile = grid.getTileByCoordinates(upperRightCoordinates.x, upperRightCoordinates.y);
        grid.render(spriteBatch, renderer, mapMode, (int) lowerLeftTile.x - 1, (int) lowerLeftTile.y - 1, (int) upperRightTile.x + 2, (int) upperRightTile.y + 2);
        renderer.end();
        spriteBatch.end();
        stage.draw();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            mapMode = Parameters.MAP_MODE_NORMAL;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            mapMode = Parameters.MAP_MODE_TEMPERATURE;
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            mapMode = Parameters.MAP_MODE_HEIGHT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            mapMode = Parameters.MAP_MODE_BIOMES;
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            // получение реальных координат точки нажатия
            camera.unproject(touchPos);
            grid.updateSelection(touchPos, tileInfoList);
        }
        // отдаление
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            camera.zoom += 0.02;
        }
        // приближение
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.getX() < 5) {
            camera.translate(-6 * camera.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.getX() > SCREEN_WIDTH - 5) {
            camera.translate(6 * camera.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.getY() > SCREEN_HEIGHT - 5) {
            camera.translate(0, -6 * camera.zoom, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.getY() < 5) {
            camera.translate(0, 6 * camera.zoom, 0);
        }
        // ограничение зума
        if (camera.zoom < 0.5) camera.zoom = 0.5f;
        if (camera.zoom > minZoom) camera.zoom = minZoom;
        // ограничение перемещения по вертикали
        if (camera.position.y < SCREEN_HEIGHT * camera.zoom / 4)
            camera.position.y = SCREEN_HEIGHT * camera.zoom / 4;
        if (camera.position.y > (0.25 * Parameters.TILE_HEIGHT * (1 + 3 * MAP_HEIGHT)) - SCREEN_HEIGHT / 4f * camera.zoom)
            camera.position.y = (0.25f * Parameters.TILE_HEIGHT * (1 + 3 * MAP_HEIGHT)) - SCREEN_HEIGHT / 4f * camera.zoom;
        // бесконечная прокрутка по горизонтали
        if (camera.position.x <= -SCREEN_WIDTH * camera.zoom / 4)
            camera.position.x = (Parameters.TILE_WIDTH * (MAP_WIDTH)) - SCREEN_WIDTH / 4f * camera.zoom;
        if (camera.position.x >= (Parameters.TILE_WIDTH * (0.5f + MAP_WIDTH)) + SCREEN_WIDTH * camera.zoom / 4f)
            camera.position.x = SCREEN_WIDTH * camera.zoom / 4f + Parameters.TILE_WIDTH * 0.5f;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
