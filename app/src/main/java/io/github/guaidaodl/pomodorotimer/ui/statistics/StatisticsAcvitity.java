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

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.guaidaodl.pomodorotimer.Injection;
import io.github.guaidaodl.pomodorotimer.R;

public class StatisticsAcvitity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, StatisticsAcvitity.class);

        context.startActivity(intent);
    }

    @BindView(R.id.statistic_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        FragmentManager fm = getSupportFragmentManager();
        StatisticsFragment statisticsFragment = (StatisticsFragment) fm
                .findFragmentById(R.id.statistic_fragment);
        if (statisticsFragment == null) {
            statisticsFragment = StatisticsFragment.newInstance();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.statistic_fragment, statisticsFragment);
            transaction.commit();
        }

        // create Presenter
        new StatisticsPresenter(Injection.provideTaskRespository(),
                Injection.providerBaseSchedulerProvider(),
                statisticsFragment);
    }

    /**
     * 初始化 ActionBar，显示返回按钮。
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
