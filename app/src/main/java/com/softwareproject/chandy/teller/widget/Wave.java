package com.softwareproject.chandy.teller.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by Chandy on 2016/12/9.
 */

//水波
public class Wave extends View {
    private int height;
    private int width;
    private double PI = Math.PI;
    private float[] yPosition;
    private Paint firstPaint;
    private Paint secondPaint;
    private int oneOffset;
    private int twoOffset;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private float mCycleFactorW;

    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 7;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 7;
    private static float STRETCH_FACTOR_A = 20;
    private static int OFFSET_Y = 0;
    private boolean stable = true;
    private boolean accelerate = false;


    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;

    float progress;
    float factorA;
    float heighBound;
    float lowBound;
    float waveOneHeight;
    float waveTwoHeight;

    private DrawFilter mDrawFilter;

    public Wave(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Wave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mXOffsetSpeedOne = TRANSLATE_X_SPEED_ONE;
        mXOffsetSpeedTwo = TRANSLATE_X_SPEED_TWO;
        factorA = STRETCH_FACTOR_A;
        firstPaint = new Paint();
        firstPaint.setAntiAlias(true);
        firstPaint.setStyle(Paint.Style.FILL);
        firstPaint.setColor(Color.GREEN);

        secondPaint = new Paint();
        secondPaint.setAntiAlias(true);
        secondPaint.setStyle(Paint.Style.FILL);
        secondPaint.setColor(Color.YELLOW);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    public static int getOffsetY() {
        return OFFSET_Y;
    }

    public static void setOffsetY(int offsetY) {
        OFFSET_Y = offsetY;
    }

    public static float getStretchFactorA() {
        return STRETCH_FACTOR_A;
    }

    public static void setStretchFactorA(float stretchFactorA) {
        STRETCH_FACTOR_A = stretchFactorA;
    }


    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSize > heightSize) {
            width = height = heightSize;
        } else {
            width = height = widthSize;
        }
        progress = 0;
        setMeasuredDimension(width, height);

    }


    public float getWaveOneHeight() {
        return waveOneHeight;
    }

    public float getWaveTwoHeight() {
        return waveTwoHeight;
    }

    public void setWaveHeightDelta(float height) {
        progress = height;
    }

    public void setAccelerate(boolean accelerate) {
        this.accelerate = accelerate;
    }

    public boolean isAccelerate() {
        return accelerate;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);

        Random random = new Random();
        if (accelerate) {
            random.setSeed(System.currentTimeMillis() % 13);
            mXOffsetSpeedOne = random.nextInt(10) + TRANSLATE_X_SPEED_ONE * 2;
            random.setSeed(System.currentTimeMillis() % 11);
            mXOffsetSpeedTwo = random.nextInt(10) + TRANSLATE_X_SPEED_TWO * 2;
        } else {
            random.setSeed(System.currentTimeMillis() % 13);
            mXOffsetSpeedOne = random.nextInt(20) + TRANSLATE_X_SPEED_ONE;
            random.setSeed(System.currentTimeMillis() % 11);
            mXOffsetSpeedTwo = random.nextInt(20) + TRANSLATE_X_SPEED_TWO;
        }

        if (!stable) {
            random.setSeed(System.currentTimeMillis() % 19);
            factorA = STRETCH_FACTOR_A + random.nextInt(20);
        } else {
            factorA = STRETCH_FACTOR_A;
        }

        resetPositonY();
        for (int i = 0; i < width; i++) {
            heighBound = (float) -Math.sqrt(Math.pow((double) height / 2, 2) - Math.pow((double) height / 2 - i, 2)) + (float) height / 2;
            lowBound = (float) Math.sqrt(Math.pow((double) height / 2, 2) - Math.pow((double) height / 2 - i, 2)) + (float) height / 2;
            waveOneHeight = height - mResetOneYPositions[i] - progress ;
            waveTwoHeight = height - mResetTwoYPositions[i] - progress ;

            //波纹高度height - mResetOneYPositions[i]
            //波纹下界(float) Math.sqrt(Math.pow((double)height/2,2)-Math.pow((double)height/2-i,2))+(float)height/2
            //波纹上界(float) -Math.sqrt(Math.pow((double)height/2,2)-Math.pow((double)height/2-i,2))+(float)height/2

            // 绘制第一条水波纹


            //上下界是视觉上的   坐标从上到下递增
            //水波大于下界的时候（低于）只绘制下界
            if (waveOneHeight > lowBound) {
                canvas.drawLine(i, lowBound, i,
                        lowBound,
                        firstPaint);

            }
            if (waveTwoHeight > lowBound) {
                canvas.drawLine(i, lowBound, i,
                        lowBound,
                        secondPaint);
            }
            if (waveOneHeight <= lowBound && waveOneHeight >= heighBound) {
                canvas.drawLine(i, waveOneHeight, i,
                        lowBound,
                        firstPaint);
            }
            if (waveTwoHeight <= lowBound && waveTwoHeight >= heighBound) {
                canvas.drawLine(i, waveTwoHeight, i,
                        lowBound,
                        secondPaint);
            }
            if (waveOneHeight < heighBound) {
                canvas.drawLine(i, heighBound, i,
                        lowBound,
                        firstPaint);

            }
            if (waveTwoHeight < heighBound) {
                canvas.drawLine(i, heighBound, i,
                        lowBound,
                        secondPaint);
            }

        }

        // 改变两条波纹的移动点
        oneOffset += mXOffsetSpeedOne;
        twoOffset += mXOffsetSpeedTwo;


        // 如果已经移动到结尾处，则重头记录
        if (oneOffset >= width) {
            oneOffset = 0;
        }
        if (twoOffset >= width) {
            twoOffset = 0;
        }

        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        postInvalidate();
    }

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = yPosition.length - oneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(yPosition, oneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(yPosition, 0, mResetOneYPositions, yOneInterval, oneOffset);

        int yTwoInterval = yPosition.length - twoOffset;
        System.arraycopy(yPosition, twoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(yPosition, 0, mResetTwoYPositions, yTwoInterval, twoOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 用于保存原始波纹的y值
        yPosition = new float[width];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[width];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[width];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / width);

        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < width; i++) {
            yPosition[i] = (float) (factorA * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }
}
