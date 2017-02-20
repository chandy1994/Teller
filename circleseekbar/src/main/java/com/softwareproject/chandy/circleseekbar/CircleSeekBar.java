package com.softwareproject.chandy.circleseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Chandy on 2016/11/25.
 */
//自定义的圆形的seekbar，实现滑动功能
//1.第一步实现了set一个drawable当作thumb


public class CircleSeekBar extends View {
    private final String TAG = "CircleSeekBar";

    private Context mcontext = null;

    private Drawable thumb = null;
    private float thumbHeight = 0;
    private float thumbWidth = 0;
    private float thumbLeft = 0;
    private float thumbTop = 0;
    private float thumbSize = 0;
    private int[] thumbNormal = null;
    private int[] thumbPressed = null;

    private int maxProgress = 100;
    private int curProgress = 0;
    private float progressDegree = 0;

    private Paint backgroundPaint = null;
    private Paint frontPaint = null;
    private int backColor = 0;
    private int frontColor = 0;

    private Paint thumbPaint;
    private int thumbColor;

    private float seekBarWidth = 0;
    private int seekbarSize = 0;
    private int seekbarRadius = 0;
    private int seekbarCenterX = 0;
    private int seekbarCenterY = 0;
    private double lastradian = 0;


    private RectF rectF;
    private final static double PI = Math.PI;
    public boolean enable = true;

    private enum Status {
        IDEL, MOVE;
    }

    private Status status;
    //seekbar的监听器
    private OnSeekBarChangeListener mOnSeekBarChangeListener = null;

    //内部类 接口
    public interface OnSeekBarChangeListener {

        void onProgressChanged(int progress);

        void onStartTrackingTouch();

        void onStopTrackingTouch();
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean flag) {
        enable = flag;
    }

