package com.inferno271.leadapp15;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OutgoingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            // Здесь вы можете обработать исходящий звонок и выполнить необходимые действия
            // Например, вы можете открыть вашу активность CallPopupActivity и передать информацию о звонке
            Intent popupIntent = new Intent(context, CallPopupActivity.class);
            popupIntent.putExtra("phone_number", phoneNumber);
            popupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(popupIntent);
        }
    }
}
