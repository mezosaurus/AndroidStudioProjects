package cs4962.battleship;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


public class GridActivity extends Activity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LinearLayout gridLayout = new LinearLayout(this);
        gridView = new GridView(this);
        gridView.setId(10);

        gridLayout.addView(gridView);
        setContentView(gridLayout);
    }
}
