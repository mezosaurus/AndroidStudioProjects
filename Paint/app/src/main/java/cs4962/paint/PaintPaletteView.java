package cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Ethan on 9/16/2014.
 */
public class PaintPaletteView extends ViewGroup {

    private Rect layoutRect;
    private ArrayList<Integer> paletteColors = new ArrayList<Integer>();
    // splotch touch listener
    OnSplotchTouchListener splotchTouchListener = null;

    public interface OnSplotchTouchListener {
        public void onSplotchTouch(int color, MotionEvent event);
    }

    public PaintPaletteView(Context context) {
        super(context);
        setSaveEnabled(true);
        setPadding(10, 10, 10, 10);
        /*if (paletteColors.size() == 0) {
            paletteColors.add(Color.BLACK);
            paletteColors.add(Color.BLUE);
            paletteColors.add(Color.CYAN);
            paletteColors.add(Color.GRAY);
            paletteColors.add(Color.GREEN);
            paletteColors.add(Color.LTGRAY);
            paletteColors.add(Color.MAGENTA);
            paletteColors.add(Color.RED);
            paletteColors.add(Color.YELLOW);
        }
        for (int colorIndex = 0; colorIndex < paletteColors.size(); colorIndex++) {
            PaintSplotchView splotchView = new PaintSplotchView(context);
            splotchView.setPadding(10, 10, 10, 10);
            splotchView.setColor(paletteColors.get(colorIndex));
            addView(splotchView);
        }*/
    }

    public ArrayList<Integer> getPaletteColors() {
        return paletteColors;
    }

    public void setPaletteColors(ArrayList<Integer> colors) {
        paletteColors = colors;
    }

    public void setOnSplotchTouchListener(OnSplotchTouchListener listener) {
        splotchTouchListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = Math.max(widthSpec, getSuggestedMinimumWidth());
        int height = Math.max(heightSpec, getSuggestedMinimumHeight());

        // TODO: make sure we are at least as high as 2 child views!

        int childState = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            if (child.getVisibility() == GONE) {
                continue;
            }
            else {
                //LayoutParams childLayoutParams = child.getLayoutParams();
                child.measure(MeasureSpec.AT_MOST | 100, MeasureSpec.AT_MOST | 100);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                                resolveSizeAndState(height, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childWidthMax = 0;
        int childHeightMax = 0;
        int childrenNotGone = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            if (child.getVisibility() == GONE) {
                continue;
            }
            childWidthMax = Math.max(childWidthMax, child.getMeasuredWidth());
            childHeightMax = Math.max(childHeightMax, child.getMeasuredHeight());
            childrenNotGone++;
        }

        layoutRect = new Rect();
        layoutRect.left = getPaddingLeft() + childWidthMax / 2;
        layoutRect.top = getPaddingTop() + childHeightMax / 2;
        layoutRect.right = getWidth() - getPaddingRight() - childWidthMax / 2;
        layoutRect.bottom = getHeight() - getPaddingBottom() - childHeightMax / 2;

        int childAngleIndex = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            double angle = (double)childAngleIndex / (double)childrenNotGone * 2.0 * Math.PI;
            int childCenterX = (int)(layoutRect.centerX() + (double)layoutRect.width() * 0.5 *  Math.cos(angle));
            int childCenterY = (int)(layoutRect.centerY() + (double)layoutRect.height() * 0.5 *  Math.sin(angle));

            View child = getChildAt(childIndex);
            Rect childLayout = new Rect();
            if (child.getVisibility() == GONE) {
                childLayout.left = 0;
                childLayout.top = 0;
                childLayout.right = 0;
                childLayout.bottom = 0;
            }
            else {
                childAngleIndex++;
                childLayout.left = childCenterX - childWidthMax / 2;
                childLayout.top = childCenterY - childHeightMax / 2;
                childLayout.right = childCenterX + childWidthMax / 2;
                childLayout.bottom = childCenterY + childHeightMax / 2;
                child.layout(childLayout.left, childLayout.top, childLayout.right, childLayout.bottom);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFFdab583);
        Path path = new Path();

        RectF contentRect = new RectF();
        contentRect.left = getPaddingLeft();
        contentRect.top = getPaddingTop();
        contentRect.right = getWidth() - getPaddingRight();
        contentRect.bottom = getHeight() - getPaddingBottom();

        canvas.drawOval(contentRect, paint);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        int touchedSplotchColor = touchInsideSplotch(touchX, touchY);
        if (touchedSplotchColor != 0) {
            if (splotchTouchListener != null) {
                splotchTouchListener.onSplotchTouch(touchedSplotchColor, event);
            }
        }
        return super.onTouchEvent(event);
    }

    public PaintSplotchView getChildByColor(int color) {
        PaintSplotchView retVal = null;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            PaintSplotchView child = (PaintSplotchView)getChildAt(childIndex);
            if (child.getColor() == color) {
                retVal = child;
            }
        }
        return retVal;
    }

