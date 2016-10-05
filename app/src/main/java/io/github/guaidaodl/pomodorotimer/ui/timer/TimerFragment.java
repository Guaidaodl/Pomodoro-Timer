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

package io.github.guaidaodl.pomodorotimer.ui.timer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.guaidaodl.pomodorotimer.R;
import io.github.guaidaodl.pomodorotimer.service.PomodoroService;

public class TimerFragment extends Fragment implements PomodoroService.TomatoStateListener{
    private static final String TAG = "TimerFragment";

    private static final String INTIAL_TIME = "25:00";

    @BindView(R.id.main_timer_control)
    Button mStartButton;

    @BindString(R.string.main_timer_start)
    String mStartText;

    @BindString(R.string.main_timer_stop)
    String mStopText;

    @BindView(R.id.main_timer_time)
    TextView mTimeTextView;

    @NonNull private String mCurrentTime = INTIAL_TIME;

    private TimerFragmentDelegate mDelegate;

    private boolean mTimerRunning = false;

    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    public TimerFragment() {}

    //<editor-fold desc="Life Cycle">
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mDelegate = (TimerFragmentDelegate)context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString()
                    + " must implement TimerFragmentDelegate");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timer, container, false);

        ButterKnife.bind(this, root);

        mStartButton.setText(!mTimerRunning ? mStartText : mStopText);
        mTimeTextView.setText(mCurrentTime);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate.registerTomatoListner(this);
    }

    /**
     * 当 Fragment 被销毁当时候应该取消订阅
     */
    public void onDestroy() {
        super.onDestroy();
        mDelegate.unregisterTomatoListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mDelegate != null) {
            mDelegate = null;
        }
    }
    //</editor-fold>

    @OnClick(R.id.main_timer_control)
    public void onClickStartButton() {
        if (mTimerRunning) {
            // 结束番茄
            mTimerRunning = false;
            resetViews();
            mDelegate.stopTomato();
        } else {
            // 开始番茄
            mStartButton.setText(getContext().getString(R.string.main_timer_stop));
            mDelegate.startNewTomato();
            mTimerRunning = true;
        }
    }


    //<editor-fold desc="Implementation of TomatoStatListner">
    @Override
    public void onTomatoStart() {
        mStartButton.setText(mStopText);
        mTimerRunning = true;
    }

    @Override
    public void onTomatoFinish() {
        mTimerRunning = false;
        resetViews();
    }

    private void resetViews() {
        mCurrentTime = INTIAL_TIME;
        mTimeTextView.setText(INTIAL_TIME);
        mStartButton.setText(mStartText);
    }

    @Override
    public void onTomatoTimeChange(int remainTime, int tomatoTime) {
        long minute = remainTime / 60;
        long second = remainTime % 60;

        @SuppressWarnings("deprecation")
        Locale locale = getResources().getConfiguration().locale;
        mCurrentTime = String.format(locale, "%02d:%02d", minute, second);
        mTimeTextView.setText(mCurrentTime);
    }
    //</editor-fold>

    public interface TimerFragmentDelegate {
        void startNewTomato();
        void stopTomato();
        void registerTomatoListner(PomodoroService.TomatoStateListener listener);
        void unregisterTomatoListener(PomodoroService.TomatoStateListener listener);
    }
}
