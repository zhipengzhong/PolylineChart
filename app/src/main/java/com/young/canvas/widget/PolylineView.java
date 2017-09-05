package com.young.canvas.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by 钟志鹏 on 2017/9/4.
 */

public class PolylineView extends View {

    private static final String TAG = "CanvasView";
    private Paint mPolylinePaint;
    private Path mPath;
    private Paint mCirclePaint;
    private ArrayList<Integer> mDatas;
    private Paint mDashPaint;
    private int mRow = 2;
    private int mColumn = 3;
    private Paint mBorderPaint;
    private float mMax = 100F;
    private float mMini = 20F;
    private Paint mTextPaint;
    private float mTextPadding = 6F;
    private int mBorderPaintWidth;
    private float mDrawPolylineWidth;
    private float mDrawPolylineHeaght;
    private Paint mSelectedLinePaint;
    private float mSelectedLineX;
    private boolean isDrawSelectedLine;
    private float mSelectedLineY;
    private OnSelectedDotListener mOnSelectedDotListener;
    private int mSelectedLineNum;

    public PolylineView(Context context) {
        this(context, null);
    }

    public PolylineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaint();
    }

    private void initPaint() {
        mPolylinePaint = new Paint();
        mPolylinePaint.setAntiAlias(true);
        mPolylinePaint.setColor(Color.RED);
        mPolylinePaint.setStrokeWidth(2);
        mPolylinePaint.setStyle(Paint.Style.STROKE);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStrokeWidth(1);

        mDashPaint = new Paint();
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{3, 6}, 0));
        mDashPaint.setAntiAlias(true);
        mDashPaint.setColor(Color.GRAY);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(2);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(0xFFBBBBBB);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaintWidth = 2;
        mBorderPaint.setStrokeWidth(mBorderPaintWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(20);

        mSelectedLinePaint = new Paint();
        mSelectedLinePaint.setAntiAlias(true);
        mSelectedLinePaint.setColor(Color.RED);
        mSelectedLinePaint.setStrokeWidth(1);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: " + event.getX() + "|" + event.getRawX());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawSelectedLine = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int size = mDatas.size();
                float gap = getMeasuredWidth() * 1.0F / (size - 1);
                float single = mDrawPolylineHeaght / (mMax - mMini);
                float max = Collections.max(mDatas);

                float x = event.getX();
                float floor = (float) Math.floor(x / gap);
                float dec = x / gap - floor;
                if (dec >= 0.5) {
                    floor += 1;
                }
                if (floor < 0) {
                    floor = 0;
                }
                if (floor >= size) {
                    floor = size - 1;
                }

                mSelectedLineX = getMeasuredWidth() * floor / (size - 1);
                mSelectedLineY = (Math.abs(mDatas.get((int) floor) * single - (max * single)) + ((mMax - max) * single));
                if (mSelectedLineNum != (int) floor) {
                    mSelectedLineNum = (int) floor;
                    mOnSelectedDotListener.OnSelectedDot(mDatas.get(mSelectedLineNum));
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isDrawSelectedLine = false;
                invalidate();
                break;
        }


        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: " + MeasureSpec.getSize(widthMeasureSpec) + "|" + MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolyline(canvas);
        drawDot(canvas);
        drawDashed(canvas);
        drawBorder(canvas);
        drawText(canvas);
        drawSelectedLine(canvas);
    }

    private void drawSelectedLine(Canvas canvas) {
        if (isDrawSelectedLine) {
            canvas.drawLine(mSelectedLineX, 0, mSelectedLineX, getMeasuredHeight(), mSelectedLinePaint);
            canvas.drawCircle(mSelectedLineX, mSelectedLineY, 6, mSelectedLinePaint);
        }
    }

    private void drawText(Canvas canvas) {
        int rowNum = getMeasuredHeight() / (mRow + 1);
        float mapY = (mMax - mMini) / (mRow + 1);
//        int columnNum = getMeasuredWidth() / (mColumn + 1);

        String text = String.format("%.2f", mMax);
        Rect rect = measureTest(text);
        canvas.drawText(text, 0 + mTextPadding, 0 + rect.height() + mTextPadding, mTextPaint);
        for (int i = 1; i <= mRow + 1; i++) {
            int rowY = rowNum * i;
            canvas.drawText(String.format("%.2f", mMax - (mapY * i)), 0 + mTextPadding, rowY - mTextPadding, mTextPaint);
        }


//        for (int i = 1; i <= mColumn; i++) {
//            int columnX = columnNum * i;
//        }
    }

    private void drawDot(Canvas canvas) {

    }

    private void drawPolyline(Canvas canvas) {
        mDrawPolylineWidth = getMeasuredWidth() - (mBorderPaintWidth * 2);
        mDrawPolylineHeaght = getMeasuredHeight() - (mBorderPaintWidth * 2);
        float single = mDrawPolylineHeaght / (mMax - mMini);

        if (mDatas != null) {
            mPath = new Path();
            Random random = new Random();
            float max = Collections.max(mDatas);
            int size = mDatas.size();
            for (int i = 0; i < size; i++) {
                float x = getMeasuredWidth() * i / (size - 1);
                float y = (Math.abs(mDatas.get(i) * single - (max * single)) + ((mMax - max) * single));
                if (i == 0) {
                    mPath.moveTo(0, y);
                } else {
                    mPath.lineTo(x, y);
                }
//                canvas.drawCircle(x, y, 4, mCirclePaint);
            }
            canvas.drawPath(mPath, mPolylinePaint);
        }
    }

    private void drawBorder(Canvas canvas) {
        int offset = mBorderPaintWidth / 2;
        canvas.drawRect(offset, offset, getMeasuredWidth() - offset, getMeasuredHeight() - offset, mBorderPaint);
    }

    private void drawDashed(Canvas canvas) {
        int rowNum = getMeasuredHeight() / (mRow + 1);
        int columnNum = getMeasuredWidth() / (mColumn + 1);
        Path path = new Path();
        for (int i = 1; i <= mRow; i++) {
            int rowY = rowNum * i;
            path.moveTo(0, rowY);
            path.lineTo(getMeasuredWidth(), rowY);
        }
        for (int i = 1; i <= mColumn; i++) {
            int columnX = columnNum * i;
            path.moveTo(columnX, 0);
            path.lineTo(columnX, getMeasuredHeight());
        }
        canvas.drawPath(path, mDashPaint);
    }

    private Rect measureTest(String text) {
        Rect bount = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), bount);
        return bount;
    }

    public void setData(ArrayList<Integer> list) {
        mDatas = list;
        invalidate();
    }

    public void setOnSelectedDotListener(OnSelectedDotListener l) {
        mOnSelectedDotListener = l;
    }

    public interface OnSelectedDotListener {
        void OnSelectedDot(float value);
    }
}
