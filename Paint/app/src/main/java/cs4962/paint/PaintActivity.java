package cs4962.paint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class PaintActivity extends Activity {

    private PaintView paintView;
    private int activeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final RelativeLayout paintLayout = new RelativeLayout(this);
        paintView = new PaintView(this);
        paintView.setMovieMode(false);

        Intent paintIntent = getIntent();
        activeColor = paintIntent.getIntExtra("activeColor", 0);
        if (activeColor == 0) {
            activeColor = Color.BLACK;
        }
        paintView.setPaintColor(activeColor);

        final Button watchBtn = new Button(this);
        watchBtn.setText("Watch");

        watchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // watch button clicked
                Intent movieIntent = new Intent(PaintActivity.this, MovieActivity.class);
                movieIntent.putExtra("points", paintView.getPoints());
                movieIntent.putExtra("colors", paintView.getColors());
                movieIntent.putExtra("events", paintView.getEvents());
                movieIntent.putExtra("activeColor", paintView.getPaintColor());
                startActivity(movieIntent);
                finish();
            }
        });

        final Button clearBtn = new Button(this);
        clearBtn.setText("Clear");

        clearBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                // clear button clicked
                paintView.invalidate();
                paintView.setPaths(new ArrayList<Path>());
                paintView.setColors(new ArrayList<Integer>());
                paintView.setPoints(new ArrayList<PaintPoint>());
                paintView.setEvents(new ArrayList<String>());
                paintView.invalidate();
                deleteFile("paint.dat");
            }
        });

        final Button paletteBtn = new Button(this);
        paletteBtn.setText("Palette");
        paletteBtn.setTextColor(Color.WHITE);
        paletteBtn.setBackgroundColor(paintView.getPaintColor());
        final Intent paletteIntent = new Intent(this, PaletteActivity.class);
        paletteIntent.putExtra("activeColor", paintView.getPaintColor());
        paletteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // palette button clicked
                startActivity(paletteIntent);
                finish();
            }
        });
        paintView.setId(8);
        watchBtn.setId(20);
        clearBtn.setId(21);
        paletteBtn.setId(22);

        RelativeLayout.LayoutParams paintParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams watchBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams clearBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams paletteBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        paintParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, paintView.getId());
        paintParams.addRule(RelativeLayout.ABOVE, watchBtn.getId());
        paintParams.addRule(RelativeLayout.ABOVE, paletteBtn.getId());
        paintParams.addRule(RelativeLayout.ABOVE, clearBtn.getId());

        watchBtnParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, clearBtn.getId());
        watchBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, watchBtn.getId());

        clearBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, clearBtn.getId());
        clearBtnParams.addRule(RelativeLayout.LEFT_OF, paletteBtn.getId());
        clearBtnParams.addRule(RelativeLayout.RIGHT_OF, watchBtn.getId());

        paletteBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, paletteBtn.getId());
        paletteBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, paletteBtn.getId());

        paintLayout.addView(paintView, paintParams);
        paintLayout.addView(watchBtn, watchBtnParams);
        paintLayout.addView(clearBtn, clearBtnParams);
        paintLayout.addView(paletteBtn, paletteBtnParams);
        setContentView(paintLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ArrayList<PaintPoint> points = paintView.getPoints();
        Type pointType = new TypeToken<ArrayList<PaintPoint>>() {}.getType();
        ArrayList<Integer> colors = paintView.getColors();
        Type colorType = new TypeToken<ArrayList<Integer>>() {}.getType();
        ArrayList<String> events = paintView.getEvents();
        Type eventType = new TypeToken<ArrayList<String>>() {}.getType();

        Gson gson = new Gson();
        String pointString = gson.toJson(points, pointType);
        String colorString = gson.toJson(colors, colorType);
        String eventString = gson.toJson(events, eventType);
        try {
            FileOutputStream os = openFileOutput("paint.dat", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(pointString);
            output.writeObject(colorString);
            output.writeObject(eventString);

            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String pointObject = "";
        String colorObject = "";
        String eventObject = "";
        try {
            FileInputStream is = openFileInput("paint.dat");
            ObjectInputStream input = new ObjectInputStream(is);

            pointObject = (String)input.readObject();
            colorObject = (String)input.readObject();
            eventObject = (String)input.readObject();

            input.close();

            Gson gson = new Gson();
            Type pointType = new TypeToken<ArrayList<PaintPoint>>() {}.getType();
            Type colorType = new TypeToken<ArrayList<Integer>>() {}.getType();
            Type eventType = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<PaintPoint> points = gson.fromJson(pointObject, pointType);
            ArrayList<String> events = gson.fromJson(eventObject, eventType);
            ArrayList<Integer> colors = gson.fromJson(colorObject, colorType);

            if (events != null)
                paintView.setEvents(events);
            if (colors != null)
                paintView.setColors(colors);
            if (points != null)
                paintView.setPoints(points);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
