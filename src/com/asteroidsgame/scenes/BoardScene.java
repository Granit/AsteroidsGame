package com.asteroidsgame.scenes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.opengl.GLES20;
import android.view.KeyEvent;
import com.asteroidsgame.activities.MainActivity;
import com.asteroidsgame.activities.R;
import com.asteroidsgame.objects.Player;
import com.asteroidsgame.objects.PlayerState;
import com.asteroidsgame.pools.AsteroidPool;
import com.asteroidsgame.pools.ObjectPool;
import com.asteroidsgame.score.ScoreManager;
import com.asteroidsgame.utils.BoardState;
import com.asteroidsgame.utils.GameResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;

/**
 *
 * @author artem
 */
public class BoardScene extends BaseScene {

    private Player player;
    private PhysicsHandler physicsHandler;
    private float x1 = 0;
    private BoardState state;
    private ObjectPool bulletPool;
    private AsteroidPool asteroidPool;
    private ArrayList<Sprite> bullets;
    private ArrayList<Sprite> asteroids;
    private float rad;
    private ScoreManager scoreManager;

    public BoardScene(MainActivity activity) {
        super(activity);
        init();
        startGame();

    }

    private void startGame() {
        bullets = new ArrayList<Sprite>();
        asteroids = new ArrayList<Sprite>();
        attachChild(player.getSprite());
        state = BoardState.NOT_PAUSED;
    }

    private void init() {
        if (MultiTouch.isSupported(activity) && MultiTouch.isSupportedDistinct(activity)) {
            initPlayer();
            initHandler();
            initBackGround();
            initControls();
            initBulletPool();
            initExplosion();
            initAsteroidPool();
            initScore();
        }

    }

    private void initPlayer() {
        GameResources.getInstance().loadSpaceShip(activity);
        ITextureRegion spaceshipRegion = GameResources.getInstance().getSpaceshipRegion();
        float centerX = (activity.getCamera().getWidth() - spaceshipRegion.getWidth()) / 2;
        float centerY = (activity.getCamera().getHeight() - spaceshipRegion.getHeight()) / 2;
        Sprite spaceship = new Sprite(centerX, centerY, spaceshipRegion, activity.getVertexBufferObjectManager());
        physicsHandler = new PhysicsHandler(spaceship);
        spaceship.registerUpdateHandler(physicsHandler);
        player = new Player();
        player.setSprite(spaceship);
        player.setState(PlayerState.IN_GAME);

    }

    private void initHandler() {
        IUpdateHandler handler = new IUpdateHandler() {
            public void onUpdate(float f) {
                if (state == BoardState.NOT_PAUSED) {
                    for (int i = 0; i < bullets.size(); i++) {
                        if (bullets.get(i).getX() <= activity.getCamera().getXMin() || bullets.get(i).getX() >= activity.getCamera().getXMax()
                                || bullets.get(i).getY() <= activity.getCamera().getYMin() || bullets.get(i).getY() >= activity.getCamera().getYMax()) {
                            bulletPool.recyclePoolItem(bullets.get(i));
                            bullets.set(i, null);

                        }
                    }
                    bullets.removeAll(Collections.singleton(null));
                    bullets.trimToSize();

                    for (int i = 0; i < asteroids.size(); i++) {
                        if (asteroids.get(i).getX() <= activity.getCamera().getXMin() || asteroids.get(i).getX() >= activity.getCamera().getXMax()
                                || asteroids.get(i).getY() <= activity.getCamera().getYMin() || asteroids.get(i).getY() >= activity.getCamera().getYMax()) {
                            asteroidPool.recyclePoolItem(asteroids.get(i));
                            asteroids.set(i, null);
                        }
                    }
                    asteroids.removeAll(Collections.singleton(null));
                    asteroids.trimToSize();

                    for (int j = 0; j < asteroids.size(); j++) {
                        for (int i = 0; i < bullets.size(); i++) {

                            if (asteroids.get(j) != null && bullets.get(i).collidesWith(asteroids.get(j))) {
                                showExplosion(asteroids.get(j).getX(), asteroids.get(j).getY());
                                bulletPool.recyclePoolItem(bullets.get(i));
                                bullets.set(i, null);
                                asteroidPool.recyclePoolItem(asteroids.get(j));
                                asteroids.set(j, null);
                                scoreManager.addScore(1);
                            }
                        }
                        if (asteroids.get(j) != null && player.getSprite().collidesWith(asteroids.get(j))) {
                            showExplosion(player.getSprite().getX(), player.getSprite().getY());
                            player.setState(PlayerState.OUT_GAME);
                            detachChild(player.getSprite());
                            state = BoardState.PAUSED;
                            showResetDialog();
                        }
                    }
                    bullets.removeAll(Collections.singleton(null));
                    bullets.trimToSize();
                    asteroids.removeAll(Collections.singleton(null));
                    asteroids.trimToSize();

                    if (asteroids.size() < 1) {
                        addAsteroid();
                    }
                }
            }

            public void reset() {
            }
        };
        registerUpdateHandler(handler);
    }

