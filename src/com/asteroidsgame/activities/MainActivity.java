/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asteroidsgame.activities;

import android.view.KeyEvent;
import com.asteroidsgame.scenes.MainScene;
import com.asteroidsgame.utils.GameResources;
import java.io.IOException;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 *
 * @author artem
 */
public class MainActivity extends SimpleBaseGameActivity {

    public final static int TEXTURES_WIDTH = 960;
    public final static int TEXTURES_HEIGHT = 600;
    private MainScene mainScene;
    private Camera camera;

    @Override
    protected void onCreateResources() throws IOException {
        GameResources.getInstance().loadButtonRegion(this);
        GameResources.getInstance().loadFonts(this);
    }

    @Override
    protected Scene onCreateScene() {
        mainScene = new MainScene(this);
        mEngine.registerUpdateHandler(new FPSLogger());
        return mainScene;
    }

    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, TEXTURES_WIDTH, TEXTURES_HEIGHT);
        final EngineOptions engineOptions =
                new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);

        engineOptions.getTouchOptions().setNeedsMultiTouch(true);

        return engineOptions;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void closeActivity() {
        finish();
    }

    public Camera getCamera() {
        return camera;
    }

    public MainScene getMainScene() {
        return mainScene;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mainScene.KeyPressed(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
