package com.example.clipdrop;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationSyncReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.clipdrop.ACTION_SYNC".equals(intent.getAction())) {
            Toast.makeText(context,"Hello button is clicked",Toast.LENGTH_SHORT).show();
        }
    }
}
