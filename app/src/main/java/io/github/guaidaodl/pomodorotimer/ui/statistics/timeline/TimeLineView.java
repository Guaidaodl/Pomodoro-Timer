/*
 *  Copyright (c) 2016.  Guaidaodl
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
 *
 */

package io.github.guaidaodl.pomodorotimer.ui.statistics.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.github.guaidaodl.pomodorotimer.R;
import io.github.guaidaodl.pomodorotimer.data.realm.Tomato;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Guaidaodl on 20/10/2016.
 */
public class TimeLineView extends View {

    private Paint mTextPaint = new Paint();
    private Paint mNetPaint = new Paint();
    private Paint mBlockPaint = new Paint();

    private float mTop;
    private float mLeft;
    private float mRight;
    private float mBottom;
    private float mCellHeight;
    private float mCellWidth;
    private float mAvailableHeight;
    private float mAvailableWidth;

    private float mScrollX = 100;

    @Nullable
    private TimeLineAdapter mAdapter;

    public TimeLineView(Context context) {
        this(context, null);
    }

    public TimeLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(getResources().getColor(R.color.black));

        mNetPaint.setStyle(Paint.Style.STROKE);
        mNetPaint.setStrokeWidth(4);

        mBlockPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBlockPaint.setColor(getResources().getColor(R.color.accentColor));
        mAdapter = new TimeLineAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Date getDate(int i) {
                return null;
            }

            @Override
            public List<Tomato> getTomatos(int i) {
                ArrayList<Tomato> tomatos = new ArrayList<>();
                Tomato t = new Tomato();

                t.setStartTime(new GregorianCalendar(2016, 10, 1, 12, 12).getTimeInMillis());
                t.setEndTime(new GregorianCalendar(2016, 10, 1, 12, 37).getTimeInMillis());
                tomatos.add(t);
                return tomatos;
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mAvailableHeight = canvas.getHeight() - getPaddingTop() - getPaddingBottom();
        mAvailableWidth = canvas.getWidth() - getPaddingEnd() - getPaddingStart();
        mTop = getPaddingTop();
        mBottom = mTop + mAvailableHeight;
        mLeft = getPaddingLeft();
        mRight = mLeft + mAvailableWidth;
        mCellHeight = mAvailableHeight / 25;
        mCellWidth = mAvailableWidth / 5;

        drawTimeNet(canvas);
        drawTime(canvas);
        drawData(canvas);
    }

    private void drawTimeNet(Canvas canvas) {

        float y = mTop;
        for (int i = 0; i < 26; i++) {
            canvas.drawLine(mLeft, y, mRight, y, mNetPaint);
            y += mCellHeight;
        }

        canvas.drawLine(mLeft, mTop, mLeft, mBottom, mNetPaint);
        canvas.drawLine(mRight, mTop, mRight, mBottom, mNetPaint);
        canvas.drawLine(mLeft + mCellWidth, mTop, mLeft + mCellWidth, mBottom, mNetPaint);
    }

    private void drawTime(Canvas canvas) {
        float textSize = mCellHeight * 0.8f;
        mTextPaint.setTextSize(textSize);
        float textWidth = mTextPaint.measureText("00:00");

        float y = mTop + textSize + mCellHeight;
        float x = mLeft + (mCellWidth - textWidth) / 2;
        for (int i = 0; i < 24; i++) {
            String timeString = String.format("%02d:00", i);
            canvas.drawText(timeString, x, y, mTextPaint);
            y += mCellHeight;
        }
    }

    private void drawData(Canvas canvas) {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }

        int index = (int)(Math.floor(mScrollX / mCellWidth));
        float startX = mLeft + mCellWidth;
        float endX = (mCellWidth * (index + 1) - mScrollX) + startX;

        while(startX < mRight && index < mAdapter.getCount()) {
            List<Tomato> tomatos = checkNotNull(mAdapter.getTomatos(index));
            for (Tomato tomato : tomatos) {
                drawTomato(canvas, startX, endX, tomato);
            }

            canvas.drawLine(endX, mTop, endX, mBottom, mNetPaint);
            startX = endX;
            endX = Math.min(endX + mCellWidth, mRight);
            index ++;
        }
    }

    private void drawTomato(Canvas canvas, float startX, float endX, Tomato tomato) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tomato.getStartTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        float startY = mTop + (mAvailableHeight - mCellHeight) * (hour + minute / 60f) / 24
                            + mCellHeight + 2;

        calendar.setTimeInMillis(tomato.getEndTime());
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        float endY = mTop + (mAvailableHeight - mCellHeight) * (hour + minute / 60f) / 24
                        + mCellHeight - 2;

        canvas.drawRect(startX, startY, endX, endY, mBlockPaint);
    }

    public void setAdapter(@NonNull TimeLineAdapter adapter) {
        checkNotNull(adapter);
        mAdapter = adapter;
    }
}
