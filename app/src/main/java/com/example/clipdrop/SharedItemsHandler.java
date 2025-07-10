package com.example.clipdrop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SharedItemsHandler extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent rintent = getIntent();
        Uri uri = rintent.getParcelableExtra(Intent.EXTRA_STREAM);
        Intent sintent = new Intent("com.example.STORAGE_BROADCAST");
        sintent.putExtra("uri",uri);
        sendBroadcast(sintent);
        finishAndRemoveTask();
    }
}
