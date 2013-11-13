package com.asteroidsgame.controls;

import com.asteroidsgame.activities.MainActivity;
import com.asteroidsgame.utils.GameResources;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

/**
 * @author artem
 */
public class MenuButton extends ButtonSprite {

    private MenuButtonListener listener;

    public MenuButton(int resourceId, MainActivity activity) {
        super(0, 0, GameResources.getInstance().getButtonRegion(), activity.getVertexBufferObjectManager());
        Text t = new Text(100, 40, GameResources.getInstance().getFont(), activity.getResources().getString(resourceId), activity.getVertexBufferObjectManager());
        attachChild(t);
    }

    public void setListener(MenuButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                this.setCurrentTileIndex(1);
                break;
            case TouchEvent.ACTION_UP:
                this.setCurrentTileIndex(0);  
                listener.actionUp();
        }
        return true;
    }
}
