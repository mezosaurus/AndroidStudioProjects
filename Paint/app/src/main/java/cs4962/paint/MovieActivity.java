package cs4962.paint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.util.ArrayList;

/**
 * Created by Ethan on 10/4/2014.
 */
public class MovieActivity extends Activity {

    private boolean isPlaying = false;
    private SeekBar scrubber;
    private Button playPause;
    private PaintView movieView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final RelativeLayout movieLayout = new RelativeLayout(this);
        movieView = new PaintView(this);
        movieView.setMovieMode(true);
        movieView.setId(15);
        scrubber = new SeekBar(this);
        scrubber.setMax(100);
        scrubber.setId(16);
        playPause = new Button(this);
        playPause.setText("Play");
        playPause.setId(18);

        if (savedInstanceState != null) {
            isPlaying = savedInstanceState.getBoolean("isPlaying");
            scrubber.setProgress(savedInstanceState.getInt("progress"));
            ArrayList<String> events = savedInstanceState.getStringArrayList("events");
            ArrayList<Integer> colors = savedInstanceState.getIntegerArrayList("paintColors");
            ArrayList<PaintPoint> points = (ArrayList<PaintPoint>)savedInstanceState.getSerializable("points");
            int activeColor = savedInstanceState.getInt("activeColor");
            movieView.setPaintColor(activeColor);
            movieView.setProgress(scrubber.getProgress());
            if (points != null)
                movieView.setPoints(points);
            if (colors != null)
                movieView.setColors(colors);
            if (events != null)
                movieView.setEvents(events);
            if (isPlaying) {
                play();
            }
            else {
                movieView.reDrawFromScrubber(scrubber.getProgress());
                //movieView.invalidate();
            }
        }
        Intent movieIntent = getIntent();
        if (movieIntent != null) {
            int activeColor = movieIntent.getIntExtra("activeColor", 0);
            ArrayList<String> events = movieIntent.getStringArrayListExtra("events");
            ArrayList<Integer> colors = movieIntent.getIntegerArrayListExtra("colors");
            ArrayList<PaintPoint> points = (ArrayList<PaintPoint>) movieIntent.getSerializableExtra("points");
            movieView.setPaintColor(activeColor);
            if (events != null)
                movieView.setEvents(events);
            if (colors != null)
                movieView.setColors(colors);
            if (points != null)
                movieView.setPoints(points);
        }

        scrubber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                movieView.reDrawFromScrubber(progress);
            }

            @Override
            public void onStartTrackingTouch (SeekBar bar) {
                pause();
            }

            @Override
            public void onStopTrackingTouch (SeekBar bar) {
            }
        });

        final Button createBtn = new Button(this);
        createBtn.setText("Create");
        createBtn.setId(17);
        createBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create button clicked
                Intent createIntent = new Intent(MovieActivity.this, PaintActivity.class);
                createIntent.putExtra("activeColor", movieView.getPaintColor());
                startActivity(createIntent);
                finish();
            }
        });


        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pause();
                }
                else {
                    play();
                }
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        RelativeLayout.LayoutParams movieParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams createBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams playPauseParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams scrubberParams = new RelativeLayout.LayoutParams(size.x / 2, RelativeLayout.LayoutParams.WRAP_CONTENT);

        movieParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, movieView.getId());
        movieParams.addRule(RelativeLayout.ABOVE, createBtn.getId());
        movieParams.addRule(RelativeLayout.ABOVE, playPause.getId());
        movieParams.addRule(RelativeLayout.ABOVE, scrubber.getId());

        createBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, createBtn.getId());
        createBtnParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, createBtn.getId());

        playPauseParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, playPause.getId());
        playPauseParams.addRule(RelativeLayout.RIGHT_OF, createBtn.getId());
        playPauseParams.addRule(RelativeLayout.LEFT_OF, scrubber.getId());

        scrubberParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, scrubber.getId());
        scrubberParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, scrubber.getId());

        movieLayout.addView(movieView, movieParams);
        movieLayout.addView(createBtn, createBtnParams);
        movieLayout.addView(playPause, playPauseParams);
        movieLayout.addView(scrubber, scrubberParams);
        setContentView(movieLayout);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // hardware back button hit
            Intent createIntent = new Intent(MovieActivity.this, PaintActivity.class);
            createIntent.putExtra("activeColor", movieView.getPaintColor());
            startActivity(createIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void pause() {
        isPlaying = false;
        playPause.setText("Play");
    }

    private void play() {
        isPlaying = true;
        playPause.setText("Pause");
        if (scrubber.getProgress() == scrubber.getMax()) {
            scrubber.setProgress(0);
        }
        updateProgressBar();
    }

    private Runnable updateScrubber = new Runnable() {
        public void run() {
            if (isPlaying) {
                int currentProgress = scrubber.getProgress();
                movieView.reDrawFromScrubber(currentProgress);
                int max = scrubber.getMax();
                int interval = 1;
                int newProgress = currentProgress + interval;
                if (newProgress <= max) {
                    scrubber.setProgress(scrubber.getProgress() + interval);
                    handler.postDelayed(this, 100);
                }
                else {
                    pause();
                }
            }
        }
    };

    private void updateProgressBar() {
        handler.postDelayed(updateScrubber, 100);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("points", movieView.getPoints());
        outState.putIntegerArrayList("paintColors", movieView.getColors());
        outState.putStringArrayList("events", movieView.getEvents());
        outState.putInt("progress", scrubber.getProgress());
        outState.putInt("activeColor", movieView.getPaintColor());
        outState.putBoolean("isPlaying", isPlaying);
    }
}
