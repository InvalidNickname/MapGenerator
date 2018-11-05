package ru.mapgenerator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ru.mapgenerator.Parameters;
import ru.mapgenerator.map.MapInputProcessor;
import ru.mapgenerator.map.ui.Grid;
import ru.mapgenerator.map.ui.TileInfoList;

public class MapScreen implements Screen {

    public static float minZoom;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Grid grid;
    private Stage stage;
    private TileInfoList tileInfoList;
    private int mapMode;

    public MapScreen(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        tileInfoList = new TileInfoList();
        stage = new Stage(new ExtendViewport(Parameters.SCREEN_WIDTH, Parameters.SCREEN_HEIGHT));
        stage.addActor(tileInfoList);
        grid = new Grid(Parameters.MAP_HEIGHT, Parameters.MAP_WIDTH);
        camera = new OrthographicCamera(Parameters.SCREEN_WIDTH / 2f, Parameters.SCREEN_HEIGHT / 2f);
        float camX = Parameters.TILE_WIDTH * (0.5f + Parameters.MAP_WIDTH) / 2;
        float camY = 0.125f * Parameters.TILE_HEIGHT * (1 + 3 * Parameters.MAP_HEIGHT);
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        grid.render(spriteBatch, mapMode);
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
            camera.translate(-3 * camera.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.getX() > Parameters.SCREEN_WIDTH - 5) {
            camera.translate(3 * camera.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.getY() > Parameters.SCREEN_HEIGHT - 5) {
            camera.translate(0, -3 * camera.zoom, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.getY() < 5) {
            camera.translate(0, 3 * camera.zoom, 0);
        }
        // ограничение зума
        if (camera.zoom < 0.5) camera.zoom = 0.5f;
        if (camera.zoom > minZoom) camera.zoom = minZoom;
        // ограничение перемещения по вертикали
        if (camera.position.y < Parameters.SCREEN_HEIGHT * camera.zoom / 4)
            camera.position.y = Parameters.SCREEN_HEIGHT * camera.zoom / 4;
        if (camera.position.y > (0.25 * Parameters.TILE_HEIGHT * (1 + 3 * Parameters.MAP_HEIGHT)) - Parameters.SCREEN_HEIGHT / 4f * camera.zoom)
            camera.position.y = (0.25f * Parameters.TILE_HEIGHT * (1 + 3 * Parameters.MAP_HEIGHT)) - Parameters.SCREEN_HEIGHT / 4f * camera.zoom;
        // ограничение перемещения по горизонтали
        if (camera.position.x < Parameters.SCREEN_WIDTH * camera.zoom / 4)
            camera.position.x = Parameters.SCREEN_WIDTH * camera.zoom / 4;
        if (camera.position.x > (Parameters.TILE_WIDTH * (0.5 + Parameters.MAP_WIDTH)) - Parameters.SCREEN_WIDTH / 4f * camera.zoom)
            camera.position.x = (Parameters.TILE_WIDTH * (0.5f + Parameters.MAP_WIDTH)) - Parameters.SCREEN_WIDTH / 4f * camera.zoom;
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
