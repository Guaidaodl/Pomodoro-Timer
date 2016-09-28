package guaidaodl.github.io.pomodorotimer;

import android.app.Application;
import android.util.Log;

import guaidaodl.github.io.pomodorotimer.service.PomodoroService;

/**
 * Created by linyb on 28/09/2016.
 */

public class PomodoroApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Application", "create");

        PomodoroService.start(this);
    }
}
