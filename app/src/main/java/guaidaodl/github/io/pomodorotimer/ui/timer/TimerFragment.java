package guaidaodl.github.io.pomodorotimer.ui.timer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guaidaodl.github.io.pomodorotimer.R;
import guaidaodl.github.io.pomodorotimer.utils.RxCountDownTimer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class TimerFragment extends Fragment {
    private static final String TAG = "TimerFragment";

    private static final String INTIAL_TIME = "25:00";

    @BindView(R.id.main_timer_start)
    Button mStartButton;

    @BindString(R.string.main_timer_start)
    String mStartText;

    @BindString(R.string.main_timer_stop)
    String mStopText;

    @BindView(R.id.main_timer_time)
    TextView mTimeText;

    @Nullable
    private TimeSuscriber mTimeSuscriber;

    @NonNull
    private String mCurrentTime = INTIAL_TIME;

    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    public TimerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View root = inflater.inflate(R.layout.fragment_timer, container, false);

        ButterKnife.bind(this, root);

        mStartButton.setText(mTimeSuscriber == null ? mStartText : mStopText);
        mTimeText.setText(mCurrentTime);
        return root;
    }

    /**
     * 当 Fragment 被销毁当时候应该取消订阅
     */
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mTimeSuscriber != null) {
            mTimeSuscriber.unsubscribe();
        }
    }


    @OnClick(R.id.main_timer_start)
    public void onClickStartButton() {
        if (mTimeSuscriber != null) {
            mTimeSuscriber.unsubscribe();
            mTimeText.setText(INTIAL_TIME);
            mStartButton.setText(getContext().getString(R.string.main_timer_start));
            mTimeSuscriber = null;
        } else {
            mStartButton.setText(getContext().getString(R.string.main_timer_stop));
            mTimeSuscriber = new TimeSuscriber();
            RxCountDownTimer.newCountDownTimer(25 * 60)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mTimeSuscriber);
        }
    }

    private class TimeSuscriber extends Subscriber<String> {
        @Override
        public void onCompleted() {
            Log.d(TAG, "completed");
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(String time) {
            mCurrentTime = time;
            mTimeText.setText(time);
        }
    };
}
