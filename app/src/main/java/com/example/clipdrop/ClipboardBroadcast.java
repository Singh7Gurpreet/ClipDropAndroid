package com.example.clipdrop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class ClipboardBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.example.CLIPBOARD_CONTENT".equals(intent.getAction())) {
            String text = intent.getStringExtra("data");
            Log.d("data",text);
        }
    }
}
