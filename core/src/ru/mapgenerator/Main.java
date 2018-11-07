package ru.mapgenerator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mapgenerator.screens.MapScreen;
import ru.mapgenerator.screens.SplashScreen;

public class Main extends Game {

    public AssetManager assetManager;
    private SpriteBatch spriteBatch;
    private SplashScreen splashScreen;
    private MapScreen mapScreen;

    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;

    @Override
    public void create() {
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        spriteBatch = new SpriteBatch();
        assetManager = new AssetManager();
        splashScreen = new SplashScreen(spriteBatch);
        splashScreen.setOnLoadListener(() -> {
            mapScreen = new MapScreen(spriteBatch);
            setScreen(mapScreen);
        });
        setScreen(splashScreen);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        if (splashScreen != null) splashScreen.dispose();
        if (mapScreen != null) mapScreen.dispose();
        assetManager.dispose();
    }
}
