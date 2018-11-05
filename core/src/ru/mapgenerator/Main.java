package ru.mapgenerator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mapgenerator.screens.MapScreen;
import ru.mapgenerator.screens.SplashScreen;

public class Main extends Game {

    public AssetManager assetManager;
    private SpriteBatch spriteBatch;
    private SplashScreen splashScreen;
    private MapScreen mapScreen;

    @Override
    public void create() {
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
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        if (splashScreen != null) splashScreen.dispose();
        assetManager.dispose();
    }
}