    //构造方法，从xml文档当中读取参数信息；
    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mcontext = context;
        rectF = new RectF();
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CircleSeekBar, defStyleAttr, defStyleRes);

        thumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed};
        thumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed};

        seekBarWidth = a.getDimension(R.styleable.CircleSeekBar_progress_width, 40);
        backColor = a.getColor(R.styleable.CircleSeekBar_progress_background, Color.GRAY);
        frontColor = a.getColor(R.styleable.CircleSeekBar_progress_front, Color.BLUE);
        maxProgress = a.getInteger(R.styleable.CircleSeekBar_progress_max, 100);
        thumb = a.getDrawable(R.styleable.CircleSeekBar_android_thumb);
        if (null != thumb) {
            thumbWidth = thumb.getIntrinsicWidth();
            thumbHeight = thumb.getIntrinsicHeight();
        } else {
            thumbColor = frontColor;
            thumbPaint = new Paint();
            thumbPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            thumbPaint.setAntiAlias(true);
            thumbPaint.setColor(thumbColor);
            thumbSize = a.getDimension(R.styleable.CircleSeekBar_thumbSize, seekBarWidth * 2);
            thumbHeight = thumbWidth = thumbSize;
        }


        //设置画笔
        backgroundPaint = new Paint();
        frontPaint = new Paint();
        backgroundPaint.setColor(backColor);
        frontPaint.setColor(frontColor);
        backgroundPaint.setAntiAlias(true);
        frontPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        frontPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(seekBarWidth);
        frontPaint.setStrokeWidth(seekBarWidth);
        status = Status.IDEL;


        a.recycle();


    }

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSeekBar(Context context) {
        this(context, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //不要用getRawX 这是屏幕上的绝对坐标
        //用getx获取相对坐标
        float pointX = event.getX();
        float pointY = event.getY();
        boolean flag = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (null != mOnSeekBarChangeListener) {
                    mOnSeekBarChangeListener.onStartTrackingTouch();
                }
                flag = moveThumb(pointX, pointY, false, false);
                break;
            case MotionEvent.ACTION_MOVE:
                if (null != mOnSeekBarChangeListener) {
                    mOnSeekBarChangeListener.onProgressChanged(curProgress);
                }
                flag = moveThumb(pointX, pointY, false, true);
                break;
            case MotionEvent.ACTION_UP:
                if (null != mOnSeekBarChangeListener) {
                    mOnSeekBarChangeListener.onStopTrackingTouch();
                }
                flag = moveThumb(pointX, pointY, true, false);
                status = Status.IDEL;
                break;
        }
        return flag;
    }


    //我们要自己测量控件的宽和高

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.EXACTLY) {

            seekbarSize = widthSize > heightSize ? heightSize : widthSize;
        }
        else {
            seekbarSize = widthSize = heightSize = getDefaultSize(200,MeasureSpec.EXACTLY);
        }
        seekbarCenterX = widthSize / 2;
        seekbarCenterY = heightSize / 2;
        seekbarRadius = (seekbarSize - (int) thumbWidth) / 2;
        int left = seekbarCenterX - seekbarRadius;
        int right = seekbarCenterX + seekbarRadius;
        int top = seekbarCenterY - seekbarRadius;
        int bottom = seekbarCenterY + seekbarRadius;
        rectF.set(left, top, right, bottom);
        setThumbPosition(0);

        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(seekbarCenterX, seekbarCenterY, seekbarRadius,
                backgroundPaint);
        canvas.drawArc(rectF, -90, progressDegree, false, frontPaint);
        drawThumb(canvas);
    }

    public void drawThumb(Canvas canvas) {
        if (null != thumb) {
            thumb.setBounds((int) thumbLeft, (int) thumbTop, (int) (thumbLeft + thumbWidth), (int) (thumbTop + thumbHeight));
            thumb.draw(canvas);
        }
        canvas.drawCircle(thumbLeft + thumbWidth / 2, thumbTop + thumbHeight / 2, thumbHeight / 2, thumbPaint);

    }

    public boolean moveThumb(float eventX, float eventY, boolean isPointerUp, boolean isMoving) {
        if (((isPointerOn(eventX, eventY) && !isPointerUp) || (status == Status.MOVE && isMoving)) && enable) {
            if (null != thumb) {
                thumb.setState(thumbPressed);

            }
            status = Status.MOVE;
            double radian = Math.atan2((seekbarCenterX - eventX), (seekbarCenterY - eventY));
            //y与直线的夹角[-Pi,Pi]

//
            if (radian < 0) {
                radian += PI * 2;
            }
            radian = 2 * PI - radian;
            if (((radian - lastradian)) > PI) {
                radian = 0;
            }
            if (((lastradian - radian)) > PI) {
                radian = 2 * PI;
            }
            lastradian = radian;

            setThumbPosition((float) radian);
            progressDegree = Math.round(Math.toDegrees(radian));
            curProgress = (int) (progressDegree / 360 * maxProgress);
            if (null != mOnSeekBarChangeListener) {
                mOnSeekBarChangeListener.onProgressChanged(curProgress);
            }
            invalidate();
            return true;
        } else {
            if (null != thumb) {
                thumb.setState(thumbNormal);
            }
            return false;
        }
    }

    public boolean isPointerOn(float eventX, float eventY) {
        float dist = (float) Math.sqrt(Math.pow(eventY - seekbarCenterY, 2) + Math.pow(eventX - seekbarCenterX, 2));
        if (dist > seekbarRadius - thumbWidth / 2 && dist < seekbarRadius + thumbWidth / 2) {
            return true;
        }
        return false;

    }

    public void setThumbPosition(float radians) {
        radians = radians - (float) (0.5 * PI);
        thumbLeft = (float) seekbarCenterX + seekbarRadius * (float) Math.cos(radians) - thumbWidth / 2;
        thumbTop = (float) seekbarCenterY + seekbarRadius * (float) Math.sin(radians) - thumbWidth / 2;
        //Log.d("left",thumbLeft+"");
    }


    public synchronized void setProgress(int i) {
        if (i < 0) {
            curProgress = 0;
        }
        if (i > maxProgress) {
            curProgress = maxProgress;
        }
        curProgress = i;
        progressDegree = (i / 360 * maxProgress);
        setThumbPosition((float) Math.toRadians(progressDegree));
        invalidate();

    }

    public void setThumb(int d) {
        thumb = mcontext.getResources().getDrawable(d);
        thumbWidth = thumb.getIntrinsicWidth();
        thumbHeight = thumb.getIntrinsicHeight();
    }

    public int getProgress() {
        return curProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        mOnSeekBarChangeListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

}
