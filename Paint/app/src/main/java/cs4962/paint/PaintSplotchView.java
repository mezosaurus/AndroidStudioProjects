package cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Ethan on 9/16/2014.
 */
public class PaintSplotchView extends View {

    // paint splotch color
    private int paintColor;
    // content area
    private RectF contentRect;
    // splotch radius
    private float radius;
    // paint active boolean
    private boolean _active;

    public float getRadius() {
        return radius;
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        _active = active;
        invalidate();
    }

    public PaintSplotchView (Context context) {
        super(context);
        setMinimumWidth(100);
        setMinimumHeight(100);
    }

    public int getColor() {
        return paintColor;
    }

    public void setColor (int c) {
        paintColor = c;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(_active) {
            // draw the highlight circle if this splotch is selected
            drawHighlightSplotch(canvas);
        }

        drawSplotch(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = getSuggestedMinimumWidth();
        int height = getSuggestedMinimumHeight();

        if (widthMode == MeasureSpec.AT_MOST) {
            width = widthSpec;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = heightSpec;
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSpec;
            height = width;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSpec;
            width = height;
        }
        if (width > height && widthMode != MeasureSpec.EXACTLY) {
            width = height;
        }
        if (height > width && heightMode != MeasureSpec.EXACTLY) {
            height = width;
        }

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, width < getSuggestedMinimumWidth() ? MEASURED_STATE_TOO_SMALL : 0),
                                resolveSizeAndState(height, heightMeasureSpec, height < getSuggestedMinimumHeight() ? MEASURED_STATE_TOO_SMALL : 0));
    }

    private void drawSplotch (Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(paintColor);
        Path path = new Path();

        contentRect = new RectF();
        contentRect.left = getPaddingLeft();
        contentRect.top = getPaddingTop();
        contentRect.right = getWidth() - getPaddingRight();
        contentRect.bottom = getHeight() - getPaddingBottom();

        PointF center = new PointF(contentRect.centerX(), contentRect.centerY());
        radius = Math.min(contentRect.width() * 0.5f, contentRect.height() * 0.5f);

        int pointCount = 50;
        for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
            PointF point = new PointF();
            point.x = center.x + radius * (float)Math.cos(((double)pointIndex / (double)pointCount) * 2.0f * Math.PI);
            point.y = center.y + + radius * (float)Math.sin(((double) pointIndex / (double) pointCount) * 2.0f * Math.PI);

            if (pointIndex == 0) {
                path.moveTo(point.x, point.y);
            }
            else {
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
    }

    private void drawHighlightSplotch (Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        Path path = new Path();

        contentRect = new RectF();
        contentRect.left = 0;
        contentRect.top = 0;
        contentRect.right = getWidth();
        contentRect.bottom = getHeight();

        PointF center = new PointF(contentRect.centerX(), contentRect.centerY());
        radius = Math.min(contentRect.width() * 0.5f, contentRect.height() * 0.5f);

        int pointCount = 50;
        for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
            PointF point = new PointF();
            point.x = center.x + radius * (float)Math.cos(((double)pointIndex / (double)pointCount) * 2.0f * Math.PI);
            point.y = center.y + + radius * (float)Math.sin(((double) pointIndex / (double) pointCount) * 2.0f * Math.PI);

            if (pointIndex == 0) {
                path.moveTo(point.x, point.y);
            }
            else {
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
    }
}
