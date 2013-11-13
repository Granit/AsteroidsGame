package com.asteroidsgame.scenes;

import com.asteroidsgame.activities.MainActivity;
import com.asteroidsgame.activities.R;
import com.asteroidsgame.controls.MenuButton;
import com.asteroidsgame.controls.MenuButtonListener;

/**
 *
 * @author artem
 */
public class MenuScene extends BaseScene {

    public MenuScene(MainActivity mainActivity) {
        super(mainActivity);
        MenuButton buttonGame = new MenuButton(R.string.game, mainActivity);
        buttonGame.setPosition((mainActivity.getCamera().getWidth()) / 2, mainActivity.getCamera().getHeight() / 2 + 200);
        registerTouchArea(buttonGame);
        MenuButton buttonTutorial = new MenuButton(R.string.tutorial, mainActivity);
        buttonTutorial.setPosition((mainActivity.getCamera().getWidth()) / 2, mainActivity.getCamera().getHeight() / 2 + 125);
        registerTouchArea(buttonTutorial);
        MenuButton buttonOptions = new MenuButton(R.string.options, mainActivity);
        buttonOptions.setPosition((mainActivity.getCamera().getWidth()) / 2, mainActivity.getCamera().getHeight() / 2 + 50);
        registerTouchArea(buttonOptions);
        MenuButton buttonExit = new MenuButton(R.string.exit, mainActivity);
        buttonExit.setPosition((mainActivity.getCamera().getWidth()) / 2, mainActivity.getCamera().getHeight() / 2 - 25);
        registerTouchArea(buttonExit);

        attachChild(buttonGame);
        attachChild(buttonTutorial);
        attachChild(buttonOptions);
        attachChild(buttonExit);

        buttonGame.setListener(createButtonGameListener());
        buttonTutorial.setListener(createButtonTutorialListener());
        buttonOptions.setListener(createButtonOptionsListener());
        buttonExit.setListener(createButtonExitListener());
        setTouchAreaBindingOnActionDownEnabled(true);
    }

    private MenuButtonListener createButtonGameListener() {
        return new MenuButtonListener() {
            public void actionUp() {
                activity.getMainScene().showBoardScene();
            }
        };
    }

    private MenuButtonListener createButtonTutorialListener() {
        return new MenuButtonListener() {
            public void actionUp() {
            }
        };
    }

    private MenuButtonListener createButtonOptionsListener() {
        return new MenuButtonListener() {
            public void actionUp() {
            }
        };
    }

    private MenuButtonListener createButtonExitListener() {
        return new MenuButtonListener() {
            public void actionUp() {
                activity.finish();

            }
        };
    }
}
