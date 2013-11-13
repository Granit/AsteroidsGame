/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asteroidsgame.scenes;

import com.asteroidsgame.activities.MainActivity;
import com.asteroidsgame.utils.GameResources;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.sprite.Sprite;

/**
 *
 * @author artem
 */
public class BaseScene extends CameraScene {

    protected MainActivity activity;

    public BaseScene(MainActivity activity) {
        super(activity.getCamera());
        this.activity = activity;
        GameResources.getInstance().loadBackground(activity);
        Sprite backgroundSprite = new Sprite(0, 0, activity.getCamera().getWidth() * 2, activity.getCamera().getHeight() * 2, GameResources.getInstance().getBackgroundRegion(),
                activity.getVertexBufferObjectManager());
        attachChild(backgroundSprite);
    }

    public void show() {
        setVisibility(true);
    }

    public void hide() {
        setVisibility(false);
    }

    private void setVisibility(boolean value) {
        setVisible(value);
        setIgnoreUpdate(!value);

    }
}
