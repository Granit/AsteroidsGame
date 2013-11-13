package com.asteroidsgame.pools;

import com.asteroidsgame.activities.MainActivity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.pool.GenericPool;

/**
 * @author artem
 */
public class AsteroidPool extends GenericPool<Sprite> {

    private TiledTextureRegion textureRegion;
    private BaseGameActivity activity;

    public AsteroidPool(MainActivity activity, TiledTextureRegion textureRegion) {
        this.activity = activity;
        this.textureRegion = textureRegion;
    }

    @Override
    protected Sprite onAllocatePoolItem() {
        AnimatedSprite sprite = new AnimatedSprite(0f, 0f, textureRegion.deepCopy(), activity.getVertexBufferObjectManager());
        sprite.animate(100);
        return sprite;
    }

    @Override
    protected void onHandleRecycleItem(final Sprite projectile) {
        projectile.clearEntityModifiers();
        projectile.clearUpdateHandlers();
        projectile.setVisible(false);
        projectile.detachSelf();
        projectile.reset();
    }
}