    public boolean checkSplotchOutOfPalette (float x, float y) {
        boolean retVal = false;

        float paletteWidth = getMeasuredWidth() - getPaddingLeft();
        float paletteHeight = getMeasuredHeight() - getPaddingTop();

        float radiusX = paletteWidth / 2;
        float radiusY = paletteHeight / 2;

        float paletteCenterX = getX() + radiusX;
        float paletteCenterY = getY() + radiusY;

        float firstComponent = ((x - paletteCenterX) * (x - paletteCenterX)) / (radiusX * radiusX);
        float secondComponent = ((y - paletteCenterY) * (y - paletteCenterY)) / (radiusY * radiusY);
        float expression = firstComponent + secondComponent;

        if (expression > 1) {
            // splotch outside of palette
            retVal = true;
        }

        return retVal;
    }

    public int getActiveColor() {
        int color = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            PaintSplotchView child = (PaintSplotchView)getChildAt(childIndex);
            if (child.isActive()) {
                color = child.getColor();
            }
        }
        return color;
    }

    public void setActiveColor(int activeColor) {
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            PaintSplotchView child = (PaintSplotchView) getChildAt(childIndex);
            if (child.getColor() == activeColor) {
                child.setActive(true);
            }
            else {
                child.setActive(false);
            }
        }
    }

    private int touchInsideSplotch(float x, float y) {
        int retVal = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            PaintSplotchView child = (PaintSplotchView)getChildAt(childIndex);
            float radius = child.getRadius() + child.getPaddingLeft();

            float circleCenterY = child.getY() + (child.getHeight() / 2);
            float circleCenterX = child.getX() + (child.getWidth() / 2);

            float distance = (float)Math.sqrt(((circleCenterX - x) * (circleCenterX - x)) +
                    ((circleCenterY - y) * (circleCenterY - y)));
            if(distance < radius) {
                // touch in the circle
                retVal = child.getColor();
            }
        }
        return retVal;
    }

    public int splotchesIntersect (float x, float y) {
        int color = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            PaintSplotchView child = (PaintSplotchView)getChildAt(childIndex);
            float radius = child.getRadius() + child.getPaddingLeft();
            float childX = child.getX() + (child.getWidth() / 2);
            float childY = child.getY() + (child.getHeight() / 2);

            float distance = (float)Math.sqrt(((childX - x) * (childX - x)) +
                    ((childY - y) * (childY - y)));

            if (distance < radius) {
                // touching over another splotch
                color = child.getColor();
                break;
            }
        }
        return color;
    }

    public void addColor(int newColor) {
        for (int color : getColors()) {
            if (newColor == color) {
                return;
            }
        }
        paletteColors.add(newColor);
        PaintSplotchView newSplotch = new PaintSplotchView(getContext());
        newSplotch.setPadding(10, 10, 10, 10);
        newSplotch.setColor(newColor);
        addView(newSplotch);
    }

    public void removeColor(int color) {
        // Check for 2 children, don't remove if only 2 children left.
        int childCount = getChildCount();
        if (childCount == 2) {
            return;
        }
        for (int childIndex = getChildCount() - 1; childIndex > 0; childIndex--) {
            PaintSplotchView splotchView = (PaintSplotchView) getChildAt(childIndex);
            if (splotchView.getColor() == color) {
                for (int colorIndex = 0; colorIndex < paletteColors.size(); colorIndex++) {
                    if (paletteColors.get(colorIndex) == splotchView.getColor()) {
                        paletteColors.remove(colorIndex);
                    }
                }
                removeView(splotchView);
                if (splotchView.isActive()) {
                    setActiveColor(((PaintSplotchView)getChildAt(0)).getColor());
                }
            }
        }
    }

    public int mixColor (int colorOne, int colorTwo) {
        int redOne = Color.red(colorOne);
        int greenOne = Color.green(colorOne);
        int blueOne = Color.blue(colorOne);

        int redTwo = Color.red(colorTwo);
        int greenTwo = Color.green(colorTwo);
        int blueTwo = Color.blue(colorTwo);

        int mixRed = (redOne + redTwo) / 2;
        int mixGreen = (greenOne + greenTwo) / 2;
        int mixBlue = (blueOne + blueTwo) / 2;

        return Color.rgb(mixRed, mixGreen, mixBlue);
    }

    public int[] getColors() {
        int[] colors = new int[getChildCount()];
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            int childColor = ((PaintSplotchView)child).getColor();
            colors[childIndex] = childColor;
        }
        return colors;
    }
}
