package com.asteroidsgame.objects;

import org.andengine.entity.sprite.Sprite;

/**
 * @author artem
 */
public class Player {

    private Sprite sprite;
    private PlayerState state;

    public Player() {
        state = PlayerState.OUT_GAME;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }
}
