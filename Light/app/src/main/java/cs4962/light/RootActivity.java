package cs4962.light;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;


public class RootActivity extends Activity {

    ImageView lightImageView = null;
    Switch lightSwitch  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.BLACK);

        lightImageView = new ImageView(this);

        lightImageView.setImageResource(R.drawable.off);
        rootLayout.addView(lightImageView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 70));

        lightSwitch = new Switch(this);
        LinearLayout.LayoutParams lightSwitchLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 30);
        lightSwitchLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        rootLayout.addView(lightSwitch, lightSwitchLayoutParams);

        lightSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSwitch = (Switch)view;
                lightImageView.setImageResource(lightSwitch.isChecked() ? R.drawable.on : R.drawable.off);
            }
        });

        setContentView(rootLayout);
    }
}
