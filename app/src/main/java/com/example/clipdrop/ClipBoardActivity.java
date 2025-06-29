package com.example.clipdrop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import helper.Authentication;
import helper.CustomCallback;

public class ClipBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clipboard_activity);

        Log.d("ClipBoardActivity", "Activity started!");

        new Handler(Looper.getMainLooper()).postDelayed(this::finish, 5000);
    }
}