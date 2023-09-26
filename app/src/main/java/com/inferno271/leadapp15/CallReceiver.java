package com.inferno271.leadapp15;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

    private static final String MyTAG = "CallPopupService";
    private boolean isPopupShowing = false;
    private static CallReceiver instance;

    public static void register(Context context) {
        if (instance == null) {
            instance = new CallReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
            filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
            context.registerReceiver(instance, filter);
        }
    }

    public static void unregister(Context context) {
        if (instance != null) {
            context.unregisterReceiver(instance);
            instance = null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int otl = 0;
        if (action != null) {
            if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
                String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                if (phoneState != null) {
                    if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        // Входящий звонок
                        Log.d(MyTAG, "Входящий звонок");
                        // Показать всплывающее окно
                        showCallPopup(context);
                    } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        // Завершение звонка (входящего или исходящего)
                        Log.d(MyTAG, "Завершение звонка (входящего или исходящего)");
                        // Закрыть всплывающее окно
                        showCallPopup(context);
                    }
                }
            } else if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                // Исходящий звонок
                String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.d(MyTAG, "Исходящий звонок: " + phoneNumber);
                // Показать всплывающее окно
                otl =1111;
                showCallPopup(context);
            }
        }
    }



    private void showCallPopup(Context context) {
        if (!isPopupShowing) {
            // Отобразить всплывающее окно, например, запустив активность CallPopupActivity
            Intent popupIntent = new Intent(context, CallPopupActivity.class);
            popupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(popupIntent);
            isPopupShowing = true;
        }
    }
}
