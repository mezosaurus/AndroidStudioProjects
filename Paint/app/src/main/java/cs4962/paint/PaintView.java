package cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Ethan on 9/15/2014.
 */
public class PaintView extends View {
    ArrayList<Path> paths = new ArrayList<Path>();
    private Path path;
    private int paintColor = 0;
    private int oldPaintColor = 0;
    private int progress = 0;

    // saved lists for redrawing
    private ArrayList<PaintPoint> points = new ArrayList<PaintPoint>();
    private ArrayList<Integer> colorList = new ArrayList<Integer>();
    private ArrayList<String> eventList = new ArrayList<String>();
    // boolean for if movie mode is available
    private boolean movieMode;

    public PaintView(Context context) {
        super(context);
        setSaveEnabled(true);
    }

    public void setProgress (int scrubberProgress) {
        progress = scrubberProgress;
    }

    public void setMovieMode(boolean mode) {
        movieMode = mode;
    }

    public void setPaths(ArrayList<Path> pathList) {
        paths = pathList;
    }

    public ArrayList<PaintPoint> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<PaintPoint> pointList) {
        points = pointList;
    }

    public ArrayList<Integer> getColors() { return colorList; }

    public void setColors(ArrayList<Integer> colors) {
        colorList = colors;
    }

    public ArrayList<String> getEvents() { return eventList; }

    public void setEvents(ArrayList<String> events) { eventList = events; }

    public int getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(int color) {
        paintColor = color;
    }

    @Override
    public void onWindowFocusChanged (boolean hasWindowFocus) {
        if (hasWindowFocus && !movieMode) {
            oldPaintColor = paintColor;
            reDraw();
        }
        else {
            oldPaintColor = paintColor;
            reDrawFromScrubber(progress);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < paths.size(); i++) {
            Paint paint = new Paint();
            paint.setStrokeWidth(20);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(colorList.get(i));
            canvas.drawPath(paths.get(i), paint);
        }
        paintColor = oldPaintColor;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (movieMode) {
            return true;
        }

        float touchX = event.getX();
        float touchY = event.getY();
        float scaledX = touchX / getMeasuredWidth();
        float scaledY = touchY / getMeasuredHeight();
        points.add(new PaintPoint(scaledX, scaledY));
        colorList.add(paintColor);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                eventList.add("DOWN");
                path = new Path();
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                eventList.add("MOVE");
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                eventList.add("UP");
                break;
        }
        paths.add(path);
        invalidate();
        return true;
    }

    public void reDraw() {
        paths = new ArrayList<Path>();
        for (int i = 0; i < points.size(); i++) {
            String event = eventList.get(i);
            int color = colorList.get(i);
            setPaintColor(color);
            PaintPoint point = points.get(i);
            float x = point.getX() * getMeasuredWidth();
            float y = point.getY() * getMeasuredHeight();
            if (event.equals("DOWN")) {
                path = new Path();
                path.moveTo(x, y);
            }
            else if (event.equals("MOVE")) {
                path.lineTo(x, y);
            }
            else if (event.equals("UP")) {
            }
            paths.add(path);
            invalidate();
        }
    }

    public void reDrawFromScrubber (int progress) {
        paths = new ArrayList<Path>();
        double factor = (double) progress / 100.0;
        int totalPoints = points.size();
        double pointsToDraw = totalPoints * factor;
        for (int i = 0; i < pointsToDraw; i++) {
            String event = eventList.get(i);
            int color = colorList.get(i);
            setPaintColor(color);
            PaintPoint point = points.get(i);
            float x = point.getX() * getMeasuredWidth();
            float y = point.getY() * getMeasuredHeight();
            if (event.equals("DOWN")) {
                path = new Path();
                path.moveTo(x, y);
            } else if (event.equals("MOVE")) {
                path.lineTo(x, y);
            } else if (event.equals("UP")) {
            }
            paths.add(path);
            invalidate();
        }
    }
}
