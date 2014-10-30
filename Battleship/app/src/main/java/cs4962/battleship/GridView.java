package cs4962.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ethan on 10/28/2014.
 */
public class GridView extends ViewGroup {

    private Canvas mCanvas;
    public GridView (Context context) {
        super(context);
        setSaveEnabled(true);
    }

    /*OnSquareTouchListener squareTouchListener = null;

    public interface OnSquareTouchListener {
        public void onSquareTouch(MotionEvent event);
    }

    public void setOnSquareTouchListener(OnSquareTouchListener listener) {
        squareTouchListener = listener;
    }*/

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
                child.measure(MeasureSpec.AT_MOST | width/11, MeasureSpec.AT_MOST | height/11);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childWidthMax = getWidth() / 11;
        int childHeightMax = getHeight() / 11;
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
            GridSquareView child = (GridSquareView)getChildAt(childIndex);
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
            if (columnCount == 10) {
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
    protected void dispatchDraw(Canvas canvas) {
        /*Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        RectF contentRect = new RectF();
        contentRect.left = 0;
        contentRect.top = 0;
        contentRect.right = getWidth();
        contentRect.bottom = getHeight();

        canvas.drawRect(contentRect, paint);*/
        mCanvas = canvas;
        super.dispatchDraw(canvas);
    }

    /*@Override
    public boolean onTouchEvent (MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        boolean touched = touchInsideSquare(touchX, touchY);
        if (touched) {
            if (squareTouchListener != null) {
                squareTouchListener.onSquareTouch(event);
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean touchInsideSquare(float x, float y) {
        boolean retVal = false;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            GridSquareView child = (GridSquareView)getChildAt(childIndex);
            // A = topLeftX, topLeftY
            float topLeftX = child.getLeft();
            float topLeftY = child.getTop();
            // B = bottomLeftX, bottomLeftY
            float bottomLeftX = child.getLeft();
            float bottomLeftY = child.getHeight();
            // C == bottomRightX, bottomRightY
            float bottomRightX = child.getRight();
            float bottomRightY = child.getWidth();
            // D = topRightX, topRightY
            float topRightX = child.getRight();
            float topRightY = child.getTop();

            // 1/2( (x3y2 - x2y3) - (x3y1 - x1y3) + (x2y1 - x1y2) )
            // Calculate triangle ABP
            float areaABP = (1/2)*((x*bottomLeftY - bottomLeftX*y) - (x*topLeftY - topLeftX*y) + (bottomLeftX*topLeftY - topLeftX*bottomLeftY));
            // Calculate triangle BCP
            float areaBCP = (1/2)*((x*bottomLeftY - bottomLeftX*y) - (x*topLeftY - topLeftX*y) + (bottomLeftX*topLeftY - topLeftX*bottomLeftY));
            // Calculate triangle CDP
            float areaCDP = (1/2)*((x*bottomLeftY - bottomLeftX*y) - (x*topLeftY - topLeftX*y) + (bottomLeftX*topLeftY - topLeftX*bottomLeftY));
            // Calculate triangle DAP
            float areaDAP = (1/2)*((x*bottomLeftY - bottomLeftX*y) - (x*topLeftY - topLeftX*y) + (bottomLeftX*topLeftY - topLeftX*bottomLeftY));


        }
        return retVal;
    }*/
}
