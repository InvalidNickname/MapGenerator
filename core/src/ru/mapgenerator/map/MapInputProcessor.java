package ru.mapgenerator.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import static ru.mapgenerator.screens.MapScreen.minZoom;

public class MapInputProcessor implements InputProcessor {

    private final OrthographicCamera camera;

    public MapInputProcessor(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        // координаты до зума
        Vector3 worldCoordinatesBefore = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        camera.zoom += amount * 0.15;
        // ограничение зума
        if (camera.zoom < 0.5) camera.zoom = 0.5f;
        if (camera.zoom > minZoom) camera.zoom = minZoom;
        camera.update();
        // координаты после зума
        Vector3 worldCoordinatesAfter = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        // сдвиг до координат то зума
        Vector3 diff = worldCoordinatesAfter.sub(worldCoordinatesBefore);
        camera.position.sub(diff);
        camera.update();
        return false;
    }


}
