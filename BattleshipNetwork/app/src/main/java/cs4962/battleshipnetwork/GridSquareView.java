package cs4962.battleshipnetwork;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Ethan on 10/28/2014.
 */
public class GridSquareView extends View {

    // The grid square identifier with pattern [A-J][1-10]
    private String mPosition;
    private String mLetterNumber;
    private int mColor;
    private int mTextPos = 0;

    public GridSquareView(Context context, int height, int width) {
        super(context);
        setPadding(1, 1, 1, 1);
        setMinimumHeight(height);
        setMinimumWidth(width);
        mColor = Color.BLUE;
    }

    public String getPosition() {
        return mPosition;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }

    public void setLetterNumber(String s) { mLetterNumber = s; }

    public void setPosition(String location) {
        mPosition = location;
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("ONTOUCHSQUARE", mPosition+"TOUCHED");
        return true;
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(getHeight());
        canvas.drawPaint(textPaint);
        drawBorder(canvas);
        drawSquare(canvas);

        if (mPosition == "") {
            int xPos = (getWidth() / 2) - (int)(textPaint.measureText(mLetterNumber)/2);
            int yPos = (int) ((getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
            canvas.drawText(mLetterNumber, xPos, yPos, textPaint);
            mTextPos++;
        }
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

    private void drawSquare(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mColor);

        RectF rect = new RectF();
        rect.left = getPaddingLeft();
        rect.top = getPaddingTop();
        rect.right = getWidth() - getPaddingRight();
        rect.bottom = getHeight() - getPaddingBottom();

        canvas.drawRect(rect, paint);
    }

    private void drawBorder(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        RectF rect = new RectF();
        rect.left = 0;
        rect.top = 0;
        rect.right = getWidth();
        rect.bottom = getHeight();

        canvas.drawRect(rect, paint);
    }
}
