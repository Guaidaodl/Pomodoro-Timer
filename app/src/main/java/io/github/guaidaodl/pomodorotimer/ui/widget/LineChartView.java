/*
 * Copyright (c) 2016.  Guaidaodl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.github.guaidaodl.pomodorotimer.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import io.github.guaidaodl.pomodorotimer.R;

/**
 * @author Guaidaodl
 */
public class LineChartView extends View {

    private int []data = {8, 6, 4, 16, 2, 9, 10};
    // 数据是否是有效的
    private boolean mInvalidate = true;

    private int mMax = 0;
    private int mMin = 0;

    private Paint mPaint;
    private Paint mCoordinatePaint;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.AppTheme);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(8.0f);
        mPaint.setColor(context.getResources().getColor(R.color.color_net));

        mCoordinatePaint = new Paint();
        mCoordinatePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getDataCount() == 0) return;

        if (mInvalidate) {
            updateMaxAndMin();
            mInvalidate = false;
        }

        float height = canvas.getHeight() - getPaddingBottom() - getPaddingTop();
        float width = canvas.getWidth() - getPaddingStart() - getPaddingEnd();

        float w = width / (getDataCount() - 1);
        float h = height / (mMax * 1.2f);
        drawGridLine(canvas, w, h);

        float preX = getPaddingStart();
        float preY = height - h * getData(0) + getPaddingTop();
        canvas.drawCircle(preX, preY, 8.0f, mPaint);

        for (int i = 1; i < getDataCount(); i++) {
            float nextX = preX + w;
            float nextY = height - h * getData(i) + getPaddingTop();
            canvas.drawCircle(nextX , nextY, 8.0f, mPaint);
            canvas.drawLine(preX, preY, nextX, nextY, mPaint);
            preX = nextX;
            preY = nextY;
        }
    }

    private void drawGridLine(Canvas canvas, float w, float h) {
        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        float height = canvas.getHeight() - paddingBottom - paddingTop;
        float width = canvas.getWidth() - paddingStart - paddingEnd;

        mCoordinatePaint.setStrokeWidth(8.0f);
        // 横轴
        float y = height + paddingTop;
        canvas.drawLine(paddingStart - 4.0f, y , width + paddingStart, y, mCoordinatePaint);
        y -= 4 * h;
        // 纵轴
        canvas.drawLine((float) paddingStart, paddingTop, (float) paddingStart, height + paddingTop + 4.0f, mCoordinatePaint);

        mCoordinatePaint.setStrokeWidth(1.0f);
        while (y >= paddingTop) {
            canvas.drawLine(paddingStart, y, width + paddingStart, y, mCoordinatePaint);
            y -= 4 * h;
        }
    }

    private void updateMaxAndMin() {
        mMax = data[0];
        mMin = data[0];
        for (int d : data) {
            if (d > mMax) {
                mMax = d;
            }
            if (d < mMin) {
                mMin = d;
            }
        }
    }

    private int getDataCount() {
        return data.length;
    }

    private int getData(int index) {
        return data[index];
    }

    public void setData(int[] data) {
        this.data = data;
        mInvalidate = true;
        invalidate();
    }
}
