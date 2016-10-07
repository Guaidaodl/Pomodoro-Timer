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

package io.github.guaidaodl.pomodorotimer.utils;

import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Pair;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtils {
    private DateUtils(){}

    public static Pair<Long, Long> getTodayTime() {
        Calendar calendar = getTodayStartCalendar();

        long startMilliSecondOfToday = calendar.getTimeInMillis();
        long endMilliSecondOfToday = startMilliSecondOfToday + 24L * 60L * 60L * 1000L;
        return Pair.create(startMilliSecondOfToday, endMilliSecondOfToday);
    }

    public static Pair<Long, Long> getCurrentWeekTime() {
        return getWeekTime(getTodayStartCalendar());
    }

    @VisibleForTesting
    static Pair<Long, Long> getWeekTime(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek + 1);

        long startMilliSecondOfToday = calendar.getTimeInMillis();
        long endMilliSecondOfToday = startMilliSecondOfToday + 7L * 24L * 60L * 60L * 1000L;

        return Pair.create(startMilliSecondOfToday, endMilliSecondOfToday);
    }

    public static Pair<Long, Long> getCurrentMonthTime() {
        return getMonthTime(getTodayStartCalendar());
    }

    @VisibleForTesting
    static Pair<Long, Long> getMonthTime(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        long startMilliSecondOfToday = calendar.getTimeInMillis();
        long endMilliSecondOfToday = startMilliSecondOfToday + dayCount * 24L * 60L * 60L * 1000L;

        return Pair.create(startMilliSecondOfToday, endMilliSecondOfToday);
    }

    private static Calendar getTodayStartCalendar() {
        Calendar temp = new GregorianCalendar();
        return new GregorianCalendar(temp.get(Calendar.YEAR), temp.get(Calendar.MONTH),
                temp.get(Calendar.DAY_OF_MONTH));
    }
}
