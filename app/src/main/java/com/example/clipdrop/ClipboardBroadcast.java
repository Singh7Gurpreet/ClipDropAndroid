package com.example.clipdrop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.FileOutputStream;

import helper.Authentication;
import helper.PersonalBinApiWrapper;
import helper.PersonalBinObject;
import helper.S3FileManager;

public class ClipboardBroadcast extends BroadcastReceiver {

    public String FILE_NAME_STORAGE = "text.txt";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.CLIPBOARD_CONTENT".equals(intent.getAction())) {
            String text = intent.getStringExtra("data");
            S3FileManager.downloadFile();
        }
    }
}
