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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import io.github.guaidaodl.pomodorotimer.R;
import io.github.guaidaodl.pomodorotimer.service.PomodoroService;
import io.github.guaidaodl.pomodorotimer.ui.main.MainActivity;

public class NotificationHelper {
    private static final int NOTIFICATION_ID_FOREGROUND = 1;
    private static final int NOTIFICATION_ID_BREAK = 2;

    private NotificationHelper(){
    }

    public static void startPomodoroTimerForeground(PomodoroService service) {
        Notification notification = buildNotificationForForeService(service.getApplicationContext());

        service.startForeground(NOTIFICATION_ID_FOREGROUND, notification);
    }
    private static Notification buildNotificationForForeService(Context context) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        return notificationBuilder.setSmallIcon(R.drawable.ic_nofication)
                .setTicker("Test Notification")
                .setContentIntent(pi)
                .build();
    }


    public static void showBreakNotification(Context context) {
        NotificationManager manager =
               (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIFICATION_ID_BREAK, buildNotificationForBreak(context));
    }

    public static void cancelBreakNotification(Context context) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.cancel(NOTIFICATION_ID_BREAK);
    }

    /**
     * Builds a notification to alert user to take a break after completed a tomato.
     */
    private static Notification buildNotificationForBreak(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        return builder.setSmallIcon(R.drawable.ic_nofication)
                .setContentTitle(context.getString(R.string.notification_tomato_end_content_title))
                .setContentText(context.getString(R.string.notification_tomato_end_content_text))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .build();
    }
}
