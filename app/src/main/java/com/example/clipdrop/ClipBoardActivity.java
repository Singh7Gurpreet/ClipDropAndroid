package com.example.clipdrop;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ClipBoardActivity extends AppCompatActivity {

    ClipboardBroadcast c = new ClipboardBroadcast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clipboard_activity);
        IntentFilter filter = new IntentFilter("com.example.CLIPBOARD_CONTENT");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(c, filter, Context.RECEIVER_NOT_EXPORTED);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            fetchContent();
            finishAndRemoveTask();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(c);
        super.onDestroy();
    }

    private void fetchContent() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        Intent intent = new Intent("com.example.CLIPBOARD_CONTENT");
        intent.setPackage(getPackageName());
        if (clipboard.hasPrimaryClip()) {
            ClipData clipData = clipboard.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                CharSequence text = clipData.getItemAt(0).getText();
                if (text != null) {
                    intent.putExtra("data",text.toString());
                }
            }
        }
        sendBroadcast(intent);
    }
}