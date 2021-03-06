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

package io.github.guaidaodl.pomodorotimer.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.guaidaodl.pomodorotimer.R;
import io.github.guaidaodl.pomodorotimer.ui.timer.TimerFragment;

/**
 * Created by linyb on 27/09/2016.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private String mTimerTilte;
    private String mTodoTodayTitle;
    private String mActionListTitle;

    public MainPagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        mTimerTilte      = context.getString(R.string.main_tab_timer);
        mTodoTodayTitle  = context.getString(R.string.main_tab_todo_today);
        mActionListTitle = context.getString(R.string.main_tab_action_list);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TimerFragment.newInstance();
            default:
                return BlankFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mTimerTilte;
            case 1:
                return mTodoTodayTitle;
            case 2:
                return  mActionListTitle;
            default:
                throw new IllegalArgumentException("The position should be 0, 1 or 2, but the actual value is " + position);
        }
    }
}
