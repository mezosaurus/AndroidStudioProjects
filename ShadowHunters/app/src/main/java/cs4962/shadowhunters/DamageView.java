package cs4962.shadowhunters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ethan on 12/17/2014.
 */
public class DamageView extends ViewGroup {

    public DamageView (Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = Math.max(widthSpec, getSuggestedMinimumWidth());
        int height = Math.max(heightSpec, getSuggestedMinimumHeight());

        int childState = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            if (child.getVisibility() == GONE) {
                continue;
            }
            else {
                //LayoutParams childLayoutParams = child.getLayoutParams();
                child.measure(MeasureSpec.AT_MOST | 40, MeasureSpec.AT_MOST | 40);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childWidthMax = getWidth() / 4;
        int childHeightMax = getHeight() / 2;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            childWidthMax = Math.max(childWidthMax, child.getMeasuredWidth());
            childHeightMax = Math.max(childHeightMax, child.getMeasuredHeight());
        }

        Rect layoutRect = new Rect();
        layoutRect.left = 0;
        layoutRect.top = 0;
        layoutRect.right = getWidth();
        layoutRect.bottom = getHeight();

        // Column count for child layout params calculations
        int columnCount = 0;
        // Row count for child layout params calculations
        int rowCount = 0;

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            DamageSquareView child = (DamageSquareView)getChildAt(childIndex);
            Rect childLayout = new Rect();

            int childCenterX = layoutRect.left + ((childWidthMax / 2) + (childWidthMax * columnCount));
            int childCenterY = layoutRect.top + ((childHeightMax / 2) + (childHeightMax * rowCount));

            childLayout.left = childCenterX - childWidthMax / 2;
            childLayout.top = childCenterY - childHeightMax / 2;
            childLayout.right = childCenterX + childWidthMax / 2;
            childLayout.bottom = childCenterY + childHeightMax / 2;
            child.layout(childLayout.left, childLayout.top, childLayout.right, childLayout.bottom);
            // Calculate new child layout params for next child
            // Increment left based on column count as we go down the row
            // Increment top based on row count as we go down the column
            /*if (child.getPosition() != "") {
                mCanvas.drawText(child.getPosition(), childCenterX, childCenterY, textPaint);
        }*/
            if (columnCount == 3) {
                columnCount = 0;
                rowCount++;
                // Reached end of row, reset childCenters
            }
            else {
                columnCount++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorder(canvas);
        drawSquare(canvas);
    }

    private void drawSquare(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);

        RectF rect = new RectF();
        rect.left = getPaddingLeft();
        rect.top = getPaddingTop();
        rect.right = getWidth() - getPaddingRight();
        rect.bottom = getHeight() - getPaddingBottom();

        canvas.drawRect(rect, paint);
    }

    private void drawBorder(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        RectF rect = new RectF();
        rect.left = 0;
        rect.top = 0;
        rect.right = getWidth();
        rect.bottom = getHeight();

        canvas.drawRect(rect, paint);
    }
}
