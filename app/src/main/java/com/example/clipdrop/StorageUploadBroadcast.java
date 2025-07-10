package com.example.clipdrop;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.Objects;

import helper.S3FileManager;
import helper.TYPE_OF_FILE;

public class StorageUploadBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(),"com.example.STORAGE_BROADCAST")) {
            Uri uri = intent.getParcelableExtra("uri");
            S3FileManager.uploadFile(context,uri, TYPE_OF_FILE.STORAGE);
        }
    }
}
