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

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.guaidaodl.pomodorotimer.R;
import io.github.guaidaodl.pomodorotimer.service.PomodoroService;
import io.github.guaidaodl.pomodorotimer.ui.statistics.StatisticsAcvitity;
import io.github.guaidaodl.pomodorotimer.ui.timer.TimerFragment;

public class MainActivity extends AppCompatActivity implements TimerFragment.TimerFragmentDelegate {
    @BindView(R.id.main_pager)
    ViewPager mMainViewPager;

    @BindView(R.id.main_tab)
    TabLayout mMainTabLayout;

    private PomodoroService.PomodoroBinder mPomodoroBinder;
    private List<PomodoroService.TomatoStateListener> mPendingListners = new LinkedList<>();
    private ServiceConnection mPomodoroServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPomodoroBinder = (PomodoroService.PomodoroBinder) service;
            for (PomodoroService.TomatoStateListener listener : mPendingListners) {
                mPomodoroBinder.registerTimeChangeListener(listener);
            }
            mPendingListners.clear();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mMainViewPager.setAdapter(new MainPagerAdapter(this, getSupportFragmentManager()));
        mMainTabLayout.setupWithViewPager(mMainViewPager);

        PomodoroService.bind(this, mPomodoroServiceConnection);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mMainViewPager.setCurrentItem(0, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_statistics: {
                StatisticsAcvitity.startActivity(this);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PomodoroService.unbind(this, mPomodoroServiceConnection);
    }

    //<editor-fold desc="Implemation of TimerFragmentDelegate">
    @Override
    public void startNewTomato() {
        mPomodoroBinder.startNewTomato(25);
    }

    @Override
    public void stopTomato() {
        mPomodoroBinder.stopTomato();
    }

    /**
     * 像 Service 注册 TomatoListner，如果 mPomodoroBinder 还为空，则将 listener 放入一个
     * pennding 的列表中，等到 Activity 跟 Service 绑定后再注册。
     */
    @Override
    public void registerTomatoListner(PomodoroService.TomatoStateListener listener) {
        if (mPomodoroBinder != null) {
            mPomodoroBinder.registerTimeChangeListener(listener);
        } else {
            mPendingListners.add(listener);
        }
    }

    @Override
    public void unregisterTomatoListener(PomodoroService.TomatoStateListener listener) {
        mPomodoroBinder.unregisterTimeChangeListener(listener);
    }
    //</editor-fold>

}
