package cs4962.paint;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ethan on 10/4/2014.
 */
public class PaintPoint implements Serializable {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float newX) {
        x = newX;
    }

    public float getY() {
        return y;
    }

    public void setY(float newY) {
        y = newY;
    }

    public PaintPoint (float x, float y) {
        this.x = x;
        this.y = y;
    }
}
