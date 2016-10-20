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

package io.github.guaidaodl.pomodorotimer.ui.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.ImmutableListMultimap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.guaidaodl.pomodorotimer.R;
import io.github.guaidaodl.pomodorotimer.data.realm.Tomato;
import io.github.guaidaodl.pomodorotimer.ui.statistics.timeline.TimeLineActivity;
import io.github.guaidaodl.pomodorotimer.ui.widget.LineChartView;
import io.github.guaidaodl.pomodorotimer.utils.DateUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class StatisticsFragment extends Fragment implements StatisticsContract.View {

    @BindView(R.id.statistics_count)
    View mCountView;
    @BindView(R.id.statistic_day_count)
    TextView mDayCountTextView;

    @BindView(R.id.statistic_week_count)
    TextView mWeekCountTextView;

    @BindView(R.id.statistic_month_count)
    TextView mMonthCountTextView;

    @BindView(R.id.statistic_line_chart_View)
    LineChartView mLineChartView;

    private StatisticsContract.Presenter mPresenter;

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    public StatisticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        ButterKnife.bind(this, rootView);

        mCountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeLineActivity.startActivity(getActivity());
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }

    //<editor-fold desc="Implementation of StatisticsContract.View">
    @Override
    public void setPresenter(@NonNull StatisticsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showTodayTomatoCount(int tomatoCount) {
        mDayCountTextView.setText(String.valueOf(tomatoCount));
    }

    @Override
    public void showWeekTomatoCount(int count) {
        mWeekCountTextView.setText(String.valueOf(count));
    }

    @Override
    public void showMonthTomatoCount(int count) {
        mMonthCountTextView.setText(String.valueOf(count));
    }

    @Override
    public void showLastSevenDaysTomatoStatistics(@NonNull ImmutableListMultimap<Long, Tomato> statistic) {
        checkNotNull(statistic);

        int []data = new int[7];
        long time = DateUtils.getTodayStartCalendar().getTimeInMillis();
        for (int i = 6; i >= 0; i--) {
            if (statistic.containsKey(time)) {
                data[i] = statistic.get(time).size();
            } else {
                data[i] = 0;
            }
            time -= 24 * 60 * 60 * 1000;
        }

        mLineChartView.setData(data);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
    //</editor-fold>
}
