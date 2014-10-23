package cs4962.paint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by Ethan on 10/2/2014.
 */
public class PaletteActivity extends Activity {
    //private ArrayList<Integer> paletteColors;
    private int activeColor;
    private PaintPaletteView paintPaletteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final RelativeLayout paletteLayout = new RelativeLayout(this);
        final Button resetBtn = new Button(this);
        paintPaletteView = new PaintPaletteView(this);
        boolean savedColorsExist = false;
        File paletteFile = this.getFileStreamPath("palette.dat");
        if (paletteFile.exists()) {
            savedColorsExist = true;
        }

        if (!savedColorsExist) {
            ArrayList<Integer> paletteColors = new ArrayList<Integer>();
            paletteColors.add(Color.BLACK);
            paletteColors.add(Color.BLUE);
            paletteColors.add(Color.CYAN);
            paletteColors.add(Color.GRAY);
            paletteColors.add(Color.GREEN);
            paletteColors.add(Color.LTGRAY);
            paletteColors.add(Color.MAGENTA);
            paletteColors.add(Color.RED);
            paletteColors.add(Color.YELLOW);
            paintPaletteView.setPaletteColors(paletteColors);
            for (int colorIndex = 0; colorIndex < paletteColors.size(); colorIndex++) {
                PaintSplotchView splotchView = new PaintSplotchView(this);
                splotchView.setPadding(10, 10, 10, 10);
                splotchView.setColor(paletteColors.get(colorIndex));
                paintPaletteView.addView(splotchView);
            }
        }
        paintPaletteView.setId(9);
        resetBtn.setId(30);
        resetBtn.setText("Reset");
        resetBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // reset button clicked
                deleteFile("palette.dat");
                paintPaletteView.removeAllViews();
                ArrayList<Integer> paletteColors = new ArrayList<Integer>();
                paletteColors.add(Color.BLACK);
                paletteColors.add(Color.BLUE);
                paletteColors.add(Color.CYAN);
                paletteColors.add(Color.GRAY);
                paletteColors.add(Color.GREEN);
                paletteColors.add(Color.LTGRAY);
                paletteColors.add(Color.MAGENTA);
                paletteColors.add(Color.RED);
                paletteColors.add(Color.YELLOW);
                paintPaletteView.setPaletteColors(paletteColors);
                for (int colorIndex = 0; colorIndex < paletteColors.size(); colorIndex++) {
                    PaintSplotchView splotchView = new PaintSplotchView(PaletteActivity.this);
                    splotchView.setPadding(10, 10, 10, 10);
                    splotchView.setColor(paletteColors.get(colorIndex));
                    paintPaletteView.addView(splotchView);
                }
                paintPaletteView.setActiveColor(Color.BLACK);
            }
        });

        Intent paletteIntent = getIntent();
        activeColor = paletteIntent.getIntExtra("activeColor", 0);
        if (activeColor != 0) {
            paintPaletteView.setActiveColor(activeColor);
        }

        paintPaletteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        paintPaletteView.setOnSplotchTouchListener(new PaintPaletteView.OnSplotchTouchListener() {
            @Override
            public void onSplotchTouch(int color, MotionEvent event) {
                // Get X and Y coords of touch event
                float touchX = event.getX();
                float touchY = event.getY();

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        paintPaletteView.setActiveColor(color);
                        // Set paint color for drawing
                        //paintView.setPaintColor(color);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        PaintSplotchView activeSplotch = paintPaletteView.getChildByColor(paintPaletteView.getActiveColor());
                        if (activeSplotch != null) {
                            activeSplotch.setX(touchX - (activeSplotch.getRadius() + activeSplotch.getPaddingLeft()));
                            activeSplotch.setY(touchY - (activeSplotch.getRadius() + activeSplotch.getPaddingTop()));
                            activeSplotch.invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        PaintSplotchView activeSplotchUp = paintPaletteView.getChildByColor(paintPaletteView.getActiveColor());
                        if (activeSplotchUp != null) {
                            float x = activeSplotchUp.getX() + (activeSplotchUp.getWidth() / 2);
                            float y = activeSplotchUp.getY() + (activeSplotchUp.getHeight() / 2);

                            // check for splotch out of palette
                            if (paintPaletteView.checkSplotchOutOfPalette(x, y)) {
                                if (paintPaletteView.getColors().length > 2) {
                                    paintPaletteView.removeColor(color);
                                    //paintView.setPaintColor(((PaintSplotchView)paintPaletteView.getChildAt(0)).getColor());
                                    break;
                                }
                            }

                            // Check to see if splotch was dragged over another splotch for color mixing
                            int colorToMix = paintPaletteView.splotchesIntersect(touchX, touchY);
                            if (colorToMix != 0 && colorToMix != color) {
                                int newColor = paintPaletteView.mixColor(colorToMix, color);
                                paintPaletteView.addColor(newColor);
                            }
                            activeSplotchUp.setX(activeSplotchUp.getLeft());
                            activeSplotchUp.setY(activeSplotchUp.getTop());
                            activeSplotchUp.invalidate();
                        }
                        break;
                }
            }
        });

        RelativeLayout.LayoutParams paletteParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams resetBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        paletteParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, paintPaletteView.getId());

        resetBtnParams.addRule(RelativeLayout.CENTER_IN_PARENT, resetBtn.getId());

        paletteLayout.addView(paintPaletteView, paletteParams);
        paletteLayout.addView(resetBtn, resetBtnParams);
        setContentView(paletteLayout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // hardware back button hit
            Intent intent = new Intent(PaletteActivity.this, PaintActivity.class);
            intent.putExtra("activeColor", paintPaletteView.getActiveColor());
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ArrayList<Integer> paletteColors = paintPaletteView.getPaletteColors();
        Type colorType = new TypeToken<ArrayList<Integer>>() {}.getType();

        Gson gson = new Gson();
        String paletteString = gson.toJson(paletteColors, colorType);
        String activeColorString = gson.toJson(paintPaletteView.getActiveColor());
        try {
            FileOutputStream os = openFileOutput("palette.dat", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(paletteString);
            output.writeObject(activeColorString);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String colorsObject = "";
        String activeColorObject = "";
        try {
            FileInputStream is = openFileInput("palette.dat");
            ObjectInputStream input = new ObjectInputStream(is);
            colorsObject = (String)input.readObject();
            activeColorObject = (String)input.readObject();
            input.close();

            Gson gson = new Gson();
            Type colorType = new TypeToken<ArrayList<Integer>>() {}.getType();
            ArrayList<Integer> colors = gson.fromJson(colorsObject, colorType);
            int color = gson.fromJson(activeColorObject, int.class);

            if (colors != null && colors.size() > 0) {
                if (paintPaletteView.getPaletteColors().size() == 0) {
                    paintPaletteView.setPaletteColors(colors);
                    for (int colorIndex = 0; colorIndex < colors.size(); colorIndex++) {
                        PaintSplotchView splotchView = new PaintSplotchView(this);
                        splotchView.setPadding(10, 10, 10, 10);
                        splotchView.setColor(colors.get(colorIndex));
                        paintPaletteView.addView(splotchView);
                    }
                }
            }
            paintPaletteView.setActiveColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