    private void initControls() {
        GameResources.getInstance().loadControls(activity);
        AnalogOnScreenControl leftControl = createMovingControl();
        Sprite rightControl = createShootControl();
        registerTouchArea(leftControl);
        registerTouchArea(rightControl);
        setChildScene(leftControl);
        leftControl.attachChild(rightControl);
        setTouchAreaBindingOnActionDownEnabled(true);
    }

    private AnalogOnScreenControl createMovingControl() {
        ITextureRegion controlBaseRegion = GameResources.getInstance().getControlBaseRegion();
        ITextureRegion controlKnobRegion = GameResources.getInstance().getControlKnobRegion();
        x1 = 100;
        float y1 = 100;
        AnalogOnScreenControl control = new AnalogOnScreenControl(x1, y1, activity.getCamera(), controlBaseRegion, controlKnobRegion, 0.1f, 200, activity.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
            @Override
            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
                if (state == BoardState.NOT_PAUSED && player.getState() == PlayerState.IN_GAME) {
                    physicsHandler.setVelocity(pValueX * activity.getCamera().getHeight() / 1.5f, pValueY * activity.getCamera().getHeight() / 1.5f);
                    if (!(pValueX == 0 && pValueY == 0)) {
                        rad = (float) Math.atan2(pValueX, pValueY);
                        float radToDeg = MathUtils.radToDeg((float) rad);
                        player.getSprite().setRotation(radToDeg);
                    }
                }
            }

            @Override
            public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
            }
        });
        control.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        control.getControlBase().setAlpha(0.5f);

        return control;
    }

    private Sprite createShootControl() {
        ITextureRegion controlBaseRegion = GameResources.getInstance().getControlBaseRegion();
        ITextureRegion controlShootRegion = GameResources.getInstance().getControlShootRegion();
        float x2 = activity.getCamera().getWidth() - controlBaseRegion.getWidth() + 25;
        float y2 = 100;
        Sprite control = new Sprite(x2, y2, controlShootRegion, activity.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (state == BoardState.NOT_PAUSED && player.getState() == PlayerState.IN_GAME) {
                    if (pSceneTouchEvent.isActionDown()) {
                        shootBullet();
                    }
                    return true;
                } else {
                    return false;
                }

            }
        };
        return control;

    }

    public boolean isPaused() {
        return state == BoardState.PAUSED;
    }

    public void setPaused() {
        state = BoardState.PAUSED;
    }

    private void initBulletPool() {
        GameResources.getInstance().loadBullet(activity);
        bulletPool = new ObjectPool(activity, GameResources.getInstance().getBulletRegion());
    }

    private void initAsteroidPool() {
        GameResources.getInstance().loadAsteroid(activity);
        asteroidPool = new AsteroidPool(activity, GameResources.getInstance().getAsteroidRegion());
    }

    private void initExplosion() {
        GameResources.getInstance().loadExplosion(activity);
    }

    private void initScore() {
        scoreManager = new ScoreManager(activity);
        scoreManager.reset();
        HUD hud = new HUD();
        Text scoreText = scoreManager.getScoreText();
        scoreText.setPosition(activity.getCamera().getXMin() + 100, activity.getCamera().getYMax() - 50);
        hud.attachChild(scoreText);
        activity.getCamera().setHUD(hud);
    }

    private Sprite createBullet() {
        Sprite bullet = bulletPool.obtainPoolItem();
        bullets.add(bullet);
        bullet.setPosition(player.getSprite().getX(), player.getSprite().getY());
        attachChild(bullet);
        return bullet;
    }

    private void shootBullet() {
        Sprite bullet = createBullet();
        float maxX = (float) 1.0 * activity.getCamera().getXMax();
        float maxY = (float) 1.0 * activity.getCamera().getYMax();
        float fromX = bullet.getX();
        float fromY = bullet.getY();

        float toX, toX1, toX2, toY, toY1, toY2;
        float dX, dY;

        if (rad > 0 && rad <= Math.PI) {
            toX2 = maxX;
        } else {
            toX2 = 0;
        }

        if (rad > -Math.PI / 2 && rad <= Math.PI / 2) {
            toY1 = maxY;
        } else {
            toY1 = 0;
        }



        //      if (rad > 0 && rad <= Math.PI / 2) {
        dX = (float) (Math.abs(toY1 - fromY) * Math.tan(rad));
        toX1 = fromX + dX;
        toY2 = (float) (fromY + (maxX - fromX) / Math.tan(rad));
        //      }
        if (rad > -Math.PI / 2 && rad <= 0) {
            dX = (float) (Math.abs(toY1 - fromY) / Math.tan(Math.PI / 2 - Math.abs(rad)));
            toX1 = fromX - dX;
            toY2 = (float) (fromY + (maxX - fromX) * Math.tan(Math.PI / 2 - Math.abs(rad)));
        }
        if (rad > -Math.PI && rad <= -Math.PI / 2) {
            dX = (float) (Math.abs(fromY - toY1) * Math.tan(Math.PI - Math.abs(rad)));
            toX1 = fromX - dX;
            toY2 = (float) (fromY - fromX / Math.tan(Math.PI - Math.abs(rad)));
        }
        if (rad > Math.PI / 2 && rad <= Math.PI) {
            dX = (float) (Math.abs(fromY - toY1) / Math.tan(rad - Math.PI / 2));
            toX1 = fromX + dX;
            toY2 = (float) (fromY - (maxX - fromX) * Math.tan(rad - Math.PI / 2));
        }

        if (countLenght((toX1 - fromX), (toY1 - fromY)) < countLenght((toX2 - fromX), (toY2 - fromY))) {
            toX = toX1;
            toY = toY1;
        } else {
            toX = toX2;
            toY = toY2;
        }

        float length = (float) Math.sqrt(((toX - fromX) * (toX - fromX))
                + ((toY - fromY) * (toY - fromY)));
        float velocity = 480.0f / 1.0f; // 480 pixels / 1 sec
        float realMoveDuration = length / velocity;
        MoveModifier mod = new MoveModifier(realMoveDuration, fromX, fromY, toX, toY);
        bullet.registerEntityModifier(mod);
    }

    private void addAsteroid() {
        float fromX;
        float fromY;
        float centerX;
        float centerY;
        float toX;
        float toY;
        switch (randomFromRange(1, 2)) {
            case 1: //left or right
                fromX = randomFromArray(new float[]{0, activity.getCamera().getWidth()});
                fromY = randomFromRange(0, (int) activity.getCamera().getHeight());
                break;
            default:
                fromX = randomFromRange(0, (int) activity.getCamera().getWidth());
                fromY = randomFromArray(new float[]{0, activity.getCamera().getHeight()});
                break;
        }
        centerX = randomFromRange((int) activity.getCamera().getWidth() / 3, (int) activity.getCamera().getWidth() * 2 / 3);
        centerY = randomFromRange((int) activity.getCamera().getHeight() / 3, (int) activity.getCamera().getHeight() * 2 / 3);

        toX = (fromX > centerX) ? fromX - 3 * centerX - 20 : fromX + 3 * centerX + 20;
        toY = (fromY > centerY) ? fromY - 3 * centerY - 20 : fromY + 3 * centerY + 20;
        addAsteroid(fromX, fromY, toX, toY);
    }

    private void addAsteroid(float fromX, float fromY, float toX, float toY) {
        Sprite asteroid = asteroidPool.obtainPoolItem();
        asteroids.add(asteroid);
        asteroid.setPosition(fromX, fromY);
        attachChild(asteroid);
        float speed = randomFromRange(50, 60) / 10;
        MoveModifier mod = new MoveModifier(speed, fromX, fromY, toX, toY);
        asteroid.registerEntityModifier(mod.deepCopy());
    }

    private int randomFromRange(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    private float randomFromArray(float[] array) {
        return array[new Random().nextInt(array.length)];
    }

    private float countLenght(float x, float y) {
        return (float) Math.sqrt(x * x) + (y * y);
    }

    private void showExplosion(float x, float y) {
        final AnimatedSprite explosion = createExplosionSprite();
        explosion.animate(100);
        explosion.setPosition(x, y);
        attachChild(explosion);
        explosion.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if (explosion.getCurrentTileIndex() == 24) {
                    activity.runOnUpdateThread(new Runnable() {
                        public void run() {
                            detachChild(explosion);
                        }
                    });
                }
            }

            @Override
            public void reset() {
            }
        });

    }

    private AnimatedSprite createExplosionSprite() {
        return new AnimatedSprite(0f, 0f, GameResources.getInstance().getExplosionRegion().deepCopy(), activity.getVertexBufferObjectManager());
    }

    private void initBackGround() {
        GameResources.getInstance().loadBackground(activity);
        getChildByIndex(0).attachChild(new Sprite(0, 0, GameResources.getInstance().getBackgroundRegion(), activity.getVertexBufferObjectManager()));

    }

    private void showResetDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("");
                alert.setMessage(String.format(activity.getString(R.string.result), scoreManager.getScore()));
                alert.setPositiveButton(activity.getString(R.string.again), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        resetGame();
                    }
                });
                alert.setNegativeButton(activity.getString(R.string.exit), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        resetGame();
                        activity.onKeyDown(KeyEvent.KEYCODE_BACK, null);
                    }
                });

                alert.show();
            }
        });
    }

    private void resetGame() {
        resetPlayer();
        scoreManager.reset();
        startGame();
    }

    private void resetPlayer() {
        float centerX = (activity.getCamera().getWidth() - player.getSprite().getWidth()) / 2;
        float centerY = (activity.getCamera().getHeight() - player.getSprite().getHeight()) / 2;
        player.getSprite().setPosition(centerX, centerY);
        player.setState(PlayerState.IN_GAME);
    }
}
