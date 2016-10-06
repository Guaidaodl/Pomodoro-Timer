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

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.util.Pair;

import java.util.List;

import io.github.guaidaodl.pomodorotimer.data.TomatoRepository;
import io.github.guaidaodl.pomodorotimer.data.realm.Tomato;
import io.github.guaidaodl.pomodorotimer.utils.DateUtils;
import io.github.guaidaodl.pomodorotimer.utils.shedulers.BaseSchedulerProvider;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

class StatisticsPresenter implements StatisticsContract.Presenter {
    @NonNull
    private final TomatoRepository mTomatoRepository;
    @NonNull
    private final StatisticsContract.View mStatisticsView;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private CompositeSubscription mSubscriptions;

    StatisticsPresenter(@NonNull TomatoRepository tomatoRepository,
                               @NonNull BaseSchedulerProvider schedulerProvider,
                               @NonNull StatisticsContract.View statisticsView) {
        mTomatoRepository  = checkNotNull(tomatoRepository, "tomatoRespository can not be null");
        mStatisticsView    = checkNotNull(statisticsView, "statisticsView can not be null");
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider can not be null");

        mSubscriptions = new CompositeSubscription();
        mStatisticsView.setPresenter(this);
    }

    @UiThread
    @Override
    public void subscribe() {
        loadStatistics();
    }

    @UiThread
    @Override
    public void unSubscribe() {
        mSubscriptions.unsubscribe();
    }

    private void loadStatistics() {
        Pair<Long, Long> todayTimePair = DateUtils.getTodayTime();
        Subscription daySubscription =
                mTomatoRepository.getTomatoWithStartTimeBetween(todayTimePair.first, todayTimePair.second)
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Action1<List<Tomato>>() {
                    @Override
                    public void call(List<Tomato> tomatos) {
                        mStatisticsView.showTodayTomatoCount(tomatos.size());
                    }
                });
        mSubscriptions.add(daySubscription);


        Pair<Long, Long> weekTime = DateUtils.getWeekTime();
        Subscription weekSubscription =
                mTomatoRepository.getTomatoWithStartTimeBetween(weekTime.first, weekTime.second)
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(new Action1<List<Tomato>>() {
                            @Override
                            public void call(List<Tomato> tomatos) {
                                mStatisticsView.showWeekTomatoCount(tomatos.size());
                            }
                        });
        mSubscriptions.add(weekSubscription);

        Pair<Long, Long> monthTime = DateUtils.getMonthTime();
        Subscription monthSubscription =
                mTomatoRepository.getTomatoWithStartTimeBetween(monthTime.first, monthTime.second)
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(new Action1<List<Tomato>>() {
                            @Override
                            public void call(List<Tomato> tomatos) {
                                mStatisticsView.showMonthTomatoCount(tomatos.size());
                            }
                        });
        mSubscriptions.add(monthSubscription);
    }
}
