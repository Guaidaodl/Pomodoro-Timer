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

import butterknife.BindView;
import butterknife.ButterKnife;
import guaidaodl.github.io.pomodorotimer.R;
import guaidaodl.github.io.pomodorotimer.service.PomodoroService;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_pager)
    ViewPager mMainViewPager;

    @BindView(R.id.main_tab)
    TabLayout mMainTabLayout;

    private PomodoroService.PomodoroBinder mPomodoroBinder;
    private ServiceConnection mPomodoroServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPomodoroBinder = (PomodoroService.PomodoroBinder) service;
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

}
