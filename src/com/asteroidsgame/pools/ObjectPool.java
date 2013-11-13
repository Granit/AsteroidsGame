package com.asteroidsgame.pools;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.pool.GenericPool;

/**
 * @author artem
 */
public class ObjectPool extends GenericPool<Sprite> {

    private TextureRegion textureRegion;
    private BaseGameActivity activity;

    public ObjectPool(BaseGameActivity activity, TextureRegion textureRegion) {
        this.activity = activity;
        this.textureRegion = textureRegion;
    }

    @Override
    protected Sprite onAllocatePoolItem() {
        return new Sprite(0f, 0f, textureRegion.deepCopy(), activity.getVertexBufferObjectManager());
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
