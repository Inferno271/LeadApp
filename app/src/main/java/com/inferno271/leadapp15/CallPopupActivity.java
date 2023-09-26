package com.inferno271.leadapp15;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import models.Call;

public class CallPopupActivity extends AppCompatActivity {


    public static final String ACTION_CLOSE_POPUP = "com.inferno271.leadapp.ACTION_CLOSE_POPUP";
    private static final String MyTAG = "CallPopupService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MyTAG, "onCreate: Активность создана");
        // Устанавливаем флаги для активности, чтобы она была поверх всех окон
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );

        setContentView(R.layout.activity_call_popup);

        // Находим корневой макет и добавляем слушатель для закрытия окна при клике
        RelativeLayout rootLayout = findViewById(R.id.rootLayout);
        rootLayout.setOnClickListener(v -> closeCallPopup());

        // Получаем информацию о последнем звонке и отображаем всплывающее окно, если есть данные
        Call lastCall = getLastCall(this);
        if (lastCall != null) {
            showCallPopup(lastCall);
        }
    }

    // Метод для открытия фрагмента внутри активности
    private void openFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Метод для получения информации о последнем звонке
    private Call getLastCall(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            int callerNameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int phoneNumberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int callDateTimeIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
            int callDurationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

            long lastCallId = generateUniqueCallId();

            if (callerNameIndex != -1 && phoneNumberIndex != -1 && callDateTimeIndex != -1 && callDurationIndex != -1) {
                String callerName = cursor.getString(callerNameIndex);
                String phoneNumber = cursor.getString(phoneNumberIndex);
                String callDateTime = cursor.getString(callDateTimeIndex);
                int callDuration = cursor.getInt(callDurationIndex);
                String additionalInfo = "";

                // Получаем имя контакта по номеру телефона, если доступно
                String contactName = getContactName(context, phoneNumber);
                if (contactName != null && !contactName.isEmpty()) {
                    callerName = contactName;
                }

                cursor.close();

                // Создаем объект Call, представляющий звонок
                return new Call(lastCallId, callerName, phoneNumber, callDateTime, callDuration, additionalInfo);
            }
        }

        return null;
    }

    // Метод для генерации уникального идентификатора звонка
    private long generateUniqueCallId() {
        return System.currentTimeMillis();
    }

    // Метод для получения имени контакта по номеру телефона
    private String getContactName(Context context, String phoneNumber) {
        ContentResolver contentResolver = context.getContentResolver();
        phoneNumber = phoneNumber.replaceAll("[^0-9+]", "");
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER + " = ?",
                new String[]{phoneNumber},
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                if (columnIndex != -1) {
                    String contactName = cursor.getString(columnIndex);
                    cursor.close();
                    return contactName;
                }
            }
            cursor.close();
        }

        return "";
    }

    // Метод для отображения всплывающего окна звонка
    private void showCallPopup(Call lastCall) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("call", lastCall);

        openFragment(CallPopupFragment.newInstance(lastCall), bundle);
    }

    // Метод для закрытия всплывающего окна
    private void closeCallPopup() {
        // Отправляем широковещательное сообщение для закрытия всплывающего окна
        Intent closePopupIntent = new Intent(ACTION_CLOSE_POPUP);
        sendBroadcast(closePopupIntent);
        finish(); // Закрываем активность, чтобы закрыть всплывающее окно
    }
}
