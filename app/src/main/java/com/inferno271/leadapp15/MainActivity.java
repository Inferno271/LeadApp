package com.inferno271.leadapp15;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 123;
    private static final String CHANNEL_ID = "CallPopupChannel"; // Уникальный идентификатор канала уведомлений

    private static final String MyTAG = "CallPopupService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Создаем канал уведомлений (необходимо только для Android 8.0 и выше)
        createNotificationChannel();

        // Проверяем, есть ли необходимые разрешения
        if (!hasPermissions()) {
            // Если разрешения отсутствуют, запрашиваем их у пользователя
            requestPermissions();
        } else {
            // Если разрешения есть, запускаем службу CallPopupService в режиме Foreground
            startForegroundService();
        }
    }

    // Метод для проверки наличия разрешений
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS);
    }

    // Метод для запроса разрешений
    @AfterPermissionGranted(PERMISSIONS_REQUEST_CODE)
    private void requestPermissions() {
        Log.i(MyTAG, "onCreate: Активность работает");

        String[] perms;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Если устройство работает на Android 8.0 (API 26) и выше, включаем MANAGE_OWN_CALLS
            perms = new String[]{
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.MANAGE_OWN_CALLS};
        } else {
            // Для устройств с API уровнем ниже 26, исключаем MANAGE_OWN_CALLS
            perms = new String[]{
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS};
        }

        if (EasyPermissions.hasPermissions(this, perms)) {
            // Если разрешения предоставлены, запускаем службу CallPopupService в режиме Foreground
            startForegroundService();
        } else {
            // Если разрешения не предоставлены, запрашиваем их у пользователя
            EasyPermissions.requestPermissions(
                    this,
                    "Для работы приложения требуются разрешения на чтение журнала звонков, состояния телефона и контактов.",
                    PERMISSIONS_REQUEST_CODE,
                    perms
            );
        }
    }

    // Метод для создания канала уведомлений (для Android 8.0 и выше)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CallPopupChannelName"; // Название канала
            String description = "CallPopupChannelDescription"; // Описание канала
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Метод для запуска службы CallPopupService в режиме Foreground
    private void startForegroundService() {
        // Создаем уведомление для Foreground Service
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Ваше приложение работает в фоновом режиме")
                .setContentText("Текст уведомления")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Установка высокого приоритета
                .build();

        // Запускаем службу CallPopupService в режиме Foreground с указанным уведомлением
        Intent serviceIntent = new Intent(this, CallPopupService.class);
        serviceIntent.putExtra("notification", notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        Log.i(MyTAG, "onCreate: уведомление создано и служба запущена в режиме Foreground");
    }



    // Другие методы и код вашей MainActivity
}