package com.example.clipdrop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import helper.Authentication;
import helper.CustomCallback;
import helper.PersonalBinApiWrapper;
import helper.PersonalBinObject;
import helper.S3FileManager;

public class ClipboardBroadcast extends BroadcastReceiver {
    Context myContext;
    public String FILE_NAME_STORAGE = "text.txt";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.CLIPBOARD_CONTENT".equals(intent.getAction())) {
            String text = intent.getStringExtra("data");
            myContext = context;
            // if is true simple copy that content to clipboard

            CustomCallback<Boolean> c = new CustomCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    Log.d("RESULT", String.valueOf(result));
                }

                @Override
                public void onFailure(Boolean errorMessage) {
                    Log.e("ERROR in C", String.valueOf(errorMessage));
                }
            };

            S3FileManager.downloadFile(context, c);
            // else paste from clipboard and then create file and call s3 upload function
        }
    }

    private void readFile() {
        try {
            File file = new File(myContext.getFilesDir(), FILE_NAME_STORAGE);
            FileInputStream fin = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fin); // convert bytes to characters

            int chr;
            while ((chr = reader.read()) != -1) {
                System.out.print((char) chr); // cast to char and print
            }

            reader.close();
            fin.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
