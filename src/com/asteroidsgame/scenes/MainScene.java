/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asteroidsgame.scenes;

import android.os.AsyncTask;
import android.view.KeyEvent;
import com.asteroidsgame.activities.MainActivity;
import com.asteroidsgame.utils.GameState;
import com.asteroidsgame.utils.LoadingState;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;

/**
 *
 * @author artem
 */
public class MainScene extends Scene {

    private BaseScene menuScene;
    private BaseScene boardScene;
    private GameState gameState;
    private LoadingState loadingState;
    private MainActivity activity;

    public MainScene(MainActivity activity) {
        this.activity = activity;
        loadingState = LoadingState.NOT_LOADED;
        setBackground(new Background(0.2f, 0.2f, 0.0f));
        menuScene = new MenuScene(activity);
        boardScene = new BoardScene(activity);
        attachChild(menuScene);
        attachChild(boardScene);
        new LoadingTask().execute();
    }

    private void showMenuScene() {
        menuScene.show();
        boardScene.hide();
        setHUDVisible(false);
        gameState = GameState.MENU;
    }

    public void showBoardScene() {
        menuScene.hide();
        boardScene.show();
        setHUDVisible(true);
        gameState = GameState.BOARD;
    }

    private void setHUDVisible(boolean value) {
        activity.getCamera().getHUD().setVisible(value);
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        //   LogUtils.d("loadingState = "+loadingState +" | "+gameState);
        switch (loadingState) {
            case LOADED:
                switch (gameState) {
                    case MENU:
                        return menuScene.onSceneTouchEvent(pSceneTouchEvent);
                    case BOARD:
                        return boardScene.onSceneTouchEvent(pSceneTouchEvent);
                }
                break;

        }
        return super.onSceneTouchEvent(pSceneTouchEvent);
    }

    public void KeyPressed(int keyCode, KeyEvent event) {
        switch (loadingState) {
            case LOADED:
                switch (gameState) {
                    case MENU:
                        activity.closeActivity();
                        break;
                    case BOARD:
                        showMenuScene();
                        break;
                }
                break;
        }
    }

    private class LoadingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            loadingState = LoadingState.LOADING;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            showMenuScene();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            loadingState = LoadingState.LOADED;
        }
    }
}
