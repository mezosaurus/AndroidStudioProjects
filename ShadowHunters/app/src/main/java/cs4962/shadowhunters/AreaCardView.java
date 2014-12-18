package cs4962.shadowhunters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ethan on 12/17/2014.
 */
public class AreaCardView extends TextView {
    private AreaCard mCard;

    public AreaCardView(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);
    }

    public AreaCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);
    }

    public AreaCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
        this.setTypeface(face);
    }

    public void setCard(AreaCard mCard) {
        this.mCard = mCard;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(32);
        canvas.drawPaint(textPaint);
        drawBorder(canvas);
        drawSquare(canvas);

        String cardText = "(" + mCard.getRollOne() + ")";
        if (mCard.getRollTwo() != 0)
            cardText += " (" + mCard.getRollTwo() + ")\n\n";
        else
            cardText += "\n\n";
        cardText += mCard.getName() + "\n\n" + mCard.getText();
        int xPos = (getWidth() / 2) - (int)(textPaint.measureText(cardText)/2);
        int yPos = (int) ((getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText(cardText, xPos, yPos, textPaint);
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
        paint.setColor(Color.BLACK);

        RectF rect = new RectF();
        rect.left = 0;
        rect.top = 0;
        rect.right = getWidth();
        rect.bottom = getHeight();

        canvas.drawRect(rect, paint);
    }

    private void drawText(Canvas canvas) {


        // Build text string
        String cardText = "(" + mCard.getRollOne() + ")";
        if (mCard.getRollTwo() != 0)
            cardText += " (" + mCard.getRollTwo() + ")\n\n";
        else
            cardText += "\n\n";
        cardText += mCard.getName() + "\n\n" + mCard.getText();


    }
}
