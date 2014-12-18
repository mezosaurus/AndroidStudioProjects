package cs4962.shadowhunters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Ethan on 12/17/2014.
 */
public class DamageSquareView extends View {
    private int mPlayerColor;
    private int mDamageValue;

    public DamageSquareView (Context context) {
        super(context);
    }

    public int getPlayerColor() {
        return mPlayerColor;
    }

    public void setPlayerColor(int mPlayerColor) {
        this.mPlayerColor = mPlayerColor;
    }

    public int getDamageValue() {
        return mDamageValue;
    }

    public void setDamageValue(int mDamageIndicator) {
        this.mDamageValue = mDamageIndicator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPlayerCircle(canvas);
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

    private void drawPlayerCircle (Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mPlayerColor);
        Path path = new Path();

        RectF contentRect = new RectF();
        contentRect.left = getPaddingLeft();
        contentRect.top = getPaddingTop();
        contentRect.right = getWidth() - getPaddingRight();
        contentRect.bottom = getHeight() - getPaddingBottom();

        PointF center = new PointF(contentRect.centerX(), contentRect.centerY());
        float radius = Math.min(contentRect.width() * 0.5f, contentRect.height() * 0.5f);

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
