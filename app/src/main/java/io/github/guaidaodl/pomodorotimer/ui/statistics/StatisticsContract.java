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

import com.google.common.collect.ImmutableListMultimap;

import io.github.guaidaodl.pomodorotimer.base.BasePresenter;
import io.github.guaidaodl.pomodorotimer.base.BaseView;
import io.github.guaidaodl.pomodorotimer.data.realm.Tomato;

class StatisticsContract {
    interface View extends BaseView<Presenter> {
        void showTodayTomatoCount(int Count);
        void showWeekTomatoCount(int count);
        void showMonthTomatoCount(int count);

        /**
         * show the statistics in last seven days.
         */
        void showLastSevenDaysTomatoStatistics(@NonNull ImmutableListMultimap<Long, Tomato> statistic);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }
}
