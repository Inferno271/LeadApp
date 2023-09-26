package com.inferno271.leadapp15;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class CallPopupService extends Service {

    // Идентификатор уведомления (должен совпадать с тем, что используется в MainActivity)
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("notification")) {
            Notification notification = intent.getParcelableExtra("notification");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Если Android 8.0 и выше, стартуем службу в режиме Foreground с уведомлением
                startForeground(NOTIFICATION_ID, notification);
            }
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
