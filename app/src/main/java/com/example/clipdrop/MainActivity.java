package com.example.clipdrop;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import helper.Authentication;
import helper.CustomCallback;

public class MainActivity extends AppCompatActivity {

    private Boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomCallback<String> callback = new CustomCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                loading = false;
            }

            @Override
            public void onFailure(String errorMessage) {
                loading = false;
            }
        };
        Authentication.initialize(this,callback);
    }
}