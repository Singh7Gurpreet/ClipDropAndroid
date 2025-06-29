package com.example.clipdrop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationSyncReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.clipdrop.ACTION_SYNC".equals(intent.getAction())) {
            Intent newIntent = new Intent(context,ClipBoardActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // required for starting from non-Activity
            context.startActivity(newIntent);
            Toast.makeText(context,"Hello button is clicked",Toast.LENGTH_LONG).show();
        }
    }
}
