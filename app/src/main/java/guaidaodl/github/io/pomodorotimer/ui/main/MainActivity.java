package guaidaodl.github.io.pomodorotimer.ui.main;

import android.content.ComponentName;
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
import guaidaodl.github.io.pomodorotimer.R;
import guaidaodl.github.io.pomodorotimer.service.PomodoroService;
import guaidaodl.github.io.pomodorotimer.ui.timer.TimerFragment;

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
