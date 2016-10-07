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

import android.support.v4.util.Pair;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.hamcrest.core.IsEqual.equalTo;

public class DateUtilsTest {
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDayInMonth;

    @Before
    public void setUp() {
        Calendar calendar = Calendar.getInstance();

        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentMonth = calendar.get(Calendar.MONTH);
        mCurrentDayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Test
    public void getTodayTime() throws Exception {
        Pair<Long, Long> todayTime = DateUtils.getTodayTime();

        Calendar startCalendar = new GregorianCalendar(mCurrentYear, mCurrentMonth, mCurrentDayInMonth, 0, 0, 0);

        Assert.assertThat(todayTime.first, equalTo(startCalendar.getTimeInMillis()));
    }

    @Test
    public void testGetWeekTime() throws Exception {
        Calendar c = new GregorianCalendar(2016, Calendar.JANUARY, 1);
        Pair<Long, Long> testWeekTime = DateUtils.getWeekTime(c);

        long expectedStartTime = new GregorianCalendar(2015, Calendar.DECEMBER, 27).getTimeInMillis();
        long expectedEndTime = new GregorianCalendar(2016, Calendar.JANUARY, 3).getTimeInMillis();

        Assert.assertThat("The start time should be 2016-9-25 0:00:00, but actual is " + formatDate(testWeekTime.first),
                            testWeekTime.first, equalTo(expectedStartTime));
        Assert.assertThat("The end time should be 2016-10-2 0:00:00 but actual is " + formatDate(testWeekTime.second),
                            testWeekTime.second, equalTo(expectedEndTime));
    }

    @Test
    public void testGetMonthTime() throws Exception {
        Calendar c = new GregorianCalendar(2012, Calendar.FEBRUARY, 23);
        Pair<Long, Long> monthTime = DateUtils.getMonthTime(c);

        long expectedStartTime = new GregorianCalendar(2012, Calendar.FEBRUARY, 1).getTimeInMillis();
        long expectedEndTime = new GregorianCalendar(2012, Calendar.MARCH, 1).getTimeInMillis();

        Assert.assertThat("The start time should be 2012-2-1 0:00:00, but actual is " + formatDate(monthTime.first),
                monthTime.first, equalTo(expectedStartTime));
        Assert.assertThat("The end time should be 2012-3-1 0:00:00, but actual is " + formatDate(monthTime.second),
                monthTime.second, equalTo(expectedEndTime));

        c = new GregorianCalendar(2014, Calendar.FEBRUARY, 23);
        monthTime = DateUtils.getMonthTime(c);

        expectedStartTime = new GregorianCalendar(2014, Calendar.FEBRUARY, 1).getTimeInMillis();
        expectedEndTime = new GregorianCalendar(2014, Calendar.MARCH, 1).getTimeInMillis();

        Assert.assertThat("The start time should be 2014-2-1 0:00:00, but actual is " + formatDate(monthTime.first),
                monthTime.first, equalTo(expectedStartTime));
        Assert.assertThat("The end time should be 2014-3-1 0:00:00, but actual is " + formatDate(monthTime.second),
                monthTime.second, equalTo(expectedEndTime));

        c = new GregorianCalendar(2016, Calendar.OCTOBER, 2);
        monthTime = DateUtils.getMonthTime(c);

        expectedStartTime = new GregorianCalendar(2016, Calendar.OCTOBER, 1).getTimeInMillis();
        expectedEndTime = new GregorianCalendar(2016, Calendar.NOVEMBER, 1).getTimeInMillis();

        Assert.assertThat("The start time should be 2016-10-1 0:00:00, but actual is " + formatDate(monthTime.first),
                monthTime.first, equalTo(expectedStartTime));
        Assert.assertThat("The end time should be 2016-11-1 0:00:00, but actual is " + formatDate(monthTime.second),
                monthTime.second, equalTo(expectedEndTime));
    }

    private String formatDate(long t) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(t);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(calendar.getTime());
    }
}