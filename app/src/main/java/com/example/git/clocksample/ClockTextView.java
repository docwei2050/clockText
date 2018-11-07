package com.example.git.clocksample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * 单行显示  10:00
 * 两行显示   5
 *          分钟
 */
public class ClockTextView extends View {
    private String mText1; //更新的文本1
    private String mText2; //更新的文本2

    private Paint mPaint;
    private TextPaint mTextPaint;
    private int mDefaultWidht;//默认的宽度
    private int mDefaultHeight; //默认的高度
    private float mTextSize;//文本的大小
    private int mWidth; //实际的宽高
    private int mHeight;//实际的高度

    private double mBaseWidth;
    private int mBg_color;
    private int mText_color;
    private String mXmlText1;
    private String mXmlText2;


    public ClockTextView(Context context) {
        this(context, null);
    }

    public ClockTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClockTextView);
        mBg_color = ta.getColor(R.styleable.ClockTextView_clock_bg_color, Color.parseColor("#FF4665FF"));
        mText_color = ta.getColor(R.styleable.ClockTextView_clock_text_color, Color.WHITE);
        //默认25dp
        mTextSize = ta.getDimension(R.styleable.ClockTextView_clock_text_size, dip2px(context, 25));
        mText1 = ta.getString(R.styleable.ClockTextView_clock_text1);
        mText2 = ta.getString(R.styleable.ClockTextView_clock_text2);


        ta.recycle();
        init();
    }

    private void init() {

        mDefaultWidht = dip2px(getContext(), 40);
        mDefaultHeight = dip2px(getContext(), 40);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mBg_color);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setColor(mText_color);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mTextPaint.setTextSize(mTextSize);



    }

    public void setText(String text) {
        mText1 = text;
        postInvalidate();
    }

    public void setText(String text1, String text2) {
        mText1 = text1;
        mText2 = text2;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (h != oldh && w != oldw) {
            mWidth = w;
            mHeight = h;
            //控件里面最大的圆，圆里面最大正方形的宽高
            mBaseWidth = mWidth * 1.0d / Math.sqrt(2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制考虑padding
        int contentWidht = mWidth - getPaddingLeft() - getPaddingRight();
        int contentHeight = mHeight - getPaddingTop() - getPaddingBottom();
        //1.绘制背景色
        int radius = Math.min(contentWidht, contentHeight) / 2;
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mPaint);

        if(TextUtils.isEmpty(mText1)){
            return;
        }
        //2.绘制文本
        if (TextUtils.isEmpty(mText2)) {
            //单行的情形
            mTextPaint.setTextSize(mTextSize);

            //如果文本内容的宽度大于控件的宽度，需要调整字体的size
            mTextPaint.setTextSize(getSuitableSingleSize(mText1, mTextSize));
            float textWidth = mTextPaint.measureText(mText1, 0, mText1.length());
            //基线
            float baseLine = mHeight / 2 + (Math.abs(mTextPaint.ascent()) - mTextPaint.descent()) / 2;
            canvas.drawText(mText1, (mWidth - textWidth) / 2, baseLine, mTextPaint);

        } else {
            mTextPaint.setTextSize(mTextSize);

            //两行显示的情形
            mTextPaint.setTextSize(getSuitableTwoLineSize(mText1, mTextSize, false));
            //获取文本的宽度
            float textWidth = mTextPaint.measureText(mText1, 0, mText1.length());

            //获取文本的高度
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float height = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading;
            //7 / 10这个比例只是校正
            float y = (mHeight - height * 7 / 10) * 1.0f / 2;
            float basline1 = y + (Math.abs(mTextPaint.ascent()) - mTextPaint.descent()) / 2;
            canvas.drawText(mText1, (mWidth - textWidth) / 2, basline1, mTextPaint);


            //绘制第二行，不要加粗
            mTextPaint.setFakeBoldText(false);
            //重置size
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setTextSize(getSuitableTwoLineSize(mText2, mTextSize, true));
            float textWidth2 = mTextPaint.measureText(mText2, 0, mText2.length());
            //7 / 10这个比例只是校正
            float basline2 = (mHeight + height * 7 / 10) * 1.0f / 2 + (Math.abs(mTextPaint.ascent()) - mTextPaint.descent()) / 2;
            canvas.drawText(mText2, (mWidth - textWidth2) / 2, basline2, mTextPaint);
        }
    }

    /**
     * 显示单行，自动调整大小
     *
     * @param text     文本
     * @param textSize 文本的size
     */
    private float getSuitableSingleSize(String text, float textSize) {
        if (TextUtils.isEmpty(text)) {
            return mTextSize;
        }
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading;
        float textWidth = mTextPaint.measureText(text, 0, text.length());
        //校正显示效果
        //以圆里面最大的正方形为参考显得小，加上(mWidth - mBaseWidth) / 2 做校正
        if (textWidth > (mBaseWidth + (mWidth - mBaseWidth) / 2) || height > mWidth) {
            textSize = textSize - 5;
            mTextPaint.setTextSize(textSize);
        } else {
            return textSize;
        }
        return getSuitableSingleSize(text, textSize);
    }

    /**
     * 显示两行，自动调整大小
     *
     * @param text     文本
     * @param textSize 文本的size
     */
    private float getSuitableTwoLineSize(String text, float textSize, boolean isSecondLine) {
        if (TextUtils.isEmpty(text)) {
            return mTextSize;
        }
        if (!isSecondLine) {
            //默认3个字节的显示效果，小于3需要手动匹配
            if (text.length() < 3) {
                if (text.length() == 1) {
                    final String textStr = text;
                    text = text.concat(textStr).concat(textStr);
                } else {
                    final String textStr = text.substring(0, 1);
                    text = text.concat(textStr);
                }
            }
        }
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading;
        float textWidth = mTextPaint.measureText(text, 0, text.length());
        //校正显示效果
        //以圆里面最大的正方形为参考显得小，加上(mWidth - mBaseWidth) / 2 做校正
        if (textWidth > (mBaseWidth - (mWidth - mBaseWidth) / 2) || height > (mBaseWidth)) {
            textSize = textSize - 1;
            mTextPaint.setTextSize(textSize);
        } else {
            return textSize;
        }
        return getSuitableTwoLineSize(text, textSize, isSecondLine);
    }


    //------------------------------------view的测量解决Warp_content默认Match_paren的效果---------start------------
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasureSize(mDefaultWidht, widthMeasureSpec), getMeasureSize(mDefaultHeight, heightMeasureSpec));
    }

    private int getMeasureSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }
    //------------------------------------view的测量解决Warp_content默认Match_paren的效果----------end-----------

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
