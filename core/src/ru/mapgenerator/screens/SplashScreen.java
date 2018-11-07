package ru.mapgenerator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mapgenerator.Main;

import static ru.mapgenerator.Main.SCREEN_HEIGHT;
import static ru.mapgenerator.Main.SCREEN_WIDTH;

public class SplashScreen implements Screen {

    private final SpriteBatch spriteBatch;
    private final OrthographicCamera camera;
    private final Texture bar, background;
    private final AssetManager assetManager;
    private OnLoadListener onLoadListener;

    public SplashScreen(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        assetManager = ((Main) Gdx.app.getApplicationListener()).assetManager;
        background = new Texture(Gdx.files.internal("splashscreen/background.png"));
        bar = new Texture(Gdx.files.internal("splashscreen/bar.jpg"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void loadData() {
        assetManager.load("tiles/selected_hex.png", Texture.class);
        assetManager.load("tiles/basic_hex.png", Texture.class);
        assetManager.load("map_objects/river_0.png", Texture.class);
        assetManager.load("map_objects/river_1.png", Texture.class);
        assetManager.load("map_objects/river_2.png", Texture.class);
        assetManager.load("map_objects/river_3.png", Texture.class);
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Override
    public void show() {
        loadData();
    }

    @Override
    public void render(float delta) {
        if (assetManager.update()) {
            assetManager.finishLoading();
            onLoadListener.loaded();
        }
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        spriteBatch.draw(bar, 21, 21, 318 * assetManager.getProgress(), 21);
        spriteBatch.end();
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
        bar.dispose();
        background.dispose();
    }

    public interface OnLoadListener {
        void loaded();
    }
}
