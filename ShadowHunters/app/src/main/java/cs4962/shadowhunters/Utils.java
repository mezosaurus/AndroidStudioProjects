package cs4962.shadowhunters;

import android.app.Activity;
import android.graphics.Color;
import android.view.inputmethod.InputMethodManager;

import java.util.Random;

/**
 * Created by Ethan on 12/9/2014.
 */
public class Utils {
    public static int mixColor(int color1, int color2) {
        int r1 = Color.red(color1);
        int g1 = Color.green(color1);
        int b1 = Color.blue(color1);

        int r2 = Color.red(color2);
        int g2 = Color.green(color2);
        int b2 = Color.blue(color2);

        int r3 = (r1 + r2) / 2;
        int g3 = (g1 + g2) / 2;
        int b3 = (b1 + b2) / 2;

        return Color.rgb(r3, g3, b3);
    }

    public static int rollDie(int numSides) {
        Random rand = new Random();

        return rand.nextInt(numSides) + 1;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
