/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asteroidsgame.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import com.asteroidsgame.activities.MainActivity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.LinearGradientFillBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.RectangleBitmapTextureAtlasSourceDecoratorShape;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

/**
 *
 * @author artem
 */
public class GameResources {

    private static GameResources gameResources;
    private Font font;
    private Font fontDefault;
    private TiledTextureRegion buttonRegion;
    private ITextureRegion controlBaseRegion;
    private ITextureRegion controlKnobRegion;
    private ITextureRegion controlShootRegion;
    private ITextureRegion spaceshipRegion;
    private TextureRegion bulletRegion;
    private TiledTextureRegion asteroidRegion;
    private TiledTextureRegion explosionRegion;
    private ITextureRegion backgroundRegion;

    public static GameResources getInstance() {
        if (gameResources == null) {
            gameResources = new GameResources();
        }
        return gameResources;

    }

    public void loadFonts(BaseGameActivity activity) {
        BitmapTextureAtlas atlas1 = createAtlas(activity.getTextureManager());
        font = FontFactory.createFromAsset(activity.getFontManager(), atlas1, activity.getAssets(), "fonts/AxeHandel.ttf", 32, true, Color.WHITE);
        activity.getFontManager().loadFont(font);
        BitmapTextureAtlas atlas2 = createAtlas(activity.getTextureManager());
        fontDefault = new Font(activity.getFontManager(), atlas2, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 54, true, Color.BLACK);
        activity.getFontManager().loadFont(fontDefault);
    }

    public Font getFont() {
        return font;
    }

    public Font getFontDefault() {
        return fontDefault;
    }

    public BitmapTextureAtlas createAtlas(TextureManager textureManager) {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager, 128, 128, TextureOptions.NEAREST_PREMULTIPLYALPHA);
        atlas.load();
        return atlas;
    }

    public Sprite createBackgroundSprite(MainActivity activity) {
        BitmapTextureAtlas backgroundGradientTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.NEAREST);

        EmptyBitmapTextureAtlasSource bitmap = new EmptyBitmapTextureAtlasSource(512, 512);
        LinearGradientFillBitmapTextureAtlasSourceDecorator gradientSource = new LinearGradientFillBitmapTextureAtlasSourceDecorator(
                bitmap, new RectangleBitmapTextureAtlasSourceDecoratorShape(), Color.parseColor("#C0ECA7"), Color.parseColor("#494b1a"),
                LinearGradientFillBitmapTextureAtlasSourceDecorator.LinearGradientDirection.TOP_TO_BOTTOM);

        TextureRegion backgroundGradientTextureRegion = TextureRegionFactory.createFromSource(backgroundGradientTexture,
                gradientSource, 0, 0);
        backgroundGradientTexture.load();
        Sprite brightBackgroundSprite = new Sprite(0, 0, activity.getCamera().getWidth() * 2, activity.getCamera().getHeight() * 2, backgroundGradientTextureRegion,
                activity.getVertexBufferObjectManager());

        return brightBackgroundSprite;
    }

    public void loadButtonRegion(BaseGameActivity activity) {
        BitmapTextureAtlas buttonAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 204, 250, TextureOptions.NEAREST);
        buttonRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(buttonAtlas, activity.getAssets(), "gfx/button_tile.png", 0, 0, 1, 3);
        buttonAtlas.load();
    }

    public TiledTextureRegion getButtonRegion() {
        return buttonRegion;
    }

    public void loadControls(BaseGameActivity activity) {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
        controlBaseRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, activity, "gfx/onscreen_control_base.png", 0, 0);
        controlKnobRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, activity, "gfx/onscreen_control_knob.png", 128, 0);
        atlas.load();
        atlas = new BitmapTextureAtlas(activity.getTextureManager(), 96, 96, TextureOptions.BILINEAR);
        controlShootRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, activity, "gfx/shoot_control.png", 0, 0);
        atlas.load();
    }

    public ITextureRegion getControlBaseRegion() {
        return controlBaseRegion;
    }

    public ITextureRegion getControlKnobRegion() {
        return controlKnobRegion;
    }

    public ITextureRegion getControlShootRegion() {
        return controlShootRegion;
    }

    public void loadSpaceShip(BaseGameActivity activity) {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(activity.getTextureManager(), 48, 48, TextureOptions.BILINEAR);
        spaceshipRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, activity, "gfx/spaceship_small.png", 0, 0);
        atlas.load();
    }

    public ITextureRegion getSpaceshipRegion() {
        return spaceshipRegion;
    }

    public void loadBullet(BaseGameActivity activity) {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(activity.getTextureManager(), 9, 9, TextureOptions.BILINEAR);
        bulletRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, activity, "gfx/bullet_red.png", 0, 0);
        atlas.load();
    }

    public TextureRegion getBulletRegion() {
        return bulletRegion;
    }

    public void loadAsteroid(BaseGameActivity activity) {
        BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.NEAREST);
        asteroidRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas, activity, "gfx/asteroid.png", 5, 6);
        try {
            atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            atlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException ex) {
            LogUtils.e("loadAsteroid", ex);
        }

    }

    public TiledTextureRegion getAsteroidRegion() {
        return asteroidRegion;
    }

    public void loadExplosion(BaseGameActivity activity) {
        BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 460, 460, TextureOptions.NEAREST);
        explosionRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas, activity, "gfx/explosion.png", 5, 5);
        try {
            atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            atlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException ex) {
            LogUtils.e("loadExplosion", ex);
        }

    }

    public TiledTextureRegion getExplosionRegion() {
        return explosionRegion;
    }

    public void loadBackground(BaseGameActivity activity) {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(activity.getTextureManager(), 1600, 1200);
        backgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, activity, "gfx/universe.png", 0, 0);
        atlas.load();
    }

    public ITextureRegion getBackgroundRegion() {
        return backgroundRegion;
    }
}
