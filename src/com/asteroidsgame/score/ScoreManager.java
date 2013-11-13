package com.asteroidsgame.score;

import com.asteroidsgame.utils.GameResources;
import org.andengine.entity.text.Text;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * @author artem
 */
public class ScoreManager {

    private Text scoreText;
    private int score;

    public ScoreManager(BaseGameActivity activity) {
        scoreText = new Text(0, 0, GameResources.getInstance().getFontDefault(), "0123456789", activity.getVertexBufferObjectManager());
    }

    public Text getScoreText() {
        return scoreText;
    }

    public void addScore(int score) {
        this.score += score;
        setScore();
    }

    public void reset() {
        score = 0;
        setScore();
    }

    private void setScore() {
        scoreText.setText(String.valueOf(score));
    }

    public int getScore() {
        return score;
    }
    
    
}
